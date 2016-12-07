package net.joycool.wap.spec.castle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;


public class CastleAction extends CastleBaseAction {	
	static CacheService cacheService = CacheService.getInstance();
	public static final int AROUND = 3;
	
	public CastleAction() {
		super();
	}

	public CastleAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public CastleAction(HttpServletRequest request) {
		super(request);
	}
	
	//初始化个人的城堡
	public CastleBean start(){
		CastleUserBean user = CastleUtil.getCastleUser(userBean.getId());	
		CastleBean bean = null;		
		//如果是新用户，则初始化城堡数据
		if(user == null) {
			int race = getParameterInt("race");		// 选择种族
			if(race == 5)	// 随机选择
				race = RandomUtil.nextInt(3) + 1;
			if(race <= 0 || race > 3)
				return null;
			int field = getParameterInt("field");	// 选择区域
			if(field == 5)	// 随机选择
				field = RandomUtil.nextInt(4) + 1;
			if(field <= 0 || field > 4)
				return null;
			if(CastleUtil.count > 100000) {
				return null;
			}
			long now = System.currentTimeMillis();
			user = new CastleUserBean(userBean.getId());
			user.setName(userBean.getNickName());
			user.setCreateTime(now);
			user.setLockTime(now + lockTime);
			user.setRace(race);
			user.setCivilTime(now);
			user.setCivilSpeed(2);
			
			castleService.addCastleUser(user);
			CacheManage.castleUser.spt(new Integer(userBean.getId()), user);
			
			bean = CastleUtil.produceCastle(userBean.getId(), race, userBean.getNickName(), field);
			user.setMain(bean.getId());
			SqlUtil.executeUpdate("update castle_user set main=" + user.getMain()
					+ " where uid=" + user.getUid(), 5);

			userResBean = new UserResBean(userBean.getId(), bean.getType2());
			userResBean.setId(bean.getId());
			
			castleUser = user;
			
			castleService.addUserRes(userResBean);
			CacheManage.castleUserRes.spt(new Integer(userResBean.getId()), userResBean);
			//初始化所有建筑
			BuildingBean city = new BuildingBean(ResNeed.CITY_BUILD, 1, bean.getId(), 1,19);//19为主城的位置pos
			castleService.addBuilding(city);
		} else {
			if(user.getCur() == 0)
				user.setCur(user.getMain());

			bean = CastleUtil.getCastleById(user.getCur());
		}
		return bean;
	}
	
	//查找坐标为x,y的某个城镇
	public boolean search(){
		int x, y;
		int pos = getParameterIntS("pos");
		if(pos == -1) {
			x = getParameterIntS("x");
			y = getParameterIntS("y");
		} else {
			x = CastleUtil.pos2X(pos);
			y = CastleUtil.pos2Y(pos);
		}
		int cid = getParameterInt("cid");
		if(cid > 0) {
			
		} else {
			if(x < 0 || y < 0 || x >= CastleUtil.mapSize || y >= CastleUtil.mapSize) {
				request.setAttribute("msg", "输入坐标不正确");
				return false;
			}
			
			cid = CastleUtil.getMapCastleId(x, y);
	
			if(cid == 0)
				return false;
		}
		
		CastleBean castleBean = CastleUtil.getCastleById(cid);
		if(castleBean == null)
			return false;
		request.setAttribute("castleBean", castleBean);
		return true;
	}
	
