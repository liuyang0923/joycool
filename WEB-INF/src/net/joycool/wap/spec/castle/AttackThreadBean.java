package net.joycool.wap.spec.castle;

import java.util.*;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class AttackThreadBean {
	public static String[] typeNames = {"攻击", "抢夺", "侦察", "侦察", "扩张", "支援", "", "攻击", "抢夺", "侦察", "支援"};
	int id;
	int cid;		// 属于哪个城堡
	int x, y;		// 目标
	int fromCid;	// 如果是返回，表示军队从fromCid返回到cid=toCid
	int toCid;
	long startTime;	//发起攻击的时间
	long endTime;	//结束时间
	String soldierCount;
	int type;		// 0表示攻击，1表示抢夺，2,3表示侦察，5表示支援, 4表示创建一个城堡
	int wood, stone, fe, grain;		// 抢夺或者携带的资源
	int opt;		// 投石车选择攻击，0表示攻击随机
	int hero;		// 英雄数量
	
	static CacheService cacheService = CacheService.getInstance();
	
	public AttackThreadBean() {
	}
	
	public static int SPEED_ADD_RANGE = 30;	// 30格以外速度加成生效
	
	public AttackThreadBean(int[] count,
			int sendCid, int sendX, int sendY, CastleBean castle,
			int receiveCid, int receiveX, int receiveY, int speedAdd, float heroSpeed) {
		startTime = System.currentTimeMillis();
		float speed = heroSpeed;
		SoldierResBean[] ss = ResNeed.getSoldierTs(castle.getRace());
		for(int i = 1;i < count.length;i++)
			if(count[i] > 0 && ss[i].getSpeed() < speed)
				speed = ss[i].getSpeed();

		fromCid = sendCid;
		toCid = receiveCid;
		float distance = CastleUtil.calcDistance(sendX - receiveX, sendY - receiveY);
		long interval;
		if(speedAdd == 0 || distance < SPEED_ADD_RANGE)
			interval= (long) (distance / speed * 3600000);
		else
			interval= (long) ((distance - SPEED_ADD_RANGE) / speed / (100 + speedAdd) * 360000000 + SPEED_ADD_RANGE / speed * 3600000);
		int art = CastleUtil.getActiveArtType(castle);
		if(art == 3)	// 拥有神器，两倍速度
			interval = interval / 2;
		else if(art == 13)	// 拥有神器，1.5倍速度
			interval = interval * 2 / 3;
		if(SqlUtil.isTest)	interval = 10000;

		endTime = startTime + interval;
		StringBuilder countsb = new StringBuilder(32);
		for(int i = 1;i < count.length;i++) {
			if(i != 1)
				countsb.append(',');
			countsb.append(count[i]);
		}
		soldierCount = countsb.toString();
		x = receiveX;
		y = receiveY;
		this.hero = count[0];
	}
	// 计算军队的速度
	public static float calcSpeed(int[] count,
			int sendX, int sendY, int sendRace,
			int receiveX, int receiveY, float heroSpeed) {
		float speed = heroSpeed;
		SoldierResBean[] ss = ResNeed.getSoldierTs(sendRace);
		for(int i = 1;i < count.length;i++)
			if(count[i] > 0 && ss[i].getSpeed() < speed)
				speed = ss[i].getSpeed();

		return speed;
	}
	// 重置目标城堡，只改变to_cid, x, y, end_time，用于群发攻击
	// 如果设置成功，返回 true
	public boolean resetTarget(float speed, int speedAdd, int toCid, int sendX, int sendY) {
		CastleBean castle = CastleUtil.getCastleById(toCid);
		if(castle == null)
			return false;
		this.toCid = toCid;
		startTime = System.currentTimeMillis();
		
		x = castle.getX();
		y = castle.getY();
		
		float distance = CastleUtil.calcDistance(sendX - x, sendY - y);
		long interval;
		if(speedAdd == 0 || distance < SPEED_ADD_RANGE)
			interval= (long) (distance / speed * 3600000);
		else
			interval= (long) ((distance - SPEED_ADD_RANGE) / speed / (100 + speedAdd) * 360000000 + SPEED_ADD_RANGE / speed * 3600000);
		if(SqlUtil.isTest)	interval = 10000;
		endTime = startTime + interval;
		return true;
	}
	
	static CastleService castleService = CastleService.getInstance();
	public void execute() {
		switch(type) {
		case 0:
		case 1:
		case 2:
		case 3:
			attack();
			break;
		case 4:
			expand();
			break;
		case 5:
			move();
			break;
		case 7:
		case 8:
		case 9:
			attack2();	// 出兵到绿洲
			break;
		case 10:
			move2();
			break;
		}
	}
	// 支援
	public void move() {
		CastleBean toCastle = CastleUtil.getCastleById(toCid);
		if(toCastle == null) {	// 城堡不存在
			endTime = System.currentTimeMillis() + endTime - startTime;
			cacheService.cancelAttackThreadBean(this);
			return;
		}
		CastleArmyBean tmpArmy = this.toArmy();

		CastleArmyBean toArmy;
		CastleBean castle;		// 所属城堡
		if(cid == toCid) {
			toArmy = CastleUtil.getCastleArmy(toCid);
			castle = toCastle;
		} else {
			if(toCastle.getRace() == 5) {	// 对方是纳塔，那么支援的军队直接回派
				endTime = System.currentTimeMillis() + endTime - startTime;
				cacheService.cancelAttackThreadBean(this);
				return;
			}
			CastleBean fromCastle = CastleUtil.getCastleById(cid);
			if(fromCastle == null) {
				cacheService.deleteAttackThreadBean(id);
				return;
			}
			castle = fromCastle;
			
			// 获取军队信息，如果没有，增加一条
			toArmy = CastleUtil.getCastleArmy(cid, toCid);
			
			String content = fromCastle.getCastleNameWml() + "支援" + toCastle.getCastleNameWml();
			StringBuilder sb = new StringBuilder(100);
			sb.append("<a href=\"search.jsp?cid=");
			sb.append(cid);
			sb.append("\">");
			sb.append(fromCastle.getCastleNameWml());
			sb.append("</a>");
			
			sb.append("支援");
			
			sb.append("<a href=\"search.jsp?cid=");
			sb.append(toCid);
			sb.append("\">");
			sb.append(toCastle.getCastleNameWml());
			sb.append("</a>");
			sb.append(':');
			tmpArmy.getSoldierString(castle.getRace(), sb);
			String detail = sb.toString();

			CastleUtil.addDetailMsg(fromCastle.getUid(), 3, content, detail);
			if(toCastle.getUid() != fromCastle.getUid())
				CastleUtil.addDetailMsg(toCastle.getUid(), 3, content, detail);
		}
		SoldierResBean[] ss = ResNeed.getSoldierTs(castle.getRace());
		int people = 0;
		int[] count3 = tmpArmy.getCount();
		for(int i = 1;i < count3.length;i++) {
			int count = count3[i];
			if(count > 0) {
				toArmy.addCount(i, count);
				people += ss[i].getPeople() * count;
			}
		}
		UserResBean toUserRes = CastleUtil.getUserResBeanById(toCid);
		if(hero != 0) {
			people += hero * ResNeed.heroGrainCost;
			if(cid != toCid && castle.getUid() == toCastle.getUid() && 
					toUserRes.getBuildingGrade(ResNeed.HERO_BUILD) > 0) {	// 自己支援自己，而且到达的地方有开采所，那么英雄变对方城堡的
				CastleArmyBean toArmy2 = CastleUtil.getCastleArmy(toCid);
				toArmy2.addHero(hero);
				castleService.updateSoldierCount(toArmy2);
			} else {
				toArmy.addHero(hero);
			}
		}
		castleService.updateSoldierCount(toArmy);
		
		if(cid != toCid) {
			// 军队支援到达，只要不是自己的军队，那么粮食消耗就会增加，支援方的粮食消耗相应减少
			if(people != 0) {
				CastleUtil.decreaseUserRes(cid, 0, 0, 0, 0, -people);
				CastleUtil.decreaseUserRes(toUserRes, 0, 0, 0, 0, people);
			} else {
				CastleUtil.increaseUserRes(toUserRes, wood, fe, grain, stone);
			}
		} else {
			CastleUtil.increaseUserRes(toUserRes, wood, fe, grain, stone);
		}

		cacheService.deleteAttackThreadBean(id);
	}
	public CastleArmyBean toArmy() {
		CastleArmyBean army = new CastleArmyBean();
		int[] count = army.getCount();
		if(soldierCount != null) {
			String[] s = soldierCount.split(",", ResNeed.soldierTypeCount);
			for(int i = 0;i < s.length;i++) {
				int c = StringUtil.toInt(s[i]);
				if(c >= 0)
					count[i + 1] = c;
			}
		}
		if(hero != 0)
			count[0] = hero;
		return army;
	}
	// 把String的soldier count转换为数组
	public int[] toCount() {
		int[] count = new int[ResNeed.soldierTypeCount + 1];
		if(soldierCount != null) {
			String[] s = soldierCount.split(",", ResNeed.soldierTypeCount);
			for(int i = 0;i < s.length;i++) {
				int c = StringUtil.toInt(s[i]);
				if(c >= 0)
					count[i + 1] = c;
			}
		}
		if(hero != 0)
			count[0] = hero;
		return count;
	}
	// 建立城堡
	public void expand() {
		CastleBean castle = CastleUtil.getCastleById(cid);
		if(castle == null) {
			cacheService.deleteAttackThreadBean(id);
			return;
		}
		synchronized(CastleUtil.createCastleLock) {
			boolean ok = true;
			int type = CastleUtil.getMapType(x, y);
			if(CastleUtil.getMapCastleId(x, y) != 0 || type > 16 || type <= 0) {		// 已经有城堡了，返回
				CastleMessage castleMessage = new CastleMessage("在" + x + "|" + y + "建立新城堡失败,该位置已不是空地", castle.getUid());
				castleService.addCastleMessage(castleMessage);
				ok = false;
			}
			long now = System.currentTimeMillis();
			CastleUserBean user = CastleUtil.getCastleUser(castle.getUid());
			if(ok) {
				if(user == null || !user.canCreate(now)) {
					CastleMessage castleMessage = new CastleMessage("在" + x + "|" + y + "建立新城堡失败,文明度不足", castle.getUid());
					castleService.addCastleMessage(castleMessage);
					ok = false;
				} else if(user.getCastleCount() >= ResNeed.maxCastleCount) {
					CastleMessage castleMessage = new CastleMessage("在" + x + "|" + y + "建立新城堡失败,城堡数量已经达到本阶段上限", castle.getUid());
					castleService.addCastleMessage(castleMessage);
					ok = false;
				}
			}
			// 判断行宫等级和扩张度
			UserResBean userRes = CastleUtil.getUserResBeanById(cid);
			if(ok) {
				int palaceGrade = userRes.getBuildingGrade(ResNeed.PALACE_BUILD);
				int palace2Grade = userRes.getBuildingGrade(ResNeed.PALACE2_BUILD);
				
				if(!castle.canExpand(palaceGrade, palace2Grade)) {
					CastleMessage castleMessage = new CastleMessage("在" + x + "|" + y + "建立新城堡失败,行宫或皇宫等级不足", castle.getUid());
					castleService.addCastleMessage(castleMessage);
					ok = false;
				}
			}
			if(ok) {	// 创建城堡
				CastleUtil.addUserCastle(user, x, y, cid);
				cacheService.deleteAttackThreadBean(id);
				userRes.addPeople2Calc(-3, now);
			} else {	// 创建失败
				endTime = now + endTime - startTime;
				cacheService.cancelAttackThreadBean(this);
			}
		}
	}
	// 攻击或者抢夺或者侦察
	public void attack(){
		float[] sendAttacks;
		float[] receiveDefences = new float[2];
		float sendAllAttack;
		
		CastleArmyBean tmpArmy = toArmy();
		int[] tmpCount = tmpArmy.getCount();
		
		List soldiers = toInts(soldierCount);
		CastleArmyBean attackAllLost = new CastleArmyBean();	// 攻击方损失
		
		CastleBean castleFrom = CastleUtil.getCastleById(fromCid);
		if(castleFrom == null) {
			cacheService.deleteAttackThreadBean(id);
			return;
		}
		CastleBean castleTo = CastleUtil.getCastleById(toCid);
		if(castleTo == null || castleTo.isPower() && (type == 0 || type == 1)) {	// 城堡被删除了？或者城堡不能被攻击（但是可以被侦察）
			CastleMessage castleMessage = new CastleMessage("派往" + x + "|" + y + "的军队没有发现敌方城堡的蛛丝马迹", castleFrom.getUid());
			castleService.addCastleMessage(castleMessage);
			if(castleFrom.getRace() == 5) {	// 如果是纳塔发出的进攻，不需要收回了
				cacheService.deleteAttackThreadBean(id);
			} else {
				endTime = System.currentTimeMillis() + endTime - startTime;
				cacheService.cancelAttackThreadBean(this);
			}
			return;
		}
		// 去出双方宝物（激活的）
		int fromArt = CastleUtil.getActiveArtType(castleFrom);
		int toArt = CastleUtil.getActiveArtType(castleTo);

		
		long now = System.currentTimeMillis();
		SoldierSmithyBean[] fromSmithy = CastleBaseAction.getSmithys(fromCid);
		
		float receiveAllDefence = 0;
		CastleUserBean fromUser = CastleUtil.getCastleUser(castleFrom.getUid());
		CastleUserBean toUser = CastleUtil.getCastleUser(castleTo.getUid());
		HeroBean sendHero = null;
		
		List receiveArmyList = castleService.getCastleArmyAtList(toCid);	// 所有支援等级都计算
		// 预先计算所有防御兵力的所在城堡，保存到armyList的每个army中
		for(int i = 0;i < receiveArmyList.size();i++) {
			CastleArmyBean army = (CastleArmyBean)receiveArmyList.get(i);
			if(army.getCid() == toCid) {
				army.setCastle(castleTo);
				army.setUser(toUser);
			} else {
				CastleBean c = CastleUtil.getCastleById(army.getCid());
				army.setCastle(c);
				army.setUser(CastleUtil.getCastleUser(c.getUid()));
			}
		}
		
		UserResBean receiveUserRes = CastleUtil.getUserResBeanById(toCid);
//		 攻击方掉入陷阱，攻击和抢夺模式有效
		CastleArmyBean trapArmy = null;	
		int trapCount = 0;
		List oriSoldiers = null;		// 用于备份掉陷阱之前的soldiers
		if(type < 2 && receiveUserRes.getTrap() != 0) {
			trapArmy = new CastleArmyBean();
			oriSoldiers = new ArrayList(soldiers);
			trapCount = trapArmy.splitFrom(receiveUserRes.getTrap(), soldiers);
		}

		//计算所有攻击力和所有防御力
//		float[] defensePart = new float[receiveArmyList.size()];		// 所有支援的军队攻击力，最后换算为比例
		CastleArmyBean[] receiveAllArmy = new CastleArmyBean[6];	// 所有防御兵力总和
		CastleArmyBean[] receiveAllLost = new CastleArmyBean[6];	// 所有防御兵力损失总和

		int palaceGrade = receiveUserRes.getBuildingGrade(ResNeed.PALACE_BUILD);
		int palace2Grade = receiveUserRes.getBuildingGrade(ResNeed.PALACE2_BUILD);

		if(type == 2 || type == 3) {
			sendAllAttack = ResNeed.calcScout(castleFrom.getRace(), soldiers, fromSmithy);
			if(fromArt == 4)	// 拥有神器，5倍侦察力量
				sendAllAttack *= 5;
			else if(fromArt == 14)	// 拥有神器，5倍侦察力量
				sendAllAttack *= 3;
			for(int i = 0;i < receiveArmyList.size();i++) {
				CastleArmyBean army = (CastleArmyBean)receiveArmyList.get(i);
				int race = army.getCastle().getRace();
				
				SoldierSmithyBean[] receiveSmithy = CastleBaseAction.getSmithys(army.getCid());
				receiveAllDefence += army.calcScoutDefense(race, receiveSmithy);
				if(receiveAllArmy[race] == null)
					receiveAllArmy[race] = new CastleArmyBean();
				receiveAllArmy[race].mergeCount(army);
			}
			if(castleFrom.getRace() == 5) {		// 如果是纳塔的侦察，无法反侦察
				receiveAllDefence = 0;
			}
		} else {
			sendAttacks = ResNeed.calcAttack(castleFrom.getRace(), soldiers, fromSmithy);
			if(hero > 0) {	// 带了英雄，但是英雄没有攻防升级
				sendHero = fromUser.getHero();
				if(sendHero != null) {
					sendAttacks[0] += sendHero.getAttack();
					sendAttacks[1] += sendHero.getAttack2();
					float attackX = sendHero.getAttackX();
					if(attackX != 0f) {
						attackX += 1f;
						sendAttacks[0] *= attackX;
						sendAttacks[1] *= attackX;
					}
				}
			}
			sendAllAttack = sendAttacks[0] + sendAttacks[1];
			
			int whistleGrade = receiveUserRes.getBuildingGrade(ResNeed.WHISTLE_BUILD);
			//被攻击者的所有防御，包括支援部队
			
			for(int i = 0;i < receiveArmyList.size();i++) {
				CastleArmyBean army = (CastleArmyBean)receiveArmyList.get(i);
				int race = army.getCastle().getRace();
			
				SoldierSmithyBean[] receiveSmithy = CastleBaseAction.getSmithys(army.getCid());
				float[] defenses = army.calcDefense(race, receiveSmithy);
				if(army.getHero() > 0) {
					HeroBean hero = army.getUser().getHero();
					if(hero != null) {
						defenses[0] += hero.getDefense();
						defenses[1] += hero.getDefense2();
						float defenseX = hero.getDefenseX();
						if(defenseX != 0f) {
							defenseX += 1f;
							defenses[0] *= defenseX;
							defenses[1] *= defenseX;
						}
					}
				}
				receiveDefences[0] += defenses[0];
				receiveDefences[1] += defenses[1];
//				defensePart[i] = defenses[0] * sendAttacks[0] + defenses[1] * sendAttacks[1];
				if(receiveAllArmy[race] == null)
					receiveAllArmy[race] = new CastleArmyBean();
				receiveAllArmy[race].mergeCount(army);
			}
			// 计算所有支援所占的比例
//			float tmpAll = receiveDefences[0] * sendAttacks[0] + receiveDefences[1] * sendAttacks[1];
//			for(int i = 0;i < receiveArmyList.size();i++) {
//				defensePart[i] /= tmpAll;
//			}

			if(whistleGrade != 0) {
				receiveAllDefence = ResNeed.getWhistleAttact(whistleGrade);
			}
			if(palaceGrade != 0)
				receiveAllDefence += palaceGrade * palaceGrade * 2;
			if(palace2Grade != 0)
				receiveAllDefence += palace2Grade * palace2Grade * 3;
			if(sendAllAttack != 0)	// 攻击力为0？
				receiveAllDefence += (receiveDefences[0] * sendAttacks[0] + receiveDefences[1] * sendAttacks[1])
						/ sendAllAttack;
			else
				receiveAllDefence += 100;	// 攻击力如果是0，防御就是无穷大
		}
		
		
		float morale = 1f;	// 人口惩罚，增加防御方的能力、防忽悠能力、城墙坚固，奇迹村没有限制
		float morale2 = 1f;	// 人口惩罚，增加建筑稳固度
		if(!castleTo.isNatar()) {
			morale = (float)fromUser.getPeople() / toUser.getPeople();
			if(morale < 1f) {
				
			} else {
				morale2 = (float)Math.pow(morale, 0.3f);
				morale = (float)Math.pow(morale, 0.2f);
				if(morale > 1.5f)
					morale = 1.5f;
				if(morale2 > 3f)
					morale2 = 3f;
				receiveAllDefence *= morale;
			}
		}
		
		UserResBean sendUserRes = CastleUtil.getUserResBeanById(fromCid);
		
		// 计算金币商城的攻防效果
		if(sendUserRes.isFlagAttack())
			sendAllAttack *= 1.1f;
		if(receiveUserRes.isFlagDefense())
			receiveAllDefence *= 1.1f;
		
		// 冲撞车效果
		int wallType = ResNeed.raceWall[castleTo.getRace()], wallGrade = 0, wallDown = -1;	// 城墙级别和下降多少，如果没有冲撞车，wallDown = 0
		BuildingBean hitBuilding = null;
		int hitGrade = 0, hitDown = -1;	// 投石车砸的情况，hitpos表示砸的建筑（暂时不允许砸资源），hitgrade表示砸之前的等级，hitdown表示砸掉的等级
		if(type == 0) {
			int bump = ((Integer)soldiers.get(6)).intValue();	// 冲装车
			int bump2 = ((Integer)soldiers.get(7)).intValue();	// 投石车，注意，投石车id是8，但soldiers这里是从0开始，所以7就行
			// 模拟抢夺，损失部分，然后再砸
			if(sendAllAttack > receiveAllDefence) {		
				float factor = receiveAllDefence / sendAllAttack;
				factor = (float)Math.pow(factor, 1.5);
				factor = factor / (1 + factor);
				bump -= Math.round(bump * factor);
				bump2 -= Math.round(bump2 * factor);
			} else  {
				float factor = sendAllAttack / receiveAllDefence;
				factor = (float)Math.pow(factor, 1.5);
				factor = 1.0f / (1 + factor);
				bump -= Math.round(bump * factor);
				bump2 -= Math.round(bump2 * factor);
			}
			if(bump != 0) {
				wallGrade = receiveUserRes.getBuildingGrade(wallType);
				if(wallGrade > 0) {
					float factor = 1f;
					float a = 60 * bump;
					if(fromSmithy[7] != null)
						a *= ResNeed.attackFactor[fromSmithy[7].getAttack()];
					float b = 150 * wallGrade * morale;
					if(castleTo.getRace() == 3)		// 不同种族的城墙，稳固度不同
						b *= 2;
					else if(castleTo.getRace() == 2)
						b *= 5;
						
					if(a < b) {
						wallDown = (int)(Math.pow(a / b, 1.5f) * wallGrade);
					} else
						wallDown = wallGrade;
					if(wallDown > 0) {
						CastleUtil.destroyBuilding(castleTo.getUid(), castleService.getBuildingBean(wallType, castleTo.getId()), wallGrade - wallDown);
					}
				}
			}
			if(bump2 != 0 && (receiveUserRes.getPeople() >= 400 || toUser.getMain() != castleTo.getId())) {		// 主城400人口以上才能攻击
				if(opt != 0 && (toArt == 8 || toArt == 18)) {		// 拥有宝物，无法瞄准
					if(opt != ResNeed.TREASURE_BUILD)
						opt = 0;
				}
				if(opt != 0 && receiveUserRes.getBuildingGrade(opt) != 0) {
					List hitList = SqlUtil.getIntList("select build_pos from castle_building where cid=" + toCid + " and build_type=" + opt, 5);
					if(hitList.size() > 0) {
						hitBuilding = castleService.getBuildingBeanByPos(castleTo.getId(), ((Integer)RandomUtil.randomObject(hitList)).intValue());
					}
				} else {	
					List hitList = SqlUtil.getIntList("select build_pos from castle_building where cid=" + toCid + " and build_pos>=19 and build_type !=26 and build_type !=28 and build_type !=29", 5);
					if(hitList.size() > 0) {
						hitBuilding = castleService.getBuildingBeanByPos(castleTo.getId(), ((Integer)RandomUtil.randomObject(hitList)).intValue());
					}
				}
				if(hitBuilding != null) {
					hitGrade = hitBuilding.getGrade();
					float factor = 1f;
					float a = 60 * bump2;
					if(fromSmithy[8] != null)
						a *= ResNeed.attackFactor[fromSmithy[8].getAttack()];
					if(castleFrom.getRace() == 5)
						a += a;	// 纳塔投石的攻击效果是其他种族的2倍
					float b = 200 * hitGrade * morale2;
					if(!castleTo.isNatar())	{// 如果是奇迹村无效
						if(toArt == 2)
							b *= 4;
						else if(toArt == 12)
							b *= 3;
					}
					int stone2Grade = receiveUserRes.getBuildingGrade(ResNeed.STONE2_BUILD);
					if(stone2Grade > 0)
						b += b * stone2Grade / 10;
					if(a < b) {
						hitDown = (int)(Math.pow(a / b, 1.5f) * hitGrade);
					} else
						hitDown = hitGrade;
					if(hitDown > 0) {
						CastleUtil.destroyBuilding(castleTo.getUid(), hitBuilding, hitGrade - hitDown);
					}
				}
			}
		}
		
		SoldierResBean[] gsAttack = ResNeed.getSoldierTs(castleFrom.getRace());
		receiveAllDefence += receiveAllDefence * receiveUserRes.getWall() / 100;	// 城墙效果
		int sendDead = 0;	// 攻击方死亡的人口数
		int receiveDead = 0;	// 被攻击方死亡的人口数
		
		float sendLost = 0f;		// 进攻放损失的百分比
		float receiveLost = 0f;		// 防守方所有部队损失的百分比
		
		//如果攻击者的攻击大于被攻击者的防御，则攻下城堡
		// 注意，侦察的计算方式类似于抢夺，但是防御方不死伤
		if(sendAllAttack > receiveAllDefence) {		
			float factor = receiveAllDefence / sendAllAttack;
			factor = (float)Math.pow(factor, 1.5);

			if(type == 0) {
				sendLost = factor;
				receiveLost = 1.0f;
			} else {	// 抢夺
				sendLost = factor / (1 + factor);
				receiveLost = 1.0f / (1 + factor);	// 全部损失
			}

		} else if(receiveAllDefence > sendAllAttack) {	//如果被攻击者的防御大于攻击者的攻击，则攻不下城堡
			float factor = sendAllAttack / receiveAllDefence;
			factor = (float)Math.pow(factor, 1.5);
			
			if(type == 0) {
				sendLost = 1.0f;
				receiveLost = factor;		
			} else {	// 抢夺
				sendLost = 1.0f / (1 + factor);
				receiveLost = factor / (1 + factor);
			}
			
		} else 	if(receiveAllDefence == sendAllAttack) {	//如果被攻击人的防御和攻击者的攻击想同，则士兵全部死掉
			//减掉防守方失去的兵力，启动线程回城
			if(type == 0) {	
				sendLost = 1f;
				receiveLost = 1f;
			} else {	// 抢夺
				sendLost = 0.5f;
				receiveLost = 0.5f;
			}
		}
		
		int leftChief = 0;	// 攻击有效的忽悠（攻击后剩余数量）
		int raidCount = 0;		// 抢夺的资源总量
		int leftCount = 0;	// 剩余军队
		int trapDead = 0;		// 掉入陷阱死亡
		CastleArmyBean leftArmy = null;
		if(sendLost != 0 || trapCount != 0) {
			StringBuilder sbc = new StringBuilder(32);
			leftArmy = new CastleArmyBean();
			for(int i = 0;i < soldiers.size();i++) {
				int count = ((Integer)soldiers.get(i)).intValue();
				if(count > 0) {
					if(sendLost == 1.0f) {
						sendDead += gsAttack[i + 1].getPeople() * count;
						attackAllLost.setCount(i + 1, count);
						count = 0;
					} else {
						int dead = (int) Math.round(count * sendLost);
						sendDead += gsAttack[i + 1].getPeople() * dead;
						count -= dead;
						leftCount += count;
						attackAllLost.setCount(i + 1, dead);
					}
				}
				leftArmy.setCount(i + 1, count);
			}
			if(hero != 0) {
				if(sendHero != null) {
					sendHero.reCalc(now);
					float health = sendHero.getHealth();
					health -= sendLost * 100;
					if(health < 0.5f) {
						health = 0f;
						sendHero.setStatus(HeroBean.STATUS_DEAD);
						fromUser.deleteHero();
						attackAllLost.setHero(1);
						sendDead += ResNeed.heroGrainCost;
					} else {
						leftArmy.setHero(1);
						leftCount++;
					}
					sendHero.setHealth(health);
					castleService.updateHeroSimple(sendHero);
				}
			}
			
			if(leftCount > 0) {
				raidCount = ResNeed.calcStore(castleFrom.getRace(), leftArmy.getCount());
				if(type == 0) {		// 攻击模式下忽悠数量才有效
					if(palaceGrade == 0 && palace2Grade == 0 && castleTo.getUid() != castleFrom.getUid())		// 如果有皇宫或者行宫，失败
						leftChief = leftArmy.getCount(10);
				}
			}

			if(trapCount > 0) {
				trapDead = trapCount / 4;		// 掉入陷阱死亡的人数
				if(leftCount > 0 && type == 0)
					trapDead = trapCount / 8;	// 如果攻击方剩余兵力，那么获救的士兵数更多，选择抢夺模式则无效
				else if(trapDead == 0)
					trapDead = 1;		//  如果没有办法获救，至少死一人
				leftCount += trapCount - trapDead;
				if(trapCount == 1)
					receiveUserRes.addTrapDB(-1);	// 损坏最后一个
				else
					receiveUserRes.addTrapDB(-trapCount / 2);	// 损坏一半
				soldiers = oriSoldiers;
				// 攻击方救回部分
				trapArmy.split(trapDead, attackAllLost);
				sendDead = attackAllLost.getGrainCost(gsAttack);	// 根据新的损失数据重新计算
				
				leftArmy.mergeCount(trapArmy);
			}
				
			soldierCount = leftArmy.toString();
			hero = leftArmy.getHero();
		} else {
			raidCount = ResNeed.calcStore(castleFrom.getRace(), soldiers);
			leftCount = 1;
			if(type == 0) {		// 攻击模式下忽悠数量才有效
				if(palaceGrade == 0 && palace2Grade == 0)		// 如果有皇宫或者行宫，失败
					leftChief = ((Integer)soldiers.get(9)).intValue();
			}
		}
		
		if(type == 2 || type == 3)		// 对于侦察，防守方兵力无损耗
			receiveLost = 0f;

		else if(receiveLost > 0f && receiveArmyList.size() > 0) {
			String castleToName = castleTo.getCastleNameWml();
			String content = "给" + castleToName + "的支援被攻击";
			float heroHealthHit = receiveLost * 100;	// 防御方英雄掉血是一样的
			List receiveHero = new ArrayList(4);
			
			for(int j = 0;j < receiveArmyList.size();j++) {
				CastleArmyBean army = (CastleArmyBean)receiveArmyList.get(j);
				int race = army.getCastle().getRace();
				
				SoldierResBean[] gs = ResNeed.getSoldierTs(race);
				
				if(receiveAllLost[race] == null)
					receiveAllLost[race] = new CastleArmyBean();
				
				int[] receiveCounts = army.getCount();
				int[] tmp = new int[ResNeed.soldierTypeCount + 1];
				int[] tmp2 = (int[])receiveCounts.clone();
				for (int i = 1; i < receiveCounts.length; i++) {
					int count = receiveCounts[i];
					if(count > 0) {
						int dead = (int) Math.round(count * receiveLost);
						receiveDead += gs[i].getPeople() * dead;
						receiveCounts[i] -= dead;

						receiveAllLost[race].addCount(i, dead);
						tmp[i] = dead;
					}
				}
				if(army.getHero() > 0) {
					HeroBean hero = army.getUser().getHero();
					if(hero != null) {
						receiveHero.add(hero);
						hero.reCalc(now);
						float health = hero.getHealth();
						health -= heroHealthHit;
						if(health < 0.5f) {
							health = 0f;
							hero.setStatus(HeroBean.STATUS_DEAD);
							army.getUser().deleteHero();
							
							receiveDead += ResNeed.heroGrainCost;
							receiveAllLost[race].addHero(1);
							receiveCounts[0] = 0;
							tmp[0] = 1;
						} else {
							
						}
						hero.setHealth(health);
						castleService.updateHeroSimple(hero);
					}
				}
				if(army.getCid() != toCid && army.isEmpty())
					castleService.deleteCastleArmyById(army.getId());	// 死光，记录删除
				else
					castleService.updateSoldierCount(army);
				if(army.getCid() != toCid) {
					CastleBean rein = CastleUtil.getCastleById(army.getCid());
					if(rein.getUid() != castleTo.getUid()) {	// 如果是自己军队攻击，则不显示支援
						// 支援被攻击
						StringBuilder sb = new StringBuilder(100);
						sb.append("[支援方]");
						sb.append("<a href=\"search.jsp?cid=");
						sb.append(rein.getId());
						sb.append("\">");
						sb.append(rein.getCastleNameWml());
						sb.append("</a>支援");
						sb.append("<a href=\"search.jsp?cid=");
						sb.append(castleTo.getId());
						sb.append("\">");
						sb.append(castleToName);
						sb.append("</a>");
						sb.append("<br/>兵力-损失:");
						CastleUtil.getSoldierLostString(army.getCastle().getRace(), tmp2, tmp, sb);
						CastleUtil.addDetailMsg(rein.getUid(), 4, content, sb.toString());
					}
				}
			}
			if(receiveHero.size() != 0) {
				int exp = sendDead / receiveHero.size();
				if(exp != 0) {
					DbOperation db = new DbOperation(5);
					for(int i = 0;i < receiveHero.size();i++) {
						HeroBean h = (HeroBean)receiveHero.get(i);
						h.addExp(exp);
						db.executeUpdate("update castle_hero set exp=" + h.getExp() + " where id=" + h.getId());
					}
					db.release();
				}
			}
			if(receiveDead > 0 && sendHero != null){
				sendHero.addExp(receiveDead);
				SqlUtil.executeUpdate("update castle_hero set exp=" + sendHero.getExp() + " where id=" + sendHero.getId(), 5);
			}
		}
		// 给所有英雄增加经验值
		

		int robRes = 0;
//		 战报标题
		String content = castleFrom.getCastleNameWml() + typeNames[type] + castleTo.getCastleNameWml();
		int pos = castleTo.getPos();	// 战争发生在哪里
		boolean chiefSuccess = false;	// 忽悠到忠诚度为0，并且成功占领
		if(type == 2 || type == 3) {	// 侦察
			if(leftCount == 0) {
				StringBuilder sb = new StringBuilder(100);
				sb.append("[攻击方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleFrom.getId());
				sb.append("\">");
				sb.append(castleFrom.getCastleNameWml());
				sb.append("</a><br/>");
				sb.append("兵力-损失:");
				CastleUtil.getSoldierLostString(castleFrom.getRace(), tmpCount, attackAllLost.getCount(), sb);
				sb.append("<br/>信息:你的士兵没有回来.<br/>");
				sb.append("[防御方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleTo.getId());
				sb.append("\">");
				sb.append(castleTo.getCastleNameWml());
				sb.append("</a><br/>");
				String detail = sb.toString();
				CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail + "兵力:未知", pos);
				if(castleFrom.getUid() != castleTo.getUid()) {		// 如果侦察方有损失，被侦察方会发现
					for(int i = 1;i <= 5;i++)
						if(receiveAllArmy[i] != null) {
							sb.append("兵力:");
							receiveAllArmy[i].getSoldierString(i, sb);
							sb.append("<br/>");
						}
					CastleUtil.addDetailMsg(castleTo.getUid(), 2, content, sb.toString(), pos);
				}
				cacheService.deleteAttackThreadBean(id);
			} else {
				StringBuilder sb = new StringBuilder(100);
				sb.append("[攻击方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleFrom.getId());
				sb.append("\">");
				sb.append(castleFrom.getCastleNameWml());
				sb.append("</a><br/>");
				sb.append("兵力-损失:");
				CastleUtil.getSoldierLostString(castleFrom.getRace(), tmpCount, attackAllLost.getCount(), sb);
				sb.append("<br/>[防御方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleTo.getId());
				sb.append("\">");
				sb.append(castleTo.getCastleNameWml());
				sb.append("</a><br/>");
				if(castleFrom.getRace() == 5) {	// 如果是纳塔的侦察，只能看见资源，不能看见兵力
					sb.append("兵力:未知<br/>");
				} else {
					for(int i = 1;i <= 5;i++)
						if(receiveAllArmy[i] != null) {
							sb.append("兵力:");
							receiveAllArmy[i].getSoldierString(i, sb);
							sb.append("<br/>");
						}
				}
				if(type == 2) {
					sb.append("资源:");
					sb.append("木");
					sb.append(receiveUserRes.getWood(now));
					sb.append("石");
					sb.append(receiveUserRes.getStone(now));
					sb.append("铁");
					sb.append(receiveUserRes.getFe(now));
					sb.append("粮");
					sb.append(receiveUserRes.getGrain(now));
				} else {
					sb.append("建筑:");
					sb.append("城墙");
					sb.append(receiveUserRes.getBuildingGrade(wallType));
					sb.append('级');
					sb.append(",哨塔");
					sb.append(receiveUserRes.getBuildingGrade(ResNeed.WHISTLE_BUILD));
					sb.append('级');
					sb.append(",行宫");
					sb.append(receiveUserRes.getBuildingGrade(ResNeed.PALACE_BUILD));
					sb.append('级');
				}
				String detail = sb.toString();
				CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail, pos);
				// 如果是纳塔的侦察，怎么都能看见
//				 如果侦察方有损失，被侦察方会发现
				if((sendLost != 0f || castleFrom.getRace() == 5) && castleFrom.getUid() != castleTo.getUid()) {
					CastleUtil.addDetailMsg(castleTo.getUid(), 2, content, detail, pos);
				}
				if(castleFrom.getRace() == 5) {	// 如果是纳塔发出的进攻，不需要收回了
					cacheService.deleteAttackThreadBean(id);
				} else {
					endTime = System.currentTimeMillis() + endTime - startTime;
					cacheService.cancelAttackThreadBean(this);
				}
			}
		} else {
			if(leftCount == 0) {
				StringBuilder sb = new StringBuilder(100);
				sb.append("[攻击方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleFrom.getId());
				sb.append("\">");
				sb.append(castleFrom.getCastleNameWml());
				sb.append("</a><br/>");
				sb.append("兵力-损失:");
				CastleUtil.getSoldierLostString(castleFrom.getRace(), tmpCount, attackAllLost.getCount(), sb);
				sb.append("<br/>信息:你的士兵没有回来.");
				sb.append("<br/>[防御方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleTo.getId());
				sb.append("\">");
				sb.append(castleTo.getCastleNameWml());
				sb.append("</a><br/>");
				
				String detail = sb.toString();
				for(int i = 1;i <= 5;i++)
					if(receiveAllArmy[i] != null) {
						sb.append("兵力-损失:");
						if(receiveAllLost[i] != null)
							CastleUtil.getSoldierLostString(i, receiveAllArmy[i].getCount(), receiveAllLost[i].getCount(), sb);
						else
							CastleUtil.getSoldierNoLostString(i, receiveAllArmy[i].getCount(), sb);
						sb.append("<br/>");
					}
				if(wallDown >= 0) {
					sb.append("信息:");
					sb.append(ResNeed.getBuildingT(wallType).getName());
					sb.append("遭到破坏,等级从");
					sb.append(wallGrade);
					sb.append("降到");
					sb.append(wallGrade - wallDown);
					sb.append("<br/>");
				}
				if(hitDown >= 0) {
					sb.append("信息:");
					sb.append(ResNeed.getBuildingT(hitBuilding.getBuildType()).getName());
					sb.append("遭到破坏,等级从");
					sb.append(hitGrade);
					sb.append("降到");
					sb.append(hitGrade - hitDown);
					sb.append("<br/>");
				}
				String detail2 = sb.toString();
				
				if(sendAllAttack + sendAllAttack >= receiveAllDefence)
					CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail2, pos);
				else
					CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail + "兵力:未知", pos);
				if(castleFrom.getUid() != castleTo.getUid()) {
					CastleUtil.addDetailMsg(castleTo.getUid(), 2, content, detail2, pos);
				}
				cacheService.deleteAttackThreadBean(id);
			} else {
				int fromLoyal = -1, toLoyal = 0;	// 成功下降多少点忠诚度
				if(leftChief > 0 && type == 0) {		// 忽悠数量>0
					receiveUserRes.reCalcLoyal(now);
					fromLoyal = receiveUserRes.getLoyal() / 10000;
					int random;
					if(castleFrom.getRace() == 1)
						random = RandomUtil.nextInt(11) + 20;	// 拜锁斯单个忽悠效果为20 - 30
					else
						random = RandomUtil.nextInt(6) + 20;	// 单个忽悠效果为20 - 25
					float chiefE = random * leftChief / morale;
					receiveUserRes.decLoyal(chiefE);
					if(receiveUserRes.getLoyal() < 1000 && (toUser.getCastleCount() > 3 || castleTo.isNatar())) {	// 至少拥有4个城堡才能忽悠
						if(castleTo.getId() != toUser.getMain()) {
							chiefSuccess = true;
							if(chiefSuccess) {
								// 不能占领主城
								if(fromUser == null || !fromUser.canCreate(now) || toUser.getMain() == castleTo.getId()) {
									CastleMessage castleMessage = new CastleMessage("无法占领" + castleTo.getX() + "|" + castleTo.getY() + ",文明度不足", castleFrom.getUid());
									castleService.addCastleMessage(castleMessage);
									chiefSuccess = false;
								} else if(fromUser.getCastleCount() >= ResNeed.maxCastleCount) {
									CastleMessage castleMessage = new CastleMessage("无法占领" + castleTo.getX() + "|" + castleTo.getY() + ",城堡数量达到上限", castleFrom.getUid());
									castleService.addCastleMessage(castleMessage);
									chiefSuccess = false;
								} else if(castleTo.isArt()) {		// 纳塔宝库村无法占领
									CastleMessage castleMessage = new CastleMessage("无法占领" + castleTo.getX() + "|" + castleTo.getY(), castleFrom.getUid());
									castleService.addCastleMessage(castleMessage);
									chiefSuccess = false;
								}
							}
							// 判断行宫等级和扩张度
							if(chiefSuccess) {
								UserResBean userRes = CastleUtil.getUserResBeanById(cid);
								palaceGrade = sendUserRes.getBuildingGrade(ResNeed.PALACE_BUILD);
								palace2Grade = sendUserRes.getBuildingGrade(ResNeed.PALACE2_BUILD);
								
								if(!castleFrom.canExpand(palaceGrade, palace2Grade)) {
									CastleMessage castleMessage = new CastleMessage("无法占领" + castleTo.getX() + "|" + castleTo.getY() + ",行宫或皇宫等级不足", castleFrom.getUid());
									castleService.addCastleMessage(castleMessage);
									chiefSuccess = false;
								}
							}
						}
					}
					toLoyal = receiveUserRes.getLoyal() / 10000;
				}
				// 判断抢夺宝物
				int captureArt = -1;
				if(type == 0 && hero != 0 && receiveUserRes.getBuildingGrade(ResNeed.TREASURE_BUILD) == 0) {
					// 注意参数的调用，攻击from对方to，但是move的时候是反向
					captureArt = CastleUtil.moveArt(castleTo, castleFrom, sendUserRes.getBuildingGrade(ResNeed.TREASURE_BUILD));	// 就算判断成功，moveArt函数内还有可能失败
				}
				// 非侦察，抢夺资源
				int cave = receiveUserRes.getCave();
				if(castleTo.getRace() == 3)	// 这个种族山洞双倍
					cave += cave;
				if(toArt == 8)
					cave *= 200;
				else if(toArt == 18)
					cave *= 100;
				if(castleFrom.getRace() == 2)	// 这个种族抢劫时山洞扣除1/3
					cave = cave * 3 / 4;
				SoldierResBean soldierRob = new SoldierResBean(receiveUserRes, cave, raidCount);
				//更新抢与被抢的资源
				wood = soldierRob.getWood();
				stone = soldierRob.getStone();
				fe = soldierRob.getFe();
				grain = soldierRob.getGrain();
				robRes = wood + stone + fe + grain;
				synchronized(CacheManage.castleUserRes) {
					CastleUtil.decreaseUserRes(receiveUserRes, soldierRob.getWood(), soldierRob.getFe(), soldierRob.getGrain(), soldierRob.getStone());
				}
				
				StringBuilder sb = new StringBuilder(100);
				sb.append("[攻击方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleFrom.getId());
				sb.append("\">");
				sb.append(castleFrom.getCastleNameWml());
				sb.append("</a><br/>");
				sb.append("兵力-损失:");
				CastleUtil.getSoldierLostString(castleFrom.getRace(), tmpCount, attackAllLost.getCount(), sb);

				sb.append("<br/>缴获:");
				sb.append("木");
				sb.append(soldierRob.getWood());
				sb.append("石");
				sb.append(soldierRob.getStone());
				sb.append("铁");
				sb.append(soldierRob.getFe());
				sb.append("粮");
				sb.append(soldierRob.getGrain());
				if(trapCount > 0) {
					sb.append("<br/>信息:");
					sb.append(trapCount);
					sb.append("士兵掉入陷阱");
					sb.append(trapCount - trapDead);
					sb.append("人逃脱");
				}
				
				sb.append("<br/>[防御方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleTo.getId());
				sb.append("\">");
				sb.append(castleTo.getCastleNameWml());
				sb.append("</a><br/>");
				for(int i = 1;i <= 5;i++)
					if(receiveAllArmy[i] != null) {
						sb.append("兵力-损失:");
						if(receiveAllLost[i] != null)
							CastleUtil.getSoldierLostString(i, receiveAllArmy[i].getCount(), receiveAllLost[i].getCount(), sb);
						else
							CastleUtil.getSoldierNoLostString(i, receiveAllArmy[i].getCount(), sb);
						sb.append("<br/>");
					}
				if(wallDown >= 0) {
					sb.append("信息:");
					sb.append(ResNeed.getBuildingT(wallType).getName());
					sb.append("遭到破坏,等级从");
					sb.append(wallGrade);
					sb.append("降到");
					sb.append(wallGrade - wallDown);
					sb.append("<br/>");
				}
				if(hitDown >= 0) {
					sb.append("信息:");
					sb.append(ResNeed.getBuildingT(hitBuilding.getBuildType()).getName());
					sb.append("遭到破坏,等级从");
					sb.append(hitGrade);
					sb.append("降到");
					sb.append(hitGrade - hitDown);
					sb.append("<br/>");
				}
				if(chiefSuccess) {
					sb.append("信息:");
					sb.append(StringUtil.toWml(castleTo.getCastleName()));
					sb.append("的人民决定加入你的帝国<br/>");
				} else if(fromLoyal >= 0) {
					sb.append("信息:");
					sb.append("居民忠诚度从");
					sb.append(fromLoyal);
					sb.append("%降到");
					sb.append(toLoyal);
					sb.append("%<br/>");
				}
				if(captureArt != -1) {
					sb.append("信息:");
					if(captureArt == 0) {
						sb.append("宝物[");
						ArtBean art = CastleUtil.getCastleArt(castleFrom.getId());
						sb.append(art.getName());
						sb.append("]被攻击者占有");
					} else if(captureArt == 1) {
						sb.append("无法抢夺宝物,攻击方已经拥有其他宝物");
					} else if(captureArt == 2) {
						sb.append("无法抢夺宝物,宝库等级不足");
					}
					sb.append("<br/>");
				}
				String detail = sb.toString();
				
				CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail, pos);
				if(castleFrom.getUid() != castleTo.getUid()) {
					CastleUtil.addDetailMsg(castleTo.getUid(), 2, content, detail, pos);
				}
				
				// 忽悠成功，执政官消失
				if(chiefSuccess) {
					if(leftArmy == null) {		// 没有损失的情况
						tmpArmy.addCount(10, -1);
						soldierCount = tmpArmy.toString();
					} else {
						leftArmy.addCount(10, -1);
						soldierCount = leftArmy.toString();
					}
				}
				
				if(castleFrom.getRace() == 5) {	// 如果是纳塔发出的进攻，不需要收回了
					cacheService.deleteAttackThreadBean(id);
				} else {
					endTime = System.currentTimeMillis() + endTime - startTime;
					cacheService.cancelAttackThreadBean(this);
				}
			}
		}
		
		if(sendDead > 0) {
			if(castleFrom.getRace() != 5) {	// 如果是纳塔，不计算士兵的粮食消耗
				sendUserRes.addPeople2Calc(-sendDead, now);
			}
			if(castleTo.getUid() != castleFrom.getUid()) {
				// 防御总点数
				toUser.defenseTotal += sendDead;
				toUser.defenseWeek += sendDead;
				SqlUtil.executeUpdate("update castle_user set defense_total=" + toUser.defenseTotal + ",defense_week=" + toUser.defenseWeek + " where uid=" + toUser.getUid(), 5);
			}
		}
		if(chiefSuccess)	// 如果忽悠成功，减少一个人口，因为执政官消失
			sendUserRes.addPeople2Calc(- ResNeed.getSoldierRes(castleFrom.getRace(), 10).getPeople(), now);
		
		if(receiveDead > 0) {
			if(castleTo.getRace() != 5)	// 如果是纳塔，不计算士兵的粮食消耗
				receiveUserRes.addPeople2Calc(-receiveDead, now);
		}
		if((receiveDead != 0 || robRes != 0) && castleTo.getUid() != castleFrom.getUid()) {
			// 加上攻击总点数，抢夺总点数
			fromUser.attackTotal += receiveDead;
			fromUser.robTotal += robRes;
			fromUser.attackWeek += receiveDead;
			fromUser.robWeek += robRes;
			SqlUtil.executeUpdate("update castle_user set attack_total=" + fromUser.attackTotal + ",attack_week=" + fromUser.attackWeek + ",rob_total=" + fromUser.robTotal + ",rob_week=" + fromUser.robWeek + " where uid=" + fromUser.getUid(), 5);
		}
		// 如果是占领，执行，这个必须放到最后，否则之前的战绩等数据就会错误
		if(chiefSuccess) {
			CastleUtil.occupyUserCastle(fromUser, castleFrom, castleTo, receiveUserRes, toUser);
		}/* else if(captureArt == 0) {
			// 如果是抢夺宝物成功而且是抢夺纳塔的宝库村，村子消失
			if(castleTo.isArt()) {
				CastleUtil.deleteCastleQuick(castleTo);
			}
		}*/
	}
	
//	 支援绿洲（不包括返回）
	public void move2() {
		OasisBean oasis = CastleUtil.getOasisByXY(x, y);
		if(oasis.getCid() == 0)	{	// 没有占领的绿洲不能支援
			cacheService.cancelAttackThreadBean(this);
			return;
		}
		
		List soldiers = toInts(soldierCount);

		toCid = oasis.getCid();
		CastleBean toCastle = CastleUtil.getCastleById(toCid);
		if(toCastle == null) {	// 城堡不存在
			cacheService.cancelAttackThreadBean(this);
			return;
		}

		CastleBean fromCastle = CastleUtil.getCastleById(cid);
		if(fromCastle == null) {
			cacheService.deleteAttackThreadBean(id);
			return;
		}
		
		// 获取军队信息，如果没有，增加一条
		CastleArmyBean toArmy = CastleUtil.getOasisArmy(cid, oasis.getId(), toCid);
		
		String content = fromCastle.getCastleNameWml() + "支援" + toCastle.getCastleNameWml() + "的绿洲";
		StringBuilder sb = new StringBuilder(100);
		sb.append("<a href=\"search.jsp?cid=");
		sb.append(cid);
		sb.append("\">");
		sb.append(fromCastle.getCastleNameWml());
		sb.append("</a>");
		
		sb.append("支援");
		
		sb.append("<a href=\"search.jsp?x=");
		sb.append(x);
		sb.append("&amp;y=");
		sb.append(y);
		sb.append("\">");
		sb.append(toCastle.getCastleNameWml());
		sb.append("的绿洲</a>");
		sb.append(':');
		CastleUtil.getSoldierString(fromCastle.getRace(), soldiers, hero, sb);
		String detail = sb.toString();

		CastleUtil.addDetailMsg(fromCastle.getUid(), 3, content, detail);
		if(toCastle.getUid() != fromCastle.getUid())
			CastleUtil.addDetailMsg(toCastle.getUid(), 3, content, detail);

		SoldierResBean[] ss = ResNeed.getSoldierTs(fromCastle.getRace());
		int people = 0;
		for(int i = 0;i < soldiers.size();i++) {
			int count = ((Integer)soldiers.get(i)).intValue();
			if(count > 0) {
				toArmy.addCount(i + 1, count);
				people += ss[i + 1].getPeople() * count;
			}
		}
		if(hero != 0) {
			people += hero * ResNeed.heroGrainCost;
			toArmy.addHero(hero);
		}
		castleService.updateOasisSoldierCount(toArmy);

		// 军队支援到达，只要不是自己的军队，那么粮食消耗就会增加，支援方的粮食消耗相应减少
		if(people != 0 && fromCid != toCastle.getId()) {
			CastleUtil.decreaseUserRes(cid, 0, 0, 0, 0, -people);
			CastleUtil.decreaseUserRes(toCid, 0, 0, 0, 0, people);
		}

		cacheService.deleteAttackThreadBean(id);
	}
	// 攻击或者抢夺或者侦察绿洲
	public void attack2(){
		float[] sendAttacks;
		float[] receiveDefences = new float[2];
		float sendAllAttack;
		
		CastleArmyBean tmpArmy = toArmy();
		int[] tmpCount = tmpArmy.getCount();
		// 非绿洲
		int oasisType = CastleUtil.getMapType(x, y);
		if(CastleUtil.getMapCastleId(x, y) != 0 || oasisType <= 16) {
			cacheService.cancelAttackThreadBean(this);
			return;
		}
		oasisType -= 16;
		OasisBean oasis = CastleUtil.getOasisByXY(x, y);
		if(oasis.getId() == -1)	// 攻击的时候才生成oasis
			oasis = CastleUtil.buildOasis(x, y, oasisType);
		long now = System.currentTimeMillis();
		if(oasis.getCid() == 0)
			CastleUtil.updateOasisArmy(oasis, now);
		
		if(type == 10 && oasis.getCid() == 0) {
			cacheService.cancelAttackThreadBean(this);
			return;
		}
		
		List soldiers = toInts(soldierCount);
		CastleArmyBean attackAllLost = new CastleArmyBean();	// 攻击方损失
		
		CastleBean castleFrom = CastleUtil.getCastleById(fromCid);
		if(castleFrom == null) {
			cacheService.deleteAttackThreadBean(id);
			return;
		}
		
		CastleUserBean toUser = null;
		CastleUserBean fromUser = CastleUtil.getCastleUser(castleFrom.getUid());
		HeroBean sendHero = null;
		
		CastleBean castleTo = null;
		UserResBean receiveUserRes = null;
		if(oasis.getCid() != 0) {
			castleTo = CastleUtil.getCastleById(oasis.getCid());
			toUser = CastleUtil.getCastleUser(castleTo.getUid());
			receiveUserRes = CastleUtil.getUserResBeanById(oasis.getCid());
		}
		

		
		SoldierSmithyBean[] fromSmithy = CastleBaseAction.getSmithys(fromCid);
		
		float receiveAllDefence = 0;
		List receiveArmyList = castleService.getOasisArmyAtList(oasis.getId());	// 所有支援等级都计算
		// 预先计算所有防御兵力的所在城堡，保存到armyList的每个army中
		for(int i = 0;i < receiveArmyList.size();i++) {
			CastleArmyBean army = (CastleArmyBean)receiveArmyList.get(i);
			CastleBean c = CastleUtil.getCastleById(army.getCid());
			army.setCastle(c);
			if(army.getCid() != 0)
				army.setUser(CastleUtil.getCastleUser(c.getUid()));
		}
		
		//计算所有攻击力和所有防御力
//		float[] defensePart = new float[receiveArmyList.size()];		// 所有支援的军队攻击力，最后换算为比例
		CastleArmyBean[] receiveAllArmy = new CastleArmyBean[6];	// 所有防御兵力总和
		CastleArmyBean[] receiveAllLost = new CastleArmyBean[6];	// 所有防御兵力损失总和
		if(type == 9) {
			sendAllAttack = ResNeed.calcScout(castleFrom.getRace(), soldiers, fromSmithy);
			for(int i = 0;i < receiveArmyList.size();i++) {
				CastleArmyBean army = (CastleArmyBean)receiveArmyList.get(i);
				int race = army.getCastle().getRace();
				SoldierSmithyBean[] receiveSmithy = CastleBaseAction.getSmithys(army.getCid());
				receiveAllDefence += army.calcScoutDefense(race, receiveSmithy);
				if(receiveAllArmy[race] == null)
					receiveAllArmy[race] = new CastleArmyBean();
				receiveAllArmy[race].mergeCount(army);
			}
		} else {
			sendAttacks = ResNeed.calcAttack(castleFrom.getRace(), soldiers, fromSmithy);
			
			if(hero > 0) {	// 带了英雄，但是英雄没有攻防升级
				sendHero = fromUser.getHero();
				if(sendHero != null) {
					sendAttacks[0] += sendHero.getAttack();
					sendAttacks[1] += sendHero.getAttack2();
					float attackX = sendHero.getAttackX();
					if(attackX != 0f) {
						attackX += 1f;
						sendAttacks[0] *= attackX;
						sendAttacks[1] *= attackX;
					}
				}
			}
			
			sendAllAttack = sendAttacks[0] + sendAttacks[1];

			for(int i = 0;i < receiveArmyList.size();i++) {
				CastleArmyBean army = (CastleArmyBean)receiveArmyList.get(i);
				int race = army.getCastle().getRace();
			
				SoldierSmithyBean[] receiveSmithy = CastleBaseAction.getSmithys(army.getCid());
				float[] defenses = army.calcDefense(race, receiveSmithy);
				
				if(army.getCid() != 0 && army.getHero() > 0) {
					HeroBean hero = army.getUser().getHero();
					if(hero != null) {
						defenses[0] += hero.getDefense();
						defenses[1] += hero.getDefense2();
						float defenseX = hero.getDefenseX();
						if(defenseX != 0f) {
							defenseX += 1f;
							defenses[0] *= defenseX;
							defenses[1] *= defenseX;
						}
					}
				}
				
				receiveDefences[0] += defenses[0];
				receiveDefences[1] += defenses[1];
				if(receiveAllArmy[race] == null)
					receiveAllArmy[race] = new CastleArmyBean();
				receiveAllArmy[race].mergeCount(army);
			}

			if(sendAllAttack != 0)	// 攻击力为0？
				receiveAllDefence += (receiveDefences[0] * sendAttacks[0] + receiveDefences[1] * sendAttacks[1])
						/ sendAllAttack;
			else
				receiveAllDefence += 100;	// 攻击力如果是0，防御就是无穷大
		}
		
		
		UserResBean sendUserRes = CastleUtil.getUserResBeanById(fromCid);
		
		// 计算金币商城的攻防效果
		if(sendUserRes.isFlagAttack())
			sendAllAttack *= 1.1f;
		
		SoldierResBean[] gsAttack = ResNeed.getSoldierTs(castleFrom.getRace());
		int sendDead = 0;	// 攻击方死亡的人口数
		int receiveDead = 0;	// 被攻击方死亡的人口数
		
		float sendLost = 0f;		// 进攻放损失的百分比
		float receiveLost = 0f;		// 防守方所有部队损失的百分比
		
		//如果攻击者的攻击大于被攻击者的防御，则攻下城堡
		// 注意，侦察的计算方式类似于抢夺，但是防御方不死伤
		if(sendAllAttack > receiveAllDefence) {		
			float factor = receiveAllDefence / sendAllAttack;
			factor = (float)Math.pow(factor, 1.5);

			if(type == 7) {
				sendLost = factor;
				receiveLost = 1.0f;
			} else {	// 抢夺
				sendLost = factor / (1 + factor);
				receiveLost = 1.0f / (1 + factor);	// 全部损失
			}

		} else if(receiveAllDefence > sendAllAttack) {	//如果被攻击者的防御大于攻击者的攻击，则攻不下城堡
			float factor = sendAllAttack / receiveAllDefence;
			factor = (float)Math.pow(factor, 1.5);
			
			if(type == 7) {
				sendLost = 1.0f;
				receiveLost = factor;		
			} else {	// 抢夺
				sendLost = 1.0f / (1 + factor);
				receiveLost = factor / (1 + factor);
			}
			
		} else 	if(receiveAllDefence == sendAllAttack) {	//如果被攻击人的防御和攻击者的攻击想同，则士兵全部死掉
			//减掉防守方失去的兵力，启动线程回城
			if(type == 7) {	
				sendLost = 1f;
				receiveLost = 1f;
			} else {	// 抢夺
				sendLost = 0.5f;
				receiveLost = 0.5f;
			}
		}
		
		int raidCount = 0;		// 抢夺的资源总量
		int leftCount = 0;	// 剩余军队
		if(sendLost != 0) {
			StringBuilder sbc = new StringBuilder(32);
			CastleArmyBean leftArmy = new CastleArmyBean();
			for(int i = 0;i < soldiers.size();i++) {
				int count = ((Integer)soldiers.get(i)).intValue();
				if(count > 0) {
					if(sendLost == 1.0f) {
						sendDead += gsAttack[i + 1].getPeople() * count;
						attackAllLost.setCount(i + 1, count);
						count = 0;
					} else {
						int dead = (int) Math.round(count * sendLost);
						sendDead += gsAttack[i + 1].getPeople() * dead;
						count -= dead;
						leftCount += count;
						attackAllLost.setCount(i + 1, dead);
					}
				}
				leftArmy.setCount(i + 1, count);
			}
			if(hero != 0) {
				if(sendHero != null) {
					float health = sendHero.getHealth(now);
					health -= sendLost * 100;
					if(health < 0.5f) {
						health = 0f;
						sendHero.setStatus(HeroBean.STATUS_DEAD);
						fromUser.deleteHero();
						attackAllLost.setHero(1);
						sendDead += ResNeed.heroGrainCost;
					} else {
						leftArmy.setHero(1);
						leftCount++;
					}
					sendHero.setHealth(health);
					castleService.updateHeroSimple(sendHero);
				}
			}
			
			if(leftCount > 0) {
				raidCount = ResNeed.calcStore(castleFrom.getRace(), leftArmy.getCount());
			}

			soldierCount = leftArmy.toString();
			hero = leftArmy.getHero();
		} else {
			raidCount = ResNeed.calcStore(castleFrom.getRace(), soldiers);
			leftCount = 1;
		}
		boolean receiveLeft = false;	// 防御方有剩下
		if(type == 9)		// 对于侦察，防守方兵力无损耗
			receiveLost = 0f;

		else if(receiveLost > 0f && receiveArmyList.size() > 0) {
			String content = "给绿洲的支援被攻击";
			float heroHealthHit = receiveLost * 100;	// 防御方英雄掉血是一样的
			List receiveHero = new ArrayList(4);
			for(int j = 0;j < receiveArmyList.size();j++) {
				CastleArmyBean army = (CastleArmyBean)receiveArmyList.get(j);
				int race = army.getCastle().getRace();
				
				SoldierResBean[] gs = ResNeed.getSoldierTs(race);
				
				if(receiveAllLost[race] == null)
					receiveAllLost[race] = new CastleArmyBean();
				
				int[] receiveCounts = army.getCount();
				int[] tmp = new int[ResNeed.soldierTypeCount + 1];
				int[] tmp2 = (int[])receiveCounts.clone();
				for (int i = 1; i < receiveCounts.length; i++) {
					int count = receiveCounts[i];
					if(count > 0) {
						int dead = (int) Math.round(count * receiveLost);
						receiveDead += gs[i].getPeople() * dead;
						receiveCounts[i] -= dead;
						if(receiveCounts[i] != 0)
							receiveLeft = true;
						receiveAllLost[race].addCount(i, dead);
						tmp[i] = dead;
					}
				}
				if(army.getHero() > 0) {
					HeroBean hero = army.getUser().getHero();
					if(hero != null) {
						receiveHero.add(hero);
						float health = hero.getHealth(now);
						health -= heroHealthHit;
						if(health < 0.5f) {
							health = 0f;
							hero.setStatus(HeroBean.STATUS_DEAD);
							army.getUser().deleteHero();
							
							receiveDead += ResNeed.heroGrainCost;
							receiveAllLost[race].setHero(1);
							receiveCounts[0] = 0;
							tmp[0] = 1;
						} else {
							
						}
						hero.setHealth(health);
						castleService.updateHeroSimple(hero);
					}
				}
				if(army.getCid() != 0 && army.isEmpty())
					castleService.deleteOasisArmyById(army.getId());	// 死光，记录删除
				else
					castleService.updateOasisSoldierCount(army);
				if(army.getCid() != toCid) {	// 如果toCid==0，应该不需要执行以下语句，因为没可能有援兵，但就算执行了也不要出错
					CastleBean rein = CastleUtil.getCastleById(army.getCid());
					if(toCid == 0 || rein.getUid() != castleTo.getUid()) {	// 如果是自己军队攻击，则不显示支援
						// 支援被攻击
						StringBuilder sb = new StringBuilder(100);
						sb.append("[支援方]");
						sb.append("<a href=\"search.jsp?cid=");
						sb.append(rein.getId());
						sb.append("\">");
						sb.append(rein.getCastleNameWml());
						sb.append("</a>支援");
						sb.append("<a href=\"search.jsp?pos=");
						sb.append(oasis.getId());
						sb.append("\">");
						sb.append("绿洲");
						sb.append("</a>");
						sb.append("<br/>兵力-损失:");
						CastleUtil.getSoldierLostString(army.getCastle().getRace(), tmp2, tmp, sb);
						CastleUtil.addDetailMsg(rein.getUid(), 4, content, sb.toString());
					}
				}
			}
			if(receiveHero.size() != 0) {
				int exp = sendDead / receiveHero.size();
				if(exp != 0) {
					DbOperation db = new DbOperation(5);
					for(int i = 0;i < receiveHero.size();i++) {
						HeroBean h = (HeroBean)receiveHero.get(i);
						h.addExp(exp);
						db.executeUpdate("update castle_hero set exp=" + h.getExp() + " where id=" + h.getId());
					}
					db.release();
				}
			}
			if(receiveDead > 0 && sendHero != null){
				sendHero.addExp(receiveDead);
				SqlUtil.executeUpdate("update castle_hero set exp=" + sendHero.getExp() + " where id=" + sendHero.getId(), 5);
			}
		}

		int robRes = 0;
//		 战报标题
		String content = castleFrom.getCastleNameWml() + typeNames[type];
		if(castleTo == null)
			content += "绿洲";
		else
			content += castleTo.getCastleNameWml() + "的绿洲";
		int pos = oasis.getId();
		
		if(type == 9) {	// 侦察
			if(leftCount == 0) {
				StringBuilder sb = new StringBuilder(100);
				sb.append("[攻击方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleFrom.getId());
				sb.append("\">");
				sb.append(castleFrom.getCastleNameWml());
				sb.append("</a><br/>");
				sb.append("兵力-损失:");
				CastleUtil.getSoldierLostString(castleFrom.getRace(), tmpCount, attackAllLost.getCount(), sb);
				sb.append("<br/>信息:你的士兵没有回来.<br/>");
				sb.append("[防御方]");
				sb.append("<a href=\"search.jsp?pos=");
				sb.append(oasis.getId());
				sb.append("\">");
				sb.append("绿洲");
				sb.append("</a><br/>");
				String detail = sb.toString();
				CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail + "兵力:未知", pos);
				if(castleFrom.getUid() != castleTo.getUid()) {		// 如果侦察方有损失，被侦察方会发现
					for(int i = 1;i <= 5;i++)
						if(receiveAllArmy[i] != null) {
							sb.append("兵力:");
							receiveAllArmy[i].getSoldierString(i, sb);
							sb.append("<br/>");
						}
					if(castleTo != null)
						CastleUtil.addDetailMsg(castleTo.getUid(), 2, content, sb.toString(), pos);
				}
				cacheService.deleteAttackThreadBean(id);
			} else {
				StringBuilder sb = new StringBuilder(100);
				sb.append("[攻击方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleFrom.getId());
				sb.append("\">");
				sb.append(castleFrom.getCastleNameWml());
				sb.append("</a><br/>");
				sb.append("兵力-损失:");
				CastleUtil.getSoldierLostString(castleFrom.getRace(), tmpCount, attackAllLost.getCount(), sb);
				sb.append("<br/>[防御方]");
				sb.append("<a href=\"search.jsp?pos=");
				sb.append(oasis.getId());
				sb.append("\">");
				sb.append("绿洲");
				sb.append("</a><br/>");
				for(int i = 1;i <= 5;i++)
					if(receiveAllArmy[i] != null) {
						sb.append("兵力:");
						receiveAllArmy[i].getSoldierString(i, sb);
						sb.append("<br/>");
					}
				
				sb.append("资源:");
				if(castleTo != null) {
					sb.append("木");
					sb.append(receiveUserRes.getWood(now) / 10);
					sb.append("石");
					sb.append(receiveUserRes.getStone(now) / 10);
					sb.append("铁");
					sb.append(receiveUserRes.getFe(now) / 10);
					sb.append("粮");
					sb.append(receiveUserRes.getGrain(now) / 10);	
				} else {
					sb.append("木");
					sb.append(oasis.getWood(now));
					sb.append("石");
					sb.append(oasis.getStone(now));
					sb.append("铁");
					sb.append(oasis.getFe(now));
					sb.append("粮");
					sb.append(oasis.getGrain(now));
				}

				String detail = sb.toString();
				CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail, pos);
				if(castleTo != null && sendLost != 0f && castleFrom.getUid() != castleTo.getUid()) {		// 如果侦察方有损失，被侦察方会发现
					CastleUtil.addDetailMsg(castleTo.getUid(), 2, content, detail, pos);
				}
				endTime = System.currentTimeMillis() + endTime - startTime;
				cacheService.cancelAttackThreadBean(this);
			}
		} else {
			if(leftCount == 0) {
				StringBuilder sb = new StringBuilder(100);
				sb.append("[攻击方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleFrom.getId());
				sb.append("\">");
				sb.append(castleFrom.getCastleNameWml());
				sb.append("</a><br/>");
				sb.append("兵力-损失:");
				CastleUtil.getSoldierLostString(castleFrom.getRace(), tmpCount, attackAllLost.getCount(), sb);
				sb.append("<br/>信息:你的士兵没有回来.");
				sb.append("<br/>[防御方]");
				sb.append("<a href=\"search.jsp?pos=");
				sb.append(oasis.getId());
				sb.append("\">");
				sb.append("绿洲");
				sb.append("</a><br/>");
				
				String detail = sb.toString();
				for(int i = 1;i <= 5;i++)
					if(receiveAllArmy[i] != null) {
						sb.append("兵力-损失:");
						if(receiveAllLost[i] != null)
							CastleUtil.getSoldierLostString(i, receiveAllArmy[i].getCount(), receiveAllLost[i].getCount(), sb);
						else
							CastleUtil.getSoldierNoLostString(i, receiveAllArmy[i].getCount(), sb);
						sb.append("<br/>");
					}
				String detail2 = sb.toString();
				
				if(sendAllAttack + sendAllAttack >= receiveAllDefence)
					CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail2, pos);
				else
					CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail + "兵力:未知", pos);
				if(castleTo != null && castleFrom.getUid() != castleTo.getUid()) {
					CastleUtil.addDetailMsg(castleTo.getUid(), 2, content, detail2, pos);
				}
				cacheService.deleteAttackThreadBean(id);
			} else {
				boolean occ = false;	//  占领
				// 占领绿洲!!!!!!!!!
				if((!receiveLeft || receiveAllDefence == 0) && castleFrom.getId() != oasis.getCid() 
						&& hero != 0) {	// 英雄不能死
					int dx = x - castleFrom.getX();
					int dy = y - castleFrom.getY();
					if(CastleUtil.inSquare(dx, dy, 3) &&
							(castleFrom.canExpand2(sendUserRes.getBuildingGrade(ResNeed.HERO_BUILD)))) {		// 占领
						DbOperation db = new DbOperation(5);
						if(oasis.getCid() != 0) {
							receiveUserRes.addOasis(oasisType, -25);
							castleTo.setExpand2(castleTo.getExpand2() - 1);
							db.executeUpdate("update castle set expand2=" + castleTo.getExpand2() + " where id=" + castleTo.getId());
							// 如果正在放弃绿洲
							db.executeUpdate("delete from cache_common where cid=" + castleTo.getId() + " and value=" + oasis.getId());
						}
						sendUserRes.addOasis(oasisType, 25);
						occ = true;
						castleFrom.setExpand2(castleFrom.getExpand2() + 1);
						db.executeUpdate("update castle set expand2=" + castleFrom.getExpand2() + " where id=" + castleFrom.getId());
						oasis.setCid(castleFrom.getId());
						db.executeUpdate("update castle_oasis set cid=" + castleFrom.getId() + ",uid=" + castleFrom.getUid() + " where id=" + oasis.getId());

						db.release();
					}
				}
				// 非侦察，抢夺资源
				SoldierResBean soldierRob;
				if(raidCount != 0) {
					if(castleTo != null) {		// 占领后无法抢夺资源
						if(toUser.getLockTime() >= now) {
							soldierRob = new SoldierResBean(receiveUserRes, oasis, raidCount);
							SqlUtil.executeUpdate("update castle_oasis set rob_time=" + oasis.getRobTime() + " where id=" + oasis.getId(), 5);
							//更新抢与被抢的资源
							wood = soldierRob.getWood();
							stone = soldierRob.getStone();
							fe = soldierRob.getFe();
							grain = soldierRob.getGrain();
							robRes = wood + stone + fe + grain;
	
							CastleUtil.decreaseUserRes(receiveUserRes, soldierRob.getWood(), soldierRob.getFe(), soldierRob.getGrain(), soldierRob.getStone());
						} else {
							soldierRob = new SoldierResBean();
						}
					} else {
						soldierRob = new SoldierResBean(oasis, raidCount);

						//更新抢与被抢的资源
						wood = soldierRob.getWood();
						stone = soldierRob.getStone();
						fe = soldierRob.getFe();
						grain = soldierRob.getGrain();
						robRes = wood + stone + fe + grain;

						CastleUtil.decreaseOasisRes(oasis, soldierRob.getWood(), soldierRob.getFe(), soldierRob.getGrain(), soldierRob.getStone());

					}
				} else {
					soldierRob = new SoldierResBean();
				}
				
				StringBuilder sb = new StringBuilder(100);
				sb.append("[攻击方]");
				sb.append("<a href=\"search.jsp?cid=");
				sb.append(castleFrom.getId());
				sb.append("\">");
				sb.append(castleFrom.getCastleNameWml());
				sb.append("</a><br/>");
				sb.append("兵力-损失:");
				CastleUtil.getSoldierLostString(castleFrom.getRace(), tmpCount, attackAllLost.getCount(), sb);

				sb.append("<br/>缴获:");
				sb.append("木");
				sb.append(soldierRob.getWood());
				sb.append("石");
				sb.append(soldierRob.getStone());
				sb.append("铁");
				sb.append(soldierRob.getFe());
				sb.append("粮");
				sb.append(soldierRob.getGrain());
				if(occ) {
					sb.append("<br/>信息:你占领了这片绿洲");
				}
				sb.append("<br/>[防御方]");
				sb.append("<a href=\"search.jsp?pos=");
				sb.append(oasis.getId());
				sb.append("\">");
				sb.append("绿洲");
				sb.append("</a><br/>");
				for(int i = 1;i <= 5;i++)
					if(receiveAllArmy[i] != null) {
						sb.append("兵力-损失:");
						if(receiveAllLost[i] != null)
							CastleUtil.getSoldierLostString(i, receiveAllArmy[i].getCount(), receiveAllLost[i].getCount(), sb);
						else
							CastleUtil.getSoldierNoLostString(i, receiveAllArmy[i].getCount(), sb);
						sb.append("<br/>");
					}
				String detail = sb.toString();
				
				CastleUtil.addDetailMsg(castleFrom.getUid(), 2, content, detail, pos);
				if(castleTo != null && castleFrom.getUid() != castleTo.getUid()) {
					CastleUtil.addDetailMsg(castleTo.getUid(), 2, content, detail, pos);
				}
				
				endTime = System.currentTimeMillis() + endTime - startTime;
				cacheService.cancelAttackThreadBean(this);
			}
		}
		
		if(sendDead > 0) {
			sendUserRes.addPeople2Calc(-sendDead, now);
			if(castleTo != null && castleTo.getUid() != castleFrom.getUid()) {
				// 防御总点数
				toUser.defenseTotal += sendDead;
				toUser.defenseWeek += sendDead;
				SqlUtil.executeUpdate("update castle_user set defense_total=" + toUser.defenseTotal + ",defense_week=" + toUser.defenseWeek + " where uid=" + toUser.getUid(), 5);
			}
		}
		if(receiveDead > 0 && receiveUserRes != null) {
			receiveUserRes.addPeople2Calc(-receiveDead, now);
		}
		if((receiveDead != 0 || robRes != 0) && (castleTo == null || castleTo.getUid() != castleFrom.getUid())) {
			// 加上攻击总点数，抢夺总点数
			fromUser.attackTotal += receiveDead;
			fromUser.robTotal += robRes;
			fromUser.attackWeek += receiveDead;
			fromUser.robWeek += robRes;
			SqlUtil.executeUpdate("update castle_user set attack_total=" + fromUser.attackTotal + ",attack_week=" + fromUser.attackWeek + ",rob_total=" + fromUser.robTotal + ",rob_week=" + fromUser.robWeek + " where uid=" + fromUser.getUid(), 5);
		}
	}
	
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getSoldierCount() {
		return soldierCount;
	}
	public List getSoldierCountList() {
		return toInts(soldierCount);
	}

	public void setSoldierCount(String soldierCount) {
		this.soldierCount = soldierCount;
	}
	
	public static List toInts(String ss) {
		List l = new ArrayList();
		if(ss != null) {
			String[] s = ss.split(",");
			for(int i = 0;i < s.length;i++) {
				int id = StringUtil.toInt(s[i]);
				if(id >= 0)
					l.add(Integer.valueOf(id));
			}
		}
		return l;
	}
	public static boolean addMsg2(String str1,String str2,CastleBean castle, int uid) {
		CastleUserBean user = CastleUtil.getCastleUserCache(uid);
		if(user != null)
			user.addUnread();
		
		return addMsg(str1, str2, castle, uid);
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeNames[type];
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getFromCid() {
		return fromCid;
	}

	public void setFromCid(int fromCid) {
		this.fromCid = fromCid;
	}

	public int getToCid() {
		return toCid;
	}

	public void setToCid(int toCid) {
		this.toCid = toCid;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public static CastleArmyBean zeroArmy = new CastleArmyBean();

	public int getFe() {
		return fe;
	}
	public void setFe(int fe) {
		this.fe = fe;
	}
	public int getGrain() {
		return grain;
	}
	public void setGrain(int grain) {
		this.grain = grain;
	}
	public int getStone() {
		return stone;
	}
	public void setStone(int stone) {
		this.stone = stone;
	}
	public int getWood() {
		return wood;
	}
	public void setWood(int wood) {
		this.wood = wood;
	}
	public int getOpt() {
		return opt;
	}
	public void setOpt(int opt) {
		this.opt = opt;
	}
	public int getHero() {
		return hero;
	}
	public void setHero(int hero) {
		this.hero = hero;
	}
}
