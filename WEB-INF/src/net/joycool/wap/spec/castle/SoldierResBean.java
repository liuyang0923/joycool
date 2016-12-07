package net.joycool.wap.spec.castle;

import java.util.*;

public class SoldierResBean {
	public static int FLAG_RESEARCHED = (1 << 0);			// 不需要研发
	public static int FLAG_NO_UPGRADE = (1 << 1);			// 无法升级攻防
	int id;
	int type;
	String soldierName;
	String shortName;
	String info;
	int wood;
	int fe;
	int grain;
	int stone;
	int time;
	float speed;
	int attack;		// 近程攻击
	int defense;
	int attack2;	// 远程攻击
	int defense2;
	int attack3;	// 侦察能力
	int defense3;
	int attack4;	// 攻城能力或者摧毁城墙
	int defense4;
	int store;		//士兵能抢夺的资源
	int people;	// 占的人口（粮食消耗）
	// 研发该兵种需要的数据
	int wood2;
	int fe2;
	int grain2;
	int stone2;
	int time2;
	// 升级攻防需要的基础资源，时间直接使用time2
	int wood3;
	int fe3;
	int grain3;
	int stone3;
	String pre;		// 研发要求
	List preList;
	int buildType;		// 哪种建筑可以训练
	int flag;
	int race;		// 种族