	//获取周围玩家
	public boolean getAround() {
		int x = this.getParameterInt("x");
		int y = this.getParameterInt("y");
		
		if(x < 0 || y < 0 || x >= CastleUtil.mapSize || y >= CastleUtil.mapSize) {
			request.setAttribute("msg", "输入坐标不正确");
			return false;
		}
		if(!hasParam("x")) {		// 没有输入坐标，查询自己周围
			x = castle.getX();
			y = castle.getY();
		}
		setAttribute("x", x);
		setAttribute("y", y);
		int x1 = x > AROUND? x - AROUND : 0;
		int x2 = x < CastleUtil.mapSize - 1 - AROUND ? x + AROUND : CastleUtil.mapSize - 1 - AROUND;
		
		int y1 = y > AROUND? y - AROUND : 0;
		int y2 = y < CastleUtil.mapSize - 1 - AROUND ? y + AROUND : CastleUtil.mapSize - 1 - AROUND;
		
		int[][] map = CastleUtil.getMap();
		List list = new ArrayList();
		
		for(int i = x1; i <= x2; i++) {
			for(int j = y1; j <= y2; j++) {
				if(map[i][j] != 0) {
//					if(!(i == x && j == y)) {
						list.add(new Integer(map[i][j]));
//					}
				}
					
			}
		}
		request.setAttribute("list", list);
		return true;
	}
	// 召回、回派，返回false表示没有成功，返回true表示执行成功
	public boolean move() {
		int type = getParameterInt("t");
		int id = getParameterInt("id");
		if(id == 0)
			return false;
		CastleArmyBean army;
		int cid = castle.getId();
		CastleBean to;
		int[] count;
		int sum = 0, people = 0;
		
		synchronized(castle) {
			army = castleService.getCastleArmyById(id);
			if(army == null)
				return false;
			
			if(army.getCid() != cid && army.getAt() != cid)	// 不能回派或者召回的部队
				return false;
		
		
			if(CastleUtil.getUserResBeanById(army.getAt()).getGrain(System.currentTimeMillis()) == 0) {	// 判断是否有饿死，如果有，处理后重新载入数据
				army = castleService.getCastleArmyById(id);
				if(army == null)
					return false;
			}
			
			setAttribute("army", army);
			
			if(army.getCid() == army.getAt() || type == 0 || army.getCid() == 0)	// 进入选择兵力，自然界的不允许回派
				return false;
			to = CastleUtil.getCastleById(army.getCid());
			SoldierResBean[] ss = ResNeed.getSoldierTs(to.getRace());
			
			if(type == 1) {
				count = new int[ResNeed.soldierTypeCount + 1];
				for(int i = 1;i < count.length;i++) {
					count[i] = getParameterInt("count" + i);
					if(count[i] < 0) {
						return false;
					}
					if(count[i] != 0) {
						sum += count[i];
						people += count[i] * ss[i].getPeople();
					}
				}
				count[0] = getParameterInt("countH");
				if(count[0] != 0 && count[0] <= army.getHero()) {
					people += count[0] * ResNeed.heroGrainCost;
					sum++;
				}
				if(sum == 0)
					return false;
				if(!army.decrease(count)) {
					return false;
				}
				if(army.isEmpty()) {
					request.setAttribute("all", "");		// 表示所有军队都撤离
					castleService.deleteCastleArmyById(army.getId());
				} else
					castleService.updateSoldierCount(army);
			} else {
				count = army.getCount();
				for(int i = 1;i < count.length;i++) {
					if(count[i] != 0) {
						sum += count[i];
						people += count[i] * ss[i].getPeople();
					}
				}
				count[0] = army.getHero();
				if(count[0] != 0)
					people += count[0] * ResNeed.heroGrainCost;
				request.setAttribute("all", "");		// 表示所有军队都撤离
				castleService.deleteCastleArmyById(army.getId());
			}
		}
		
		int speedAdd;
		if(army.getCid() == cid)
			speedAdd = userResBean.getSpeedAdd();
		else
			speedAdd = CastleUtil.getUserResBeanById(army.getCid()).getSpeedAdd();
		
		
		CastleBean from = CastleUtil.getCastleById(army.getAt());
		float heroSpeed = 100f;	// 默认20
		if(count[0] != 0) {
			HeroBean hero = CastleUtil.getCastleUser(to.getUid()).getHero();
			if(hero != null)
				heroSpeed = hero.getHeroSoldier().getSpeed();
		}
		AttackThreadBean attackThreadBean = new AttackThreadBean(count,
				from.getId(), from.getX(), from.getY(), to,
				to.getId(), to.getX(), to.getY(), speedAdd, heroSpeed);		
		attackThreadBean.setCid(army.getCid());
		attackThreadBean.setType(5);	// 移动指令都属于支援
		cacheService.addCacheAttack(attackThreadBean);
		
//		 军队召回，军队一定不在自己的城堡，那么粮食消耗就会改变，支援方的粮食消耗相应减少
		if(people != 0) {
			CastleUtil.decreaseUserRes(army.getCid(), 0, 0, 0, 0, people);
			CastleUtil.decreaseUserRes(army.getAt(), 0, 0, 0, 0, -people);
		}
		
		tip("tip", "指令执行成功");
		return true;
	}
	// 绿洲的召回、回派，返回false表示没有成功，返回true表示执行成功
	public boolean move2() {
		
		int id = getParameterInt("id");
		if(id == 0)
			return false;
		
		int type = getParameterInt("t");
		
		CastleArmyBean army;
		OasisBean oasis;
		CastleBean to;
		CastleBean atCastle;
		int[] count;
		int sum = 0, people = 0, cid;
		synchronized(castle) {
		
			army = castleService.getOasisArmyById(id);
			if(army == null)
				return false;
			oasis = CastleUtil.getOasisById(army.getAt());
			atCastle = CastleUtil.getCastleById(oasis.getCid());	// 军队所在绿洲的所属
			cid = castle.getId();
			if(army.getCid() != cid && atCastle.getId() != cid || atCastle == null)	// 不能回派或者召回的部队
				return false;
			
			if(CastleUtil.getUserResBeanById(atCastle.getId()).getGrain(System.currentTimeMillis()) == 0) {	// 判断是否有饿死，如果有，处理后重新载入数据
				army = castleService.getCastleArmyById(id);
				if(army == null)
					return false;
			}
			
			setAttribute("army", army);
			
			if(type == 0)	// 进入选择兵力，自然界的不允许回派
				return false;

			to = CastleUtil.getCastleById(army.getCid());
			SoldierResBean[] ss = ResNeed.getSoldierTs(to.getRace());
			if(type == 1) {
				count = new int[ResNeed.soldierTypeCount + 1];
				for(int i = 1;i < count.length;i++) {
					count[i] = getParameterInt("count" + i);
					if(count[i] < 0) {
						return false;
					}
					if(count[i] != 0) {
						sum += count[i];
						people += count[i] * ss[i].getPeople();
					}
				}
				count[0] = getParameterInt("countH");
				if(count[0] != 0 && count[0] <= army.getHero()) {
					people += count[0] * ResNeed.heroGrainCost;
					sum++;
				}
				if(sum == 0)
					return false;
				if(!army.decrease(count)) {
					return false;
				}
				if(army.isEmpty()) {
					request.setAttribute("all", "");		// 表示所有军队都撤离
					castleService.deleteOasisArmyById(army.getId());
				} else
					castleService.updateOasisSoldierCount(army);
			} else {
				count = army.getCount();
				for(int i = 1;i < count.length;i++) {
					if(count[i] != 0) {
						sum += count[i];
						people += count[i] * ss[i].getPeople();
					}
				}
				
				count[0] = army.getHero();
				if(count[0] != 0)
					people += count[0] * ResNeed.heroGrainCost;
				request.setAttribute("all", "");		// 表示所有军队都撤离
				castleService.deleteOasisArmyById(army.getId());
			}
		}

		int speedAdd;
		if(army.getCid() == cid)
			speedAdd = userResBean.getSpeedAdd();
		else
			speedAdd = CastleUtil.getUserResBeanById(army.getCid()).getSpeedAdd();
		
		
		CastleBean from = CastleUtil.getCastleById(atCastle.getId());	// 绿洲拥有者的城堡
		float heroSpeed = 100f;	// 默认20
		if(count[0] != 0) {
			HeroBean hero = CastleUtil.getCastleUser(to.getUid()).getHero();
			if(hero != null)
				heroSpeed = hero.getHeroSoldier().getSpeed();
		}
		AttackThreadBean attackThreadBean = new AttackThreadBean(count,
				from.getId(), oasis.getX(), oasis.getY(), to,
				to.getId(), to.getX(), to.getY(), speedAdd, heroSpeed);		
		attackThreadBean.setCid(army.getCid());
		attackThreadBean.setType(5);	// 移动指令都属于支援
		cacheService.addCacheAttack(attackThreadBean);
		
//		 除非是城堡支援自己拥有的绿洲，否则需要改变城堡拥有者的人口和军队拥有者的人口
		if(people != 0 && atCastle.getId() != army.getCid()) {
			CastleUtil.decreaseUserRes(army.getCid(), 0, 0, 0, 0, people);
			CastleUtil.decreaseUserRes(atCastle.getId(), 0, 0, 0, 0, -people);
		}
		
		tip("tip", "指令执行成功");
		return true;
	}
	//出兵前往坐标为x,y的城堡
	public void attack(){
		int x = this.getParameterInt("x");
		int y = this.getParameterInt("y");
		int type = getParameterInt("t");

		if(type < 0 || type > 5) {	// 0 1 2表示攻击，除此以外只有支援5
			type = 5;
		}

		int cid = CastleUtil.getMapCastleId(x, y);

		int[] count = new int[ResNeed.soldierTypeCount + 1];
		int sum = 0;
		if(type == 4) {	// 拓荒
			if(cid != 0) {
				request.setAttribute("msg", "该位置已有城堡存在");
				return;
			}
			count[9] = 3;
			sum = 3;
		} else {
			if(cid == 0 || cid == castle.getId()) {
				request.setAttribute("msg", "不能攻击自己");
				return;
			}
			if(type != 2 && type != 3) {		// 非侦察
				for(int i = 1;i < count.length;i++) {
					count[i] = getParameterInt("count" + i);
					if(count[i] < 0) {
						request.setAttribute("msg", "士兵数不能小于0");
						return;
					}
					sum += count[i];
				}
				count[0] = getParameterInt("countH");
				if(count[0] > 0) {
					sum += count[0];
				}
			} else {		//  侦察
				count[6] = getParameterInt("count6");
				if(count[6] < 0) {
					request.setAttribute("msg", "士兵数不能小于0");
					return;
				}
				sum = count[6];
			}
		}
		if(sum == 0) {
			request.setAttribute("msg", "士兵数不能为0");
			return;
		}

		CastleBean castleBean = null;
		long now = System.currentTimeMillis();
		if(cid != 0) {
			castleBean = CastleUtil.getCastleById(cid);
			if(castleBean == null || castleBean.isLock()) {
				request.setAttribute("msg", "暂时无法攻打");
				return;
			}
			CastleUserBean castleUser2 = CastleUtil.getCastleUser(castleBean.getUid());
			if(castleUser.getProtectTime() > now) {
				request.setAttribute("msg", "你的保护期到" + DateUtil.formatDate2(castleUser.getProtectTime()));
				return;
			}	
			if(castleUser2.getProtectTime() > now) {
				request.setAttribute("msg", "该玩家保护期到" + DateUtil.formatDate2(castleUser2.getProtectTime()));
				return;
			}	
			if(castleUser2.getLockTime() < now) {
				request.setAttribute("msg", "该玩家帐号已冻结");
				return;
			}	
		} else {
			int mapType = CastleUtil.getMapType(x, y);
			if(mapType <= 0 || mapType > 16) {
				request.setAttribute("msg", "目标位置无法建立城堡");
				return;
			}
			if(!castleUser.canCreate(now)) {
				request.setAttribute("msg", "文明度不足");
				return;
			}
			if(!castle.canExpand(userResBean.getBuildingGrade(ResNeed.PALACE_BUILD), userResBean.getBuildingGrade(ResNeed.PALACE2_BUILD))) {
				request.setAttribute("msg", "行宫等级不足");
				return;
			}
		}
		
		synchronized(castle) {
			CastleArmyBean army = CastleUtil.getCastleArmy(castle.getId());
			
			//判断出兵是否足够的兵，如果够就直接扣除
			if(!army.decrease(count)) {
				request.setAttribute("msg", "士兵的数量不够");
				return;
			}
			castleService.updateSoldierCount(army);
		}
		float heroSpeed = 20f;	// 默认20
		if(count[0] != 0) {
			HeroBean hero = castleUser.getHero();
			if(hero != null)
				heroSpeed = hero.getHeroSoldier().getSpeed();
		}
		AttackThreadBean attackThreadBean = new AttackThreadBean(count,
				castle.getId(), castle.getX(), castle.getY(), castle,
				cid, x, y, userResBean.getSpeedAdd(), heroSpeed);		
		attackThreadBean.setCid(castle.getId());
		attackThreadBean.setType(type);
		if(count[8] != 0) {		// 如果携带投石车，选择攻击
			int opt = getParameterInt("opt");
			if(opt != 0) {
				// 判断集结点是否够等级，然后判断对方是否有神器
				int gatherGrade = userResBean.getBuildingGrade(ResNeed.GATHER_BUILD);
				if(opt <= 0 || opt >= ResNeed.buildingTypeCount || ResNeed.gatherOpt[opt] > gatherGrade)
					opt = 0;
/*				else if(opt != 0 && opt != ResNeed.TREASURE_BUILD) {		// 留到攻击到达的时候判断
					int art = CastleUtil.getActiveArtType(castleBean);
					if(art == 8 || art == 18)
						opt = 0;
				}*/
			}
			attackThreadBean.setOpt(opt);
		}
		cacheService.addCacheAttack(attackThreadBean);
		request.setAttribute("attack", attackThreadBean);
		
		
		if(type != 5) {
			if(cid != 0) {
				StringBuilder sb1 = new StringBuilder("我带");
				sb1.append(sum);
				sb1.append("士兵向");
				addMsg(sb1.toString(), "进发",castleBean, castle.getUid());
				if(castleBean.getUid() != castle.getUid() && type != 2 && type != 3) {	// 如果是侦察就看不到
					StringBuilder sb2 = new StringBuilder("的城堡有");
					sb2.append(sum);
					sb2.append("士兵向");
					sb2.append(castleBean.getCastleNameWml());
					sb2.append("进发");
					addMsg("", sb2.toString(),castle, castleBean.getUid());
				}
				
//				request.setAttribute("msg", "已发兵至"+x+"|"+y+"的城堡");
			} else {
				userResBean.decreaseRes(750, 750, 750, 750);
//				request.setAttribute("msg", "已向"+x+"|"+y+"的空地进发");
			}
		} else {
//			request.setAttribute("msg", "已发兵至"+x+"|"+y+"的城堡");
		}
	}
	//出兵前往坐标为x,y的绿洲
	public void attack2(){
		int x = this.getParameterInt("x");
		int y = this.getParameterInt("y");
		int type = getParameterInt("t");

		if(type < 7 || type > 10) {	// 对绿洲，7 8 9表示攻击，除此以外只有支援10
			type = 10;
		}

		if(CastleUtil.getMapCastleId(x, y) != 0 || CastleUtil.getMapType(x, y) <= 16) {
			request.setAttribute("msg", "无法出兵");
			return;
		}
		OasisBean oasis = CastleUtil.getOasisByXY(x, y);
		int cid = oasis.getCid();
		if(cid == 0) {
			if(type == 10 || type == 7)
				type = 8;
		}

		int[] count = new int[ResNeed.soldierTypeCount + 1];
		int sum = 0;

		if(type != 9) {		// 非侦察
			for(int i = 1;i < count.length;i++) {
				count[i] = getParameterInt("count" + i);
				if(count[i] < 0) {
					request.setAttribute("msg", "士兵数不能小于0");
					return;
				}
				sum += count[i];
			}
			count[0] = getParameterInt("countH");
			if(count[0] > 0) {
				sum++;
			}
		} else {		//  侦察
			count[6] = getParameterInt("count6");
			if(count[6] < 0) {
				request.setAttribute("msg", "士兵数不能小于0");
				return;
			}
			sum = count[6];
		}

		if(sum == 0) {
			request.setAttribute("msg", "士兵数不能为0");
			return;
		}

		CastleBean castleBean = null;
		long now = System.currentTimeMillis();

		if(cid != 0) {
			castleBean = CastleUtil.getCastleById(cid);
			CastleUserBean castleUser2 = CastleUtil.getCastleUser(castleBean.getUid());
			if(castleUser.getProtectTime() > now) {
				request.setAttribute("msg", "你的保护期到" + DateUtil.formatDate2(castleUser.getProtectTime()));
				return;
			}	
			if(castleUser2.getProtectTime() > now) {
				request.setAttribute("msg", "该玩家保护期到" + DateUtil.formatDate2(castleUser2.getProtectTime()));
				return;
			}	
//			if(castleUser2.getLockTime() < now) {
//				request.setAttribute("msg", "该玩家帐号已冻结");
//				return;
//			}	
		}
		synchronized(castle) {
			CastleArmyBean army = CastleUtil.getCastleArmy(castle.getId());
			
			//判断出兵是否足够的兵，如果够就直接扣除
			if(!army.decrease(count)) {
				request.setAttribute("msg", "士兵的数量不够");
				return;
			}
			castleService.updateSoldierCount(army);
		}
		float heroSpeed = 20f;	// 默认20
		if(count[0] != 0) {
			HeroBean hero = castleUser.getHero();
			if(hero != null)
				heroSpeed = hero.getHeroSoldier().getSpeed();
		}
		AttackThreadBean attackThreadBean = new AttackThreadBean(count,
				castle.getId(), castle.getX(), castle.getY(), castle,
				cid, x, y, userResBean.getSpeedAdd(), heroSpeed);		
		attackThreadBean.setCid(castle.getId());
		attackThreadBean.setType(type);

		cacheService.addCacheAttack(attackThreadBean);
		request.setAttribute("attack", attackThreadBean);

		
		if(type != 10) {
			StringBuilder sb1 = new StringBuilder(32);
			sb1.append("我带");
			sb1.append(sum);
			sb1.append("士兵向");
			sb1.append(x);
			sb1.append('|');
			sb1.append(y);
			sb1.append("的绿洲进发");
			addMsg(sb1.toString(), castle.getUid());
			if(castleBean != null && castleBean.getUid() != castle.getUid() && type != 9) {		// 如果是侦察就看不到
				StringBuilder sb2 = new StringBuilder(32);
				sb2.append(castle.getX());
				sb2.append('|');
				sb2.append(castle.getY());
				sb2.append("的城堡有");
				sb2.append(sum);
				sb2.append("士兵向我的绿洲");
				sb2.append(x);
				sb2.append('|');
				sb2.append(y);
				sb2.append("进发");
				addMsg("坐标为" + sb2.toString(), castleBean.getUid());
			}
		}
//		request.setAttribute("msg", "已发兵至"+x+"|"+y+"的绿洲");
	}
	// 取消进攻，出发后90秒内才可以取消
	public boolean cancelAttack() {
		int id = getParameterInt("id");
		AttackThreadBean bean;
		synchronized(AttackThread.class) {
			bean = cacheService.getCacheAttack(id);
			
			if(bean == null || bean.getFromCid() != castle.getId() || bean.getX() == castle.getX() && bean.getY() == castle.getY()
					|| System.currentTimeMillis() - bean.getStartTime() > 90000) {
				request.setAttribute("msg", "军队行动取消失败");
				return false;
			}
			long now = System.currentTimeMillis();
			bean.setEndTime(now + now - bean.getStartTime());
			cacheService.cancelAttackThreadBean(bean);
		}
		
		request.setAttribute("msg", "行动取消,军队正在中途返回");
		return true;
	}
	
