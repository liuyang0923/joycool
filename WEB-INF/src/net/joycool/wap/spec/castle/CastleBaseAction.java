package net.joycool.wap.spec.castle;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;

public class CastleBaseAction extends CustomAction {
	
	protected UserBean userBean;
	static CastleService castleService = CastleService.getInstance();
	protected UserResBean userResBean;
	protected CastleBean castle;
	CastleUserBean castleUser;
	public byte curPage = 0;		// 保存当前所在页面，顶部四个链接分别是1 2 3 4 
	
	public CastleBean getCastle() {
		return castle;
	}

	public void setCastle(CastleBean castle) {
		this.castle = castle;
	}

	public CastleBaseAction() {
		super();
	}

	// 三个种族的商人携带
	public static int[] merchantCarry = {1, 1500, 3000, 2250};
	// 计算需要商人的数量
	public int getMerchantCount(int sum) {
		int carry = getMerchantCarry();
		return (sum - 1) / carry + 1;
	}
	// 计算商人运载
	public int getMerchantCarry() {
		int carry = merchantCarry[castle.getRace()];
		int tradeLevel = userResBean.getBuildingGrade(ResNeed.TRADE_BUILD);
		if(tradeLevel > 0)
			carry += carry * tradeLevel / 10;
		return carry;
	}
	
	public CastleBaseAction(HttpServletRequest request) {
		super(request);
		userBean = this.getLoginUser();
		
		//从缓存读取用户资源，如果没有则从数据库读取并保留至缓存
		castleUser = CastleUtil.getCastleUser(userBean.getId());
		if(castleUser == null)
			return;
		if(castleUser.getCur() == 0) {
			castleUser.setCur(castleUser.getMain());

		}
		long now = System.currentTimeMillis();
		if(castleUser.getLockTime() - now < lockJudgeTime) {
			castleUser.setLockTime(now + lockTime);
			SqlUtil.executeUpdate("update castle_user set lock_time=date_add(now(),interval 7 day) where uid=" + castleUser.getUid(), 5);
		}
		castle = CastleUtil.getCastleById(castleUser.getCur());

		userResBean = CastleUtil.getUserResBeanById(castle.getId());
	}
	
	public int getParameterInt2(String name) {
		try {
			return (request.getParameter(name) == null 
					|| request.getParameter(name).length() == 0) 
					? 0 : Integer.parseInt(request.getParameter(name).trim());
		} catch (Exception e) {
			return -1;
		}
	}
	
	
	
	public CastleUserBean getCastleUser() {
		return castleUser;
	}
	// 城堡攻防升级结果
	static SoldierSmithyBean[] zeroSmithy = new SoldierSmithyBean[ResNeed.soldierTypeCount + 1];
	public static ICacheMap castleSmithyCache = CacheManage.castleSmithy;
	public static SoldierSmithyBean[] getSmithys(int cid) {
		if(cid <= 0)	// 自然界
			return zeroSmithy;
		Integer key = new Integer(cid);
		synchronized(castleSmithyCache) {
			SoldierSmithyBean[] beans = (SoldierSmithyBean[])castleSmithyCache.get(key);
			if(beans == null) {
				beans = new SoldierSmithyBean[ResNeed.soldierTypeCount + 1];
				CastleService castleService = CastleService.getInstance();
				List list = castleService.getSoldierSmithy(cid);
				for(int i = 0;i < list.size();i++) {
					SoldierSmithyBean bean = (SoldierSmithyBean)list.get(i);
					beans[bean.getSoldierType()] = bean;
				}
				
				if(beans[1] == null) {
					beans[1] = new SoldierSmithyBean();
					beans[1].setCid(cid);
					beans[1].setSoldierType(1);
					castleService.addSoldierSmithy(beans[1]);
				}
				
				castleSmithyCache.put(key, beans);
			}
			return beans;
		}
	}
	public SoldierSmithyBean[] getSmithys() {
		return getSmithys(castle.getId());
	}
	public static long lockJudgeTime = DateUtil.MS_IN_DAY * 6;	// 6天内进行判断
	public static long lockTime = DateUtil.MS_IN_DAY * 7;	// 7天内不能锁
	public CastleBaseAction(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		userBean = getLoginUser();
		
		castleUser = CastleUtil.getCastleUser(userBean.getId());
		if(castleUser == null)
			return;
		if(castleUser.getCur() == 0) {
			castle = CastleUtil.getCastleByUid(userBean.getId());
			castleUser.setCur(castle.getId());
			long now = System.currentTimeMillis();
			if(castleUser.getLockTime() - now < lockJudgeTime) {
				castleUser.setLockTime(now + lockTime);
				SqlUtil.executeUpdate("update castle_user set lock_time=date_add(now(),interval 7 day) where uid=" + castleUser.getUid(), 5);
			}
		} else {
			castle = CastleUtil.getCastleById(castleUser.getCur());
		}
		userResBean = CastleUtil.getUserResBeanById(castle.getId());
	}
	
	public String getTop() {
		long now = System.currentTimeMillis();
		
		StringBuilder sb = new StringBuilder("资源信息:粮");
		sb.append(this.userResBean.getGrain(now));
		sb.append("|铁");
		sb.append(this.userResBean.getFe(now));
		sb.append("|木");
		sb.append(this.userResBean.getWood(now));
		sb.append("|石");
		sb.append(this.userResBean.getStone(now));
		
		return sb.toString();
	}

	public CastleService getCastleService() {
		return castleService;
	}
	public CastleService getService() {
		return castleService;
	}

	public void setCastleService(CastleService castleServic) {
		castleService = castleServic;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public UserResBean getUserResBean() {
		return userResBean;
	}

	public void setUserResBean(UserResBean userResBean) {
		this.userResBean = userResBean;
	}
	public boolean isMainCastle() {
		return castleUser.getMain() == castle.getId();
	}
	// 如果返回false，表示这个建筑不会出现在建造列表
	public boolean canBuild(BuildingTBean bt) {
		byte[] bs = userResBean.getBuildings();
		int grade = bs[bt.getBuildType()];
		if(grade != 0) {
			if(!bt.isFlagRebuild() || bt.getMaxGrade()>grade)
				return false;
		} else {
			if(bt.getRace()!=0&&bt.getRace()!=castle.getRace())
				return false;
			if(bt.isFlagMain()) {
				if(!isMainCastle())
					return false;
			} else if(bt.isFlagNotMain()){
				if(isMainCastle())
					return false;
			}
		}
		if(bt.isFlagUnique()&&castleUser.isFlagPalace())		//  皇宫是唯一的
			return false;
		
		if(bt.isFlagArt()) {		// 如果拥有神器，允许建造
			if(CastleUtil.getActiveArtType(castle) == 7)
				return true;
		}
		
		if(bt.isFlagNatar()) {
			if(!castle.isNatar())
				return false;
		} else if(bt.isFlagNotNatar()){
			if(castle.isNatar())
				return false;
		}
		return true;
	}
}
