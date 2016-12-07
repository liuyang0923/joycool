package net.joycool.wap.spec.castle;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.SqlUtil;

public class BuildingAction extends CastleBaseAction {
	static CacheService cacheService = CacheService.getInstance();
	public BuildingAction() {
		super();
	}

	public BuildingAction(HttpServletRequest request) {
		super(request);
	}

	public BuildingAction(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}
	
	/**
	 * 升级一个建筑
	 * @return
	 */
	public boolean upgrade(){
		int pos = getParameterInt("pos");
		int type = getParameterInt("type");
		BuildingBean bean = null;
		long endTime;
		synchronized(userResBean) {
		
			List cacheList = cacheService.getCacheBuildingByCid(castle.getId());
			if(CastleUtil.containCacheBuildingPos(cacheList, pos)) {
				request.setAttribute("msg", "该位置已经有建筑在建造中");
				return false;
			}
			
			if(pos >= 19 && CastleUtil.containCacheBuildingType(cacheList, type)) {
				request.setAttribute("msg", "该建筑已经在建造中");
				return false;
			}
			if(type == 0)	// 如果是升级建筑，type必须等于0
				bean = castleService.getBuildingBeanByPos(castle.getId(), pos);
			else {
				if(castleService.getBuildingBeanByPos(castle.getId(), pos) != null) {
					request.setAttribute("msg", "该位置已有建筑");
					return false;
				}
			}
			
			if(bean == null) {
				BuildingTBean btnew = ResNeed.getBuildingT(type);	// 先修数据保存在template
				// 非本种族的建筑无法建造
				if(pos > ResNeed.castlePosCount || pos < 0 || castle.isNatar() && pos > ResNeed.castlePosCountNatar) {
					request.setAttribute("msg", "无法建造");
					return false;
				}
				
	//			if(btnew.isFlagMain()) {
	//				if(!isMainCastle()) {
	//					request.setAttribute("msg", "无法建造");
	//					return false;
	//				}
	//			} else if(btnew.isFlagNotMain()){
	//				if(isMainCastle()) {
	//					request.setAttribute("msg", "无法建造");
	//					return false;
	//				}
	//			}
				if(pos < 19) {
					if(type != ResNeed.baseBuildRes[castle.getType2()][pos]) {
						request.setAttribute("msg", "无法建造");
						return false;
					}
				} else if(type < 4 || type == 9) {
					request.setAttribute("msg", "无法建造");
					return false;
				}
				
	//			if(type == ResNeed.PALACE2_BUILD && castleUser.isFlagPalace()) {
	//				request.setAttribute("msg", "无法建造");
	//				return false;
	//			}
				
				if(!userResBean.canBuild(btnew)) {
					request.setAttribute("msg", "没有达到建造条件");
					return false;
				}
				if(pos >= 19) {
	//				int oldGrade = userResBean.getBuildingGrade(type);	// 其他同类型建筑等级
	//				if(oldGrade > 0 && (oldGrade < btnew.getMaxGrade() || !btnew.isFlagRebuild())){
	//					request.setAttribute("msg", "没有达到建造条件");
	//					return false;
	//				}
					if(castle.isNatar()) {
						if(pos > 36 || type == ResNeed.WONDER_BUILD && pos != 19 || type != ResNeed.WONDER_BUILD && pos == 19
								|| type == ResNeed.GATHER_BUILD && pos != 20 || type != ResNeed.GATHER_BUILD && pos == 20
								|| type == ResNeed.CITY_BUILD && pos != 21 || type != ResNeed.CITY_BUILD && pos == 21) {
							request.setAttribute("msg", "无法建造");
							return false;
						}
					}
					if(!canBuild(btnew)) {
						request.setAttribute("msg", "无法建造");
						return false;
					}
				}
				bean = new BuildingBean(type, 0, castle.getId(), 1, pos);
			} else
				type = bean.getBuildType();
			// 判断建筑图纸
			if(type == ResNeed.WONDER_BUILD) {
				if(!CastleUtil.userHasArt(castleUser.getUid(), 1)) {
					request.setAttribute("msg", "没有建筑图纸,无法建造");
					return false;
				}
				if(bean.getGrade() >= 50) {	// 高于50级需要另一个图纸
					if(castleUser.getTong() == 0 || !CastleUtil.tongHasArt(castleUser, 1)) {
						request.setAttribute("msg", "缺少建筑图纸,无法建造");
						return false;
					}
				}
			}
			
			// 最高等级检查
			boolean notMain = (castleUser.getMain() != castle.getId());
			if(bean.getGrade() >= ResNeed.getBuildingT(bean.getBuildType()).getMaxGrade()
					|| notMain && bean.getGrade() >= 10 && pos < 19) {
				request.setAttribute("msg", "该建筑已建造完成");
				return false;
			}
	
			BuildingTBean bt = ResNeed.getBuildingT(bean.getBuildType(), bean.getGrade() + 1);
	
			int time = userResBean.calcBuildTime(bt.getTime());
			if(SqlUtil.isTest)	time=10;
			
			long now = System.currentTimeMillis();
	//		//检查升级限制
	//		if(!checkUpdate(type, bt.getGrade())){
	//			return false;
	//		}
			// 粮食产量必须大于所有建筑占的人口
			if(bean.getBuildType() != 1 && userResBean.getGrainRealSpeed() <= bt.getPeople()) {
				request.setAttribute("msg", "粮食产量不足,需要先建造一个粮田.");
				return false;
			}
	
			if(castle.getRace() == 1) {		// 这个种族可以同时建造一个建筑和一个资源
				if(type < 4 || type == 9)
					endTime = CastleUtil.getCacheBuildingTime1(cacheList);
				else
					endTime = CastleUtil.getCacheBuildingTime2(cacheList);
			} else {
				endTime = CastleUtil.getCacheBuildingTime3(cacheList);
			}
			if(endTime != 0) {
				if(CastleUtil.getCastleUser(userBean.getId()).isSpAccount(now)) {
					if(CastleUtil.containCacheBuildingQueue(cacheList)) {
						request.setAttribute("msg","建造队列已满");
						return false;
					}
				} else {
					request.setAttribute("msg","已经有建筑在建造中");
					return false;
				}
			}
			
			if(!userResBean.decreaseRes(bt.getWood(), bt.getStone(), bt.getFe(), bt.getGrain())) {
				request.setAttribute("msg", "资源不足");
				return false;
			}
			if(bean.getGrade() == 0)	// 如果是新建，则立刻产生一条等级0的记录，升级完成后改为1
				castleService.addBuilding(bean);
			
			BuildingThreadBean buildingThreadBean = new BuildingThreadBean(bt, time, castle.getId(), bean.getBuildPos(), endTime);
	
			cacheService.addCacheBuilding(buildingThreadBean);

		}
		if(bean.getGrade() == 0) {
			if(bean.getBuildType() == ResNeed.PALACE2_BUILD) {	// 皇宫要特殊处理
				castleUser.addFlag(CastleUserBean.FLAG_PALACE);
				castleService.updateUserFlag(castleUser);
			}
		}
		if(endTime != 0)
			request.setAttribute("msg", "已加入建造队列");
		else if(bean.getGrade() == 0) {
			request.setAttribute("msg", "建筑开始建造");
		} else
			request.setAttribute("msg", "建筑开始升级");
		return true;
	}	
	