	public boolean celebrate() {
		int type = getParameterInt("type");
		if(type != 1)
			type = 2;

		int grade = userResBean.getBuildingGrade(27);	// 广场等级
		if(grade < 10 && type == 2) {
			request.setAttribute("msg", "建筑等级不够");
			return false;
		}
		ResTBean res;
		int addCivil;		// 增加的文明度
		if(type == 1) {
			addCivil = 500;
			res = ResNeed.celeRes;
		} else {
			addCivil = 2000;
			res = ResNeed.cele2Res;
		}
		if(cacheService.containCommon(castle.getId(), 1)) {
			request.setAttribute("msg", "已有活动在举行中");
			return true;
		}
		
		// 最后一步才能扣资源，其他判断必须在之前
		if(!userResBean.decreaseRes(res.getWood(), res.getStone(), res.getFe(), res.getGrain())) {
			request.setAttribute("msg", "资源不足");
			return false;
		}
		
		int time = ResNeed.getGradeTime(grade, res.getTime());// 拆除时间是建造的1/4
//		time = 10;
		CommonThreadBean commonThreadBean = new CommonThreadBean(userBean.getId(), castle.getId(), 1, time, addCivil);
		cacheService.addCacheCommon(commonThreadBean);
		if(type == 1)
			request.setAttribute("msg", "小型活动开始举行");
		else
			request.setAttribute("msg", "大型活动开始举行");
		return true;
	}
	