	public String getRaceName() {
		return ResNeed.raceNames[race];
	}
	public int getRace() {
		return race;
	}
	public void setRace(int race) {
		this.race = race;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagResearched() {
		return (flag & FLAG_RESEARCHED) != 0;
	}
	public boolean isFlagNoUpgrade() {
		return (flag & FLAG_NO_UPGRADE) != 0;
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public SoldierResBean(){}
	
	/**
	 * 
	 * @param userRes
	 * @param protect	受保护的资源
	 * @param sendStore 士兵能掠夺的总量
	 */
	public SoldierResBean(UserResBean userRes, int protect, int sendStore) {
		long now = System.currentTimeMillis();
		wood = (int)(userRes.getWood(now)- protect);
		grain = (int)(userRes.getGrain(now) - protect);
		fe = (int)(userRes.getFe(now) - protect);
		stone = (int)(userRes.getStone(now) - protect);
		if(wood < 0) wood=0;
		if(grain < 0) grain=0;
		if(fe < 0) fe=0;
		if(stone < 0) stone=0;
		int sum = wood + grain + fe + stone;
		if(sendStore < sum) {
			float rate = (float)sendStore / sum;
			wood *= rate;
			fe *= rate;
			grain *= rate;
			stone *= rate;
		} else if(sendStore > sum) {
			// 退还在市场卖出的订单
			MerchantAction.cancel(userRes);
			// 重新计算
			wood = (int)(userRes.getWood(now)- protect);
			grain = (int)(userRes.getGrain(now) - protect);
			fe = (int)(userRes.getFe(now) - protect);
			stone = (int)(userRes.getStone(now) - protect);
			if(wood < 0) wood=0;
			if(grain < 0) grain=0;
			if(fe < 0) fe=0;
			if(stone < 0) stone=0;
			sum = wood + grain + fe + stone;
			if(sendStore < sum) {
				float rate = (float)sendStore / sum;
				wood *= rate;
				fe *= rate;
				grain *= rate;
				stone *= rate;
			}
		}
	}
	// 未占领的绿洲
	public SoldierResBean(OasisBean oasis, int sendStore) {
		if(sendStore == 0)
			return;
		long now = System.currentTimeMillis();
		wood = (int)(oasis.getWood(now));
		grain = (int)(oasis.getGrain(now));
		fe = (int)(oasis.getFe(now));
		stone = (int)(oasis.getStone(now));
		if(wood < 0) wood=0;
		if(grain < 0) grain=0;
		if(fe < 0) fe=0;
		if(stone < 0) stone=0;
		int sum = wood + grain + fe + stone;
		if(sendStore < sum) {
			float rate = (float)sendStore / sum;
			wood *= rate;
			fe *= rate;
			grain *= rate;
			stone *= rate;
		}
	}
	public static int ROB_TIME_INTERVAL = 6000000;
	// 已占领的绿洲
	public SoldierResBean(UserResBean userRes, OasisBean oasis, int sendStore) {
		if(sendStore == 0)
			return;
		long now = System.currentTimeMillis();
		float add = (float)(now - oasis.getRobTime()) / ROB_TIME_INTERVAL;
		if(add > 0.1f)
			add = 0.1f;

		wood = (int)(userRes.getWood(now) * add);
		grain = (int)(userRes.getGrain(now) * add);
		fe = (int)(userRes.getFe(now) * add);
		stone = (int)(userRes.getStone(now) * add);
		if(wood < 0) wood=0;
		if(grain < 0) grain=0;
		if(fe < 0) fe=0;
		if(stone < 0) stone=0;
		int sum = wood + grain + fe + stone;
		if(sendStore < sum) {
			float rate = (float)sendStore / sum;
			wood *= rate;
			fe *= rate;
			grain *= rate;
			stone *= rate;
			oasis.setRobTime((long) (now - ROB_TIME_INTERVAL * add * (1 - rate)));
		} else {
			oasis.setRobTime(now);
		}
	}
	
	public int getAttack2() {
		return attack2;
	}
	public void setAttack2(int attack2) {
		this.attack2 = attack2;
	}
	public int getDefense2() {
		return defense2;
	}
	public void setDefense2(int defense2) {
		this.defense2 = defense2;
	}
	public int getStore() {
		return store;
	}
	public void setStore(int store) {
		this.store = store;
	}
	public int getWood() {
		return wood;
	}
	public void setWood(int wood) {
		this.wood = wood;
	}
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
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSoldierName() {
		return soldierName;
	}
	public void setSoldierName(String soldierName) {
		this.soldierName = soldierName;
	}
	public int getStone() {
		return stone;
	}
	public void setStone(int stone) {
		this.stone = stone;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getFe2() {
		return fe2;
	}

	public void setFe2(int fe2) {
		this.fe2 = fe2;
	}

	public int getGrain2() {
		return grain2;
	}

	public void setGrain2(int grain2) {
		this.grain2 = grain2;
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public List getPreList() {
		return preList;
	}

	public void setPreList(List preList) {
		this.preList = preList;
	}

	public int getStone2() {
		return stone2;
	}

	public void setStone2(int stone2) {
		this.stone2 = stone2;
	}

	public int getTime2() {
		return time2;
	}

	public void setTime2(int time2) {
		this.time2 = time2;
	}

	public int getWood2() {
		return wood2;
	}

	public void setWood2(int wood2) {
		this.wood2 = wood2;
	}

	public int getFe3() {
		return fe3;
	}

	public void setFe3(int fe3) {
		this.fe3 = fe3;
	}

	public int getGrain3() {
		return grain3;
	}

	public void setGrain3(int grain3) {
		this.grain3 = grain3;
	}

	public int getStone3() {
		return stone3;
	}

	public void setStone3(int stone3) {
		this.stone3 = stone3;
	}

	public int getWood3() {
		return wood3;
	}

	public void setWood3(int wood3) {
		this.wood3 = wood3;
	}
	public int getAttack3() {
		return attack3;
	}
	public void setAttack3(int attack3) {
		this.attack3 = attack3;
	}
	public int getAttack4() {
		return attack4;
	}
	public void setAttack4(int attack4) {
		this.attack4 = attack4;
	}
	public int getDefense3() {
		return defense3;
	}
	public void setDefense3(int defense3) {
		this.defense3 = defense3;
	}
	public int getDefense4() {
		return defense4;
	}
	public void setDefense4(int defense4) {
		this.defense4 = defense4;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