	/**
	 * 取消一个正在升级的建筑
	 * @return
	 */
	public boolean cancel(){
		int id = getParameterInt("id");
		
		BuildingThreadBean bean;
		synchronized(BuildingThread.class) {
			bean = cacheService.getCacheBuildBean(id);
			
			if(bean == null) {
				request.setAttribute("msg", "没有该建造");
				return false;
			}
	
			if(bean.getCid() != castle.getId()) {
				request.setAttribute("msg", "建造取消失败");
				return false;
			}
	
			cacheService.deleteCacheBuilding(id);
			if(bean.getGrade() == 1) {
				castleService.deleteBuilding(bean);
				if(bean.getType() == ResNeed.PALACE2_BUILD) {	// 皇宫要特殊处理
					castleUser.deleteFlag(CastleUserBean.FLAG_PALACE);
					castleService.updateUserFlag(castleUser);
				}
			}
		}
		BuildingTBean bt = ResNeed.getBuildingT(bean.getType(), bean.getGrade());
		//更新缓存和数据库的资源
		long now = System.currentTimeMillis();
		if(bean.getStartTime() > now)	// 如果是建造队列并且未开始建造，返回所有资源
			CastleUtil.increaseUserRes(userResBean, bt.getWood(), bt.getFe(), bt.getGrain(), bt.getStone());
		else {
			float fund = (float)(bean.getEndTime() - now) / (bean.getEndTime() - bean.getStartTime());	// 返回的百分比
			if(fund > 0.6f)
				fund = 0.6f;
			if(fund > 0)
				CastleUtil.increaseUserRes(userResBean, (int)(bt.getWood() * fund), (int)(bt.getFe() * fund)
						, (int)(bt.getGrain() * fund), (int)(bt.getStone() * fund));
		}
		request.setAttribute("msg", "建造取消成功");
		return true;
	}
	// 拆一个建筑一级，拆的时间是建造的1/4
	public boolean dupgrade(){
		if(hasParam("cancel")) {
			cancelDupgrade();
			return false;
		}
		int pos = getParameterInt("pos");
		if(pos < 19)	// 返回false表示显示选择页面，否则显示处理结果页面
			return false;
		BuildingBean bean = castleService.getBuildingBeanByPos(castle.getId(), pos);
		if(bean == null || bean.getGrade() == 0)
			return false;
		
		int mainGrade = userResBean.getBuildingGrade(4);	// 城堡等级
		if(mainGrade < 10)
			return false;
		
		if(cacheService.containCommon(castle.getId(), 2)) {
			request.setAttribute("msg", "已有建筑在拆毁中");
			return true;
		}
		
		BuildingTBean bt = ResNeed.getBuildingT(bean.getBuildType(), bean.getGrade());
		
		int time = ResNeed.getGradeTime(mainGrade, bt.getTime()) / 4;// 拆除时间是建造的1/4
		if(SqlUtil.isTest)	time = 10;
		CommonThreadBean commonThreadBean = new CommonThreadBean(userBean.getId(), castle.getId(), 2, time, pos);
		cacheService.addCacheCommon(commonThreadBean);
		
		request.setAttribute("msg", "建筑开始拆毁");
		return true;
	}
	// 取消拆除
	public void cancelDupgrade(){
		int id = getParameterInt("cancel");
		if(id == 0)
			return;
		CommonThreadBean bean = cacheService.getCacheCommon(id);
		if(bean == null || bean.getCid() != castle.getId())
			return;
		cacheService.deleteCacheCommon(id);
	}
	
}