	public boolean account() {
		if(hasParam("del")) {
			if(cacheService.containCommon2(userBean.getId(), 3)) {
				return false;
			}
			
			String pwd = request.getParameter("pwd");
			
			// 获取用户设置信息
			UserSettingBean userSetting = this.getLoginUser().getUserSetting();
			
			if(userSetting != null && userSetting.getBankPw() != null ) {
				if(pwd == null || !pwd.equals(userSetting.getBankPw())) {
					request.setAttribute("msg", "密码不正确");
					return false;
				}
			}
			
			CastleUserBean castleUserBean = CastleUtil.getCastleUser(getLoginUser().getId());
			
			if(castleUserBean.getTong() != 0) {
				TongBean tongBean = CastleUtil.getTong(castleUserBean.getTong());
				if(getLoginUser().getId() == tongBean.getUid()) {
					request.setAttribute("msg", "您是盟主不能删除账号");
					return false;
				}
			}
			
			CommonThreadBean commonThreadBean = new CommonThreadBean(userBean.getId(), castle.getId(), 3, 3 * 24 * 3600, 0);
			cacheService.addCacheCommon(commonThreadBean);
			
			request.setAttribute("msg", "帐号将在3天内删除");
			return true;
		}
		if(hasParam("cdel")) {
			int id = getParameterInt("cdel");
			if(id == 0)
				return false;
			CommonThreadBean bean = cacheService.getCacheCommon(id);
			
			if(bean == null) {
				return false;
			}
			
			if(bean.getUid() != userBean.getId())
				return false;
			cacheService.deleteCacheCommon(id);
			request.setAttribute("msg", "帐号删除被取消");
			return true;
		}
		return false;
	}
	
	// 迁都设置主城
	public void setMain() {
		if(castle.getId() == castleUser.getMain() || castle.isNatar()) {	// 不需要提示，如果不能迁都应该看不到连接
			tip("tip", "设定失败");
			return;
		}
		if(userResBean.getBuildingGrade(ResNeed.PALACE2_BUILD) <= 0) {
			tip("tip", "设定失败");
			return;
		}
		CastleUtil.setMain(castleUser, castle);
		tip("tip", "新主城设定成功");
	}
	
	protected static boolean addMsg(String str1,String str2,CastleBean castle, int uid) {
		StringBuilder receiveContent = new StringBuilder(str1);
		receiveContent.append(castle.getX());
		receiveContent.append("|");
		receiveContent.append(castle.getY());
		receiveContent.append(str2);
		CastleMessage msg = new CastleMessage(receiveContent.toString(), uid);
		return castleService.addCastleMessage(msg);
	}
	protected static boolean addMsg(String str, int uid) {
		CastleMessage msg = new CastleMessage(str, uid);
		return castleService.addCastleMessage(msg);
	}
	// 任务完成检查、进入下一个任务等等
	public void quest() {
		CastleUserBean user = getCastleUser();
		synchronized(user) {
			if(hasParam("next")) {
				if(user.getQuestStatus() == 1) {
					user.setQuestStatus(0);
					user.setQuest(user.getQuest() + 1);
					SqlUtil.executeUpdate("update castle_user set quest_status=0,quest=quest+1 where uid=" + user.getUid(), 5);
				}
			} else if(user.getQuest() < ResNeed.getQuestCount()) {
				CastleQuestBean quest = ResNeed.getQuest(user.getQuest());
				if(user.getQuestStatus() == 0) {
					if(checkQuest(user, quest)) {
						if(quest.getWood() > 0)
							userResBean.increaseRes(quest.getWood(), quest.getStone(), quest.getFe(), quest.getGrain());
						user.setQuestStatus(1);
						if(quest.getSp() > 0) {
							CastleUtil.updateSPAccount(user, 0, quest.getSp());
						}
						if(quest.getGold() == 0) {
							SqlUtil.executeUpdate("update castle_user set quest_status=1 where uid=" + user.getUid(), 5);
						} else {
							user.setGold(user.getGold() + quest.getGold());
							SqlUtil.executeUpdate("update castle_user set quest_status=1,gold=" + user.getGold() + " where uid=" + user.getUid(), 5);
						}
					}
					
				}
			}
		}
	}
	public boolean checkQuest(CastleUserBean user, CastleQuestBean quest) {
		
		CastleService castleService = CastleService.getInstance();
		switch(quest.getType()) {
		case 0:	// 第一个任务只是开始，直接完成即可
			return true;
		case 1: {		// 
			if(userResBean.getBuildingGrade(ResNeed.WOOD_BUILD) > 0) {
				tip("tip", "是的，这样你就可以得到更多木头了。");
				return true;
			}
			synchronized(BuildingThread.class) {
				List list = cacheService.getCacheBuildingByCid(castle.getId());
				for(int i = 0; i < list.size(); i++) {	//新建一个伐木场
					BuildingThreadBean bean = (BuildingThreadBean)list.get(i);
					if(bean.getType() == ResNeed.WOOD_BUILD) {
						bean.startBuild();
						return true;
					}
				}
			}
		} break;
		case 2: {		// 是否建造了某个建筑，达到级别value2
			if(userResBean.getBuildingGrade(quest.getValue()) >= quest.getValue2())
				return true;
		} break;
		case 3: {		// 村庄的名字
			if(!castle.getCastleName().equals("新的城堡"))
				return true;
		} break;
		case 4: {		// 其他玩家
			int rank = castleService.getCastleCurArray(user.getUid());
			int input = this.getParameterInt("r");
			if(input != 0) {
				if(input < rank)
					tip("tip", "不管怎么说，如果你能坚持不懈地努力，总有一天你能达到这个等级！这里是一些为你准备的资源，继续努力吧。");
				else if(input > rank)
					tip("tip", "你已经超过了这个等级！你目前的排名为" + rank + "。这里是一些资源，希望可以帮助你更上一层楼。");
				return true;
			}
		} break;
		case 5: {		// 建筑建造队列
			if(userResBean.getBuildingGrade(ResNeed.FE_BUILD) > 0 && 
					userResBean.getBuildingGrade(ResNeed.STONE_BUILD) > 0)
				return true;

		} break;
		case 6: {		// 输入粮食产量
			int q = this.getParameterIntS("q");
			if(q != -1) {
				UserResBean us = this.getUserResBean();
				int real = us.getGrainRealSpeed2();
				if(real == q) {
					return true;
				} else if(real > q) {
					tip("tip", "不，你现在的粮食产量可不止这些。");
				} else {
					tip("tip", "不，你现在的粮食产量并没有那么多。");
				}
			}
		} break;
		case 7: {		// 邻居们
			int x = this.getParameterIntS("x");
			int y = this.getParameterIntS("y");
			
			if(x < 0 || y < 0 || x >= CastleUtil.mapSize || y >= CastleUtil.mapSize) {
				return false;
			}
			int cid = CastleUtil.getMapCastleId(x, y); 
			if(cid == 0) {
				tip("tip", "不对，那不是正确的坐标。再试一次吧！");
				return false;
			}
			if(cid != castle.getId()) {
				return true;
			}
			tip("tip", "不对，那是你自己的坐标。再试一次吧！");
		} break;
		case 8: {		// 大型军团
			if(hasParam("c")) {	// 确认送出
				userResBean.reCalc(System.currentTimeMillis());
				int set = userResBean.getGrain() - 200;
				if(set < 0)
					userResBean.setGrain(0);
				else
					userResBean.setGrain(set);
				// 发出一个自然界的军队
				long now = System.currentTimeMillis();
				AttackThreadBean bean = new AttackThreadBean();		
				bean.setCid(0);
				bean.setFromCid(castle.getId());
				bean.setToCid(castle.getId());
				bean.setStartTime(now);
				bean.setEndTime(now + DateUtil.MS_IN_DAY / 2);
				bean.setType(5);
				bean.setSoldierCount("1");
				cacheService.addCacheAttack(bean);
				return true;
			}
		} break;
		case 9: {		// 每样一个！
			int[] count = castleService.getBuildingCountByType("cid=" + castle.getId() + " and grade>0");
			if(count[ResNeed.WOOD_BUILD] > 1 &&
					count[ResNeed.FE_BUILD] > 1 &&
					count[ResNeed.STONE_BUILD] > 1 &&
					count[ResNeed.GRAIN_BUILD] > 1)
				return true;
		} break;
		case 10: {		// 马上就到！
			if(SqlUtil.getIntResult("select id from cache_attack where cid=0 and to_cid="
					+ castle.getId(), 5) == -1)
				return true;
		} break;
		case 11: {	
			if(castleUser.getUnread() == 0)
				return true;
		} break;
		case 12: {		// 全部value级
			int[] count = castleService.getBuildingCountByType("cid=" + castle.getId() + " and grade>=" + quest.getValue());
			if(count[ResNeed.WOOD_BUILD] > 3 &&
					count[ResNeed.FE_BUILD] > 3 &&
					count[ResNeed.STONE_BUILD] > 3 &&
					count[ResNeed.GRAIN_BUILD] > 5)
				return true;
		} break;
		case 13: {		// 资料
			if(CastleUtil.getCastleUser(user.getUid()).getInfo().indexOf("[#0]") != -1)
				return true;
		} break;
		case 15: {		// 每个资源升级一个到2级
			if(userResBean.getBuildingGrade(ResNeed.FE_BUILD) > 1 && 
					userResBean.getBuildingGrade(ResNeed.STONE_BUILD) > 1 &&
					userResBean.getBuildingGrade(ResNeed.WOOD_BUILD) > 1 &&
					userResBean.getBuildingGrade(ResNeed.GRAIN_BUILD) > 1)
				return true;

		} break;
		case 16: {		// 输入兵营需要的木头
			int input = this.getParameterInt("r");
			if(input != 0) {
				if(input > quest.getValue())
					tip("tip", "不，兵营所需的资源并没有那么多。");
				else if(input < quest.getValue())
					tip("tip", "不，兵营所需的资源不止这些。");
				else
					return true;
			}

		} break;
		case 18: {		// 排名上升了
			int rank = castleService.getCastleCurArray(user.getUid());
			int input = this.getParameterInt("r");
			if(input != 0) {
				if(input < rank)
					tip("tip", "如果你坚持不断地苦练，你应该可以到达那个水准。再试一次吧！");
				else if(input > rank)
					tip("tip", "你的能力并不止如此！再试一次吧！");
				else
					return true;
			}
		} break;
		}
		return false;
	}
	
	//public static int SP_ACCOUNT_PRICE = 15;
	//public static int SP_ACCOUNT_TIME = 7;
	public static int[] shopPrice = {
		0,15,5,5,5,5,3,3,2,3
	};
	public static int[] shopTime = {
		0,7,7,7,7,7,7,7,0,0
	};
	public static int[] shopValue = {
		0,0,
		UserResBean.FLAG_WOOD,
		UserResBean.FLAG_STONE,
		UserResBean.FLAG_FE,
		UserResBean.FLAG_GRAIN,
		UserResBean.FLAG_ATTACK,
		UserResBean.FLAG_DEFENSE,
		0,0
	};
	public static String[] affect = {
		"",
		//城堡SP帐号
		"城堡SP帐号增加",
		//木头产量+25%(7天)5金币
		"木头产量增加百分之25",
		//石头产量+25%(7天)5金币
		"石头产量增加百分之25",
		//铁快产量+25%(7天)5金币
		"铁块产量增加百分之25",
		//粮食产量+25%(7天)5金币
		"粮食产量增加百分之25",
		//士兵攻击力+10%(7天)3金币
		"士兵攻击力增加百分之10",
		//士兵防御力+10%(7天)3金币
		"士兵防御力增加百分之10",
		"立即完成正在建造的建筑",
		//随意调配四种资源3金币
		"随意调配四种资源"
	};
	//商城购买
	public boolean shopBuy(){
		int id = this.getParameterIntS("id");
		CastleUserBean user = castleUser;
		if(this.hasParam("a")) {
			if(id <= 0) {
				request.setAttribute("msg", "购买不合法");
				return false;
			}
			if(user.getGold() < shopPrice[id]) {
				request.setAttribute("msg", "金币不足");
				return false;
			}
			switch(id) {
				case 1:{
					CastleUtil.updateSPAccount(user, shopPrice[id], shopTime[id]);
					
					request.setAttribute("msg", "购买成功");
				}break;
				case 2://wood
				case 3://stone
				case 4://fe
				case 5://grain
				case 6://attack
				case 7://defense
				{					
					synchronized(BuildingThread.class){
						CommonThreadBean bean = cacheService.getCacheCommon2(user.getUid(), 4, shopValue[id]);
						if(bean != null) {
							cacheService.updateCacheCommon(bean.getId(), DateUtil.MS_IN_DAY * shopTime[id]);
						} else {
							CommonThreadBean common = new CommonThreadBean(user.getUid(), castle.getId(), 4, 24 * 3600 * shopTime[id], shopValue[id]);
							cacheService.addCacheCommon(common);
						}
					}
					if(castleUser.getCastleCount() == 0)
						CastleUtil.updateUserResFlag(userResBean, shopValue[id]);
					else {
						List list = user.getCastleList();
						for(int i = 0;i < list.size();i++) {
							Integer iid = (Integer)list.get(i);
							UserResBean userResBean2 = CastleUtil.getUserResBeanById(iid.intValue());
							CastleUtil.updateUserResFlag(userResBean2, shopValue[id]);
						}
					}
					CastleUtil.updateGold(user, shopPrice[id]);
					
					request.setAttribute("msg", "购买成功");
				}break;
				case 8:{
					if(castle.isNatar()) {
						request.setAttribute("msg", "所在城堡无法使用该功能");
						return false;
					}
					synchronized(BuildingThread.class){
						List cacheList = cacheService.getCacheBuildingByCid(castle.getId());
						if(cacheList == null || cacheList.size() == 0) {
							request.setAttribute("msg", "当前没有建筑正在建造");
							return false;
						} else {
							int ok = 0;
							Iterator iterator = cacheList.iterator();
							while(iterator.hasNext()){
								BuildingThreadBean cacheBuildingBean = (BuildingThreadBean)iterator.next();
								if(cacheBuildingBean.getType() == ResNeed.PALACE_BUILD || cacheBuildingBean.getType() == ResNeed.PALACE2_BUILD)
									continue;	// 皇宫和行宫不可以速建
								cacheBuildingBean.startBuild();
								cacheService.deleteCacheBuilding(cacheBuildingBean.getId());
								ok++;
							}
							if(ok == 0) {
								request.setAttribute("msg", "没有可以完成的建造任务");
							} else {
								CastleUtil.updateGold(user, shopPrice[id]);
								request.setAttribute("msg", ok + "个建造任务已完成");
							}
						}
					}
					
				}break;
			}
		}
		SqlUtil.executeUpdate("insert into castle_gold set uid=" + user.getUid() + ",gold=-" + shopPrice[id] + ",type=" + id + ",create_time=now(),`left`=" + user.getGold(), 5);
		switch(id) {
			case 1: {
				long now = System.currentTimeMillis();
				if(user.isSpAccount(now)) {
					long left = user.getSpTime() - now;
					float leftDay = leftDay(left);
					request.setAttribute("sp", "你的SP账号还剩余"+leftDay+"天");
				} else {
					request.setAttribute("sp", "你还没有购买SP账号");
				}
			}break;
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			{
				CommonThreadBean bean = cacheService.getCacheCommon2(user.getUid(), 4, shopValue[id]);
				if(bean != null) {
					long now = System.currentTimeMillis();
					long left = bean.getEndTime() - now;
					float leftDay = leftDay(left);
					request.setAttribute("sp", affect[id] + "还剩余"+leftDay+"天");
				}
			}break;
		}
		return true;
	}
	
	private float leftDay(long left) {
		float leftDay = Math.round((float)left / DateUtil.MS_IN_DAY * 10) / 10f;
		if(leftDay == 0) leftDay = 0.1f;
		return leftDay;
	}
	
	
	
	public static String RES_SESSION_G = "castle_res_g";
	public static String RES_SESSION_P = "castle_res_p";
	
	public void arrange(){
		if(castle.isNatar()){
			request.setAttribute("msg", "所在城堡无法使用该功能");
			return;
		}
		if(this.hasParam("a")){
			String a = request.getParameter("a");
			if(a == null) return;
			if(a.equals("g")) {
				int r = this.getParameterInt("r1");
				int g = this.getParameterInt("g");
				if(r == 0) return;
				if(g <= 0) {
					request.setAttribute("msg", "输入不合法");
					return;
				}
				int[] resG = (int[])session.getAttribute(RES_SESSION_G);
				if(resG == null){ 
					resG = new int[5];
					session.setAttribute(RES_SESSION_G, resG);
				}
				long now = System.currentTimeMillis(); 
				switch(r) {
				case 1:{
					if(resG[r] + g > this.userResBean.getWood(now)){
						request.setAttribute("msg", "所需木不够");
						return;
					}
				}break;
				case 2:{
					if(resG[r] + g > this.userResBean.getStone(now)){
						request.setAttribute("msg", "所需石不够");
						return;
					}
				}break;
				case 3:{
					if(resG[r] + g > this.userResBean.getFe(now)){
						request.setAttribute("msg", "所需铁不够");
						return;
					}
				}break;
				case 4:{
					if(resG[r] + g > this.userResBean.getGrain(now)){
						request.setAttribute("msg", "所需粮不够");
						return;
					}
				}break;
				}
				resG[r] += g;
			} else if(a.equals("p")) {
				int r = this.getParameterInt("r2");
				int p = this.getParameterInt("p");
				if(r == 0) return;
				if(p <= 0) {
					request.setAttribute("msg", "输入不合法");
					return;
				}
				int[] resP = (int[])session.getAttribute(RES_SESSION_P);
				if(resP == null) {
					resP = new int[5];
					session.setAttribute(RES_SESSION_P, resP);
				}
				int[] resG = (int[])session.getAttribute(RES_SESSION_G);
				if(resG == null) {
					resG = new int[5];
					session.setAttribute(RES_SESSION_G, resG);
				}
				
				if(resG[1]+resG[2]+resG[3]+resG[4]-resP[1]-resP[2]-resP[3]-resP[4]-p < 0) {
					request.setAttribute("msg", "分配资源不够");
					return;
				}
				resP[r] += p;
			} else if(a.equals("s")) {
				CastleUserBean user = CastleUtil.getCastleUser(getLoginUser().getId());
				
				if(user.getGold() < shopPrice[9]) {
					request.setAttribute("msg", "金币不足");
					return;
				}
				
				int[] resP = (int[])session.getAttribute(RES_SESSION_P);
				if(resP == null) {
					request.setAttribute("msg", "请先提取资源");
					return;
				}
				int[] resG = (int[])session.getAttribute(RES_SESSION_G);
				if(resG == null) {
					request.setAttribute("msg", "请分配资源");
					return;
				}
				if(resG[1]+resG[2]+resG[3]+resG[4]-resP[1]-resP[2]-resP[3]-resP[4] > 0) {
					request.setAttribute("msg", "资源还未分配完成");
					return;
				}
				if(resG[1]+resG[2]+resG[3]+resG[4]-resP[1]-resP[2]-resP[3]-resP[4] < 0) {
					request.setAttribute("msg", "资源分配有错");
					return;
				}
				
				
				synchronized(this.userResBean) {
					userResBean.reCalc(System.currentTimeMillis());
					if(userResBean.getWood() < resG[1] && resG[1] != 0 ||
							userResBean.getFe() < resG[3] && resG[3] != 0 ||
							userResBean.getGrain() < resG[4] && resG[4] != 0 ||
							userResBean.getStone() < resG[2] && resG[2] != 0 ) {
						request.setAttribute("msg", "资源不足");
						return;
					}
					userResBean.setWood(userResBean.getWood() - resG[1]);
					userResBean.setFe(userResBean.getFe() - resG[3]);
					userResBean.setGrain(userResBean.getGrain() - resG[4]);
					userResBean.setStone(userResBean.getStone() - resG[2]);
					userResBean.setWood(userResBean.getWood() + resP[1]);
					userResBean.setFe(userResBean.getFe() + resP[3]);
					userResBean.setGrain(userResBean.getGrain() + resP[4]);
					userResBean.setStone(userResBean.getStone() + resP[2]);
					castleService.updateUserRes(userResBean);
				}
				CastleUtil.updateGold(user, shopPrice[9]);
				SqlUtil.executeUpdate("insert into castle_gold set uid=" + user.getUid() + ",gold=-" + shopPrice[9] + ",type=9,create_time=now(),`left`=" + user.getGold(), 5);
				session.removeAttribute(RES_SESSION_G);
				session.removeAttribute(RES_SESSION_P);
			}
		} else {
			session.removeAttribute(RES_SESSION_G);
			session.removeAttribute(RES_SESSION_P);
		}
	}
	public static int[] rewardItem2 = {182, 183, 184};	// 攻击排名前三
	public static int[] rewardItem3 = {185, 186, 187};	// 防御排名前三 
	public static int[] rewardItem4 = {191, 192, 193};	// 上周攻击排名前三
	public static int[] rewardItem5 = {194, 195, 196};	// 上周防御排名前三 
	public static int[] rewardItem1 = {188, 189, 190};	// 总排名前三 
	// 一周攻击排名、防御排名以及总排名前三，每人发一个道具
	public static void weekTop() {
		try {
			CastleUtil.statPeople();
			List ids1 = SqlUtil.getIntList("select uid from castle_stat order by id limit 3", 5);
			for (int i = 0; i < ids1.size(); i++) {
				Integer iid = (Integer) ids1.get(i);
				UserBagCacheUtil.addUserBagCache(iid.intValue(), rewardItem1[i]);
			}

			CastleUtil.stat3Attack();
			List ids2 = SqlUtil.getIntList("select uid from castle_stat3 order by id limit 3", 5);
			for (int i = 0; i < ids2.size(); i++) {
				Integer iid = (Integer) ids2.get(i);
				UserBagCacheUtil.addUserBagCache(iid.intValue(), rewardItem2[i]);
			}

			CastleUtil.stat4Defense();
			List ids3 = SqlUtil.getIntList("select uid from castle_stat4 order by id limit 3", 5);
			for (int i = 0; i < ids3.size(); i++) {
				Integer iid = (Integer) ids3.get(i);
				UserBagCacheUtil.addUserBagCache(iid.intValue(), rewardItem3[i]);
			}
			
			CastleUtil.stat3AttackW();
			List ids2w = SqlUtil.getIntList("select uid from castle_statw3 order by id limit 3", 5);
			for (int i = 0; i < ids2w.size(); i++) {
				Integer iid = (Integer) ids2w.get(i);
				UserBagCacheUtil.addUserBagCache(iid.intValue(), rewardItem4[i]);
			}

			CastleUtil.stat4DefenseW();
			List ids3w = SqlUtil.getIntList("select uid from castle_statw4 order by id limit 3", 5);
			for (int i = 0; i < ids3w.size(); i++) {
				Integer iid = (Integer) ids3w.get(i);
				UserBagCacheUtil.addUserBagCache(iid.intValue(), rewardItem5[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		try {
			CastleUtil.stat3AttackW();
			CastleUtil.stat4DefenseW();
			CastleUtil.stat5Rob();
			CastleUtil.stat5RobW();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		SqlUtil.executeUpdate("insert into castle_stat_week (type,rank,uid,name,point,time) (select 1,id,uid,name,people,curdate() from castle_stat order by id limit 10)", 5);
		SqlUtil.executeUpdate("insert into castle_stat_week (type,rank,uid,name,point,time) (select 2,id,uid,name,attack_total,curdate() from castle_stat3 order by id limit 10)", 5);
		SqlUtil.executeUpdate("insert into castle_stat_week (type,rank,uid,name,point,time) (select 3,id,uid,name,defense_total,curdate() from castle_stat4 order by id limit 10)", 5);
		SqlUtil.executeUpdate("insert into castle_stat_week (type,rank,uid,name,point,time) (select 4,id,uid,name,rob_total,curdate() from castle_stat5 order by id limit 10)", 5);
		SqlUtil.executeUpdate("insert into castle_stat_week (type,rank,uid,name,point,time) (select 5,id,uid,name,attack_total,curdate() from castle_statw3 order by id limit 10)", 5);
		SqlUtil.executeUpdate("insert into castle_stat_week (type,rank,uid,name,point,time) (select 6,id,uid,name,defense_total,curdate() from castle_statw4 order by id limit 10)", 5);
		SqlUtil.executeUpdate("insert into castle_stat_week (type,rank,uid,name,point,time) (select 7,id,uid,name,rob_total,curdate() from castle_statw5 order by id limit 10)", 5);
		SqlUtil.executeUpdate("insert into castle_stat_week (type,rank,uid,name,point,time) (select 8,id,tong_id,name,people,curdate() from castle_stat2 order by id limit 10)", 5);
		
		SqlUtil.executeUpdate("update castle_user set attack_week=0,defense_week=0,rob_week=0", 5);
		CacheManage.castleUser.clear();
	}
	// 每天4点调用
	public static void dayTask(int weekday) {
		if (weekday == 1)
			weekTop();
		// 更新奇异宝物，随机变成一个新的
		List artList = CastleUtil.getArtList();
		if(artList != null) {
			for(int i = 0;i < artList.size();i++) {
				ArtBean art = (ArtBean)artList.get(i);
				if(art.isFlagChange())
					CastleUtil.changeArt(art);
			}
		}
	}
}
