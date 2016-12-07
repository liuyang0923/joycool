package net.joycool.wap.spec.castle;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;

//城堡指挥官
public class HeroBean {
	public static int STATUS_ALIVE = 0;
	public static int STATUS_DEAD = 1;
	public static int STATUS_REVIVE = 2;
	public static int STATUS_TEAINING = 3;
	int id;
	int uid;	//  所属玩家
	String name = "指挥官";	//  玩家给英雄
	long createTime;
	
	int race;	// 种族，不保存在数据库
	int stat1;
	int stat2;
	int stat3;
	int stat4;
	int stat5;
	int exp;		//  经验值
	int rank;	// 计算得到的等级，不保存到数据库

	float health = 100f;	// 生命
	long time;	// 用于计算生命
	
	int status = STATUS_ALIVE;		// 状态，1 死亡 0 正常 2 正在复活
	public boolean isAlive() {
		return status == STATUS_ALIVE;
	}
	public boolean isDead() {
		return status == STATUS_DEAD;
	}
	public boolean isRevive() {
		return status == STATUS_REVIVE;
	}
	public boolean isTraining() {
		return status == STATUS_TEAINING;
	}
	
	
	int type;		// 兵种类型 1－11
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public float getHealth() {
		return health;
	}
	public int getStat5() {
		return stat5;
	}
	public void setStat5(int stat5) {
		this.stat5 = stat5;
	}
	public void setHealth(float health) {
		this.health = health;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStat1() {
		return stat1;
	}
	public void setStat1(int stat1) {
		this.stat1 = stat1;
	}
	public int getStat2() {
		return stat2;
	}
	public void setStat2(int stat2) {
		this.stat2 = stat2;
	}
	public int getStat3() {
		return stat3;
	}
	public void setStat3(int stat3) {
		this.stat3 = stat3;
	}
	public int getStat4() {
		return stat4;
	}
	public void setStat4(int stat4) {
		this.stat4 = stat4;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getExp() {
		return exp;
	}
	
	public void addExp(int add) {
		exp += add;
		calcRank();
	}
	public void addExpDB(int add) {
		addExp(add);
		SqlUtil.executeUpdate("update castle_hero set exp=" + exp + " where id=" + id, 5);
	}
	public void setExp(int exp) {
		this.exp = exp;
		calcRank();
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	// 剩余可以加的点数
	public int getFreePoint() {
		return rank * 5 - stat1 - stat2 - stat3 - stat4 - stat5 + 5;
	}
	public void calcRank() {
		if(rank >= 99)	// 最高99级
			return;
		for(int i = rank + 1;i < rankExp.length;i++) {
			if(exp >= rankExp[i])
				rank++;
			else
				return;
		}
	}
	public static int[] rankExp = new int[100];
	static {
		for(int i = 1;i < 100;i++) {
			rankExp[i] = rankExp[i - 1] + i * 100;
		}
	}
	SoldierResBean heroSoldier = null;
	public SoldierResBean getHeroSoldier() {
		if(heroSoldier == null)
			heroSoldier = ResNeed.getSoldierRes(race, type);
		return heroSoldier;
	}
	
	public int getAttack() {
		getHeroSoldier();
		if(heroSoldier.getAttack() == 0)
			return 0;
		return Math.round((0.4f * heroSoldier.getAttack() / 3 + 5.5f) * stat1 + 0.25f * heroSoldier.getAttack()) * 5;
	}
	public int getAttack2() {
		getHeroSoldier();
		if(heroSoldier.getAttack2() == 0)
			return 0;
		return Math.round((0.4f * heroSoldier.getAttack2() / 3 + 5.5f) * stat1 + 0.25f * heroSoldier.getAttack2()) * 5;
	}
	
	public int getDefense() {
		getHeroSoldier();
		float corr = (float) Math.pow((float) heroSoldier.getDefense() / heroSoldier.getDefense2(), 0.2f);
		return Math.round((0.4f * heroSoldier.getDefense() / 3 + 5.5f * corr) * stat2 + heroSoldier.getDefense() / 3) * 5;
	}
	public int getDefense2() {
		getHeroSoldier();
		float corr = (float) Math.pow((float) heroSoldier.getDefense() / heroSoldier.getDefense2(), 0.2f);
		return Math.round((0.4f * heroSoldier.getDefense2() / 3 + 5.5f / corr) * stat2 + heroSoldier.getDefense2() / 3) * 5;
	}
	// int[5] = wood stone fe grain time/s
	public int[] getReviveRes() {
		getHeroSoldier();
		return getReviveRes(heroSoldier, rank);
	}
	public static int[] getReviveRes(SoldierResBean heroSoldier, int rank) {
		int lvl = Math.min(rank, 60) + 1;
		int[] res = {heroSoldier.getWood(), heroSoldier.getStone(), heroSoldier.getFe(),
				heroSoldier.getGrain(), heroSoldier.getTime()};
		res[4] = res[4] * 2 * lvl;
		
		float coeff = (float)Math.pow(lvl, 1.25f);
		for(int i = 0;i < 4;i++) {
			if (rank > 0)
				res[i] += 30;
			res[i] = trickyRounding(2 * res[i] * coeff);
		}
		
		return res;
	}
	public static int trickyRounding(float value) {
		int round_size = 100;
		if (value < 1000) round_size = 10;
		if (value >= 10000) round_size = 500;
		if (value > 240000) value = 240000;
		return Math.round(value / round_size) * round_size;
	}
	
	public float getAttackX() {
		return stat3 * 0.002f;
	}
	public float getDefenseX() {
		return stat4 * 0.002f;
	}
	public int getHealthSpeed() {
		return stat5 * 5;
	}
	// 根据stat1到stat5重新计算攻防数据
	public void reCalc(long now) {
		health = getHealth(now);
		time = now;
	}
	public int getRace() {
		return race;
	}
	public void setRace(int race) {
		this.race = race;
	}
	public float getHealth(long now) {
		float days = (float)((now - time)) / DateUtil.MS_IN_DAY;
		
		float cur = health + (int)(getHealthSpeed() * days);
		
		if(cur > 100f)
			return 100f;
		return cur;
	}
	public static int exp2Rank(int exp) {
		int rank = 0;
		for(rank = 0;rank < 99;rank++) {
			if(exp < rankExp[rank + 1])
				return rank;
		}
		return rank;
	}
	// 返回经验值百分比
	public int getExpPercent() {
		if(rank >= 99)
			return 0;
		return (exp - rankExp[rank]) * 100 / (rankExp[rank + 1] - rankExp[rank]);
	}
}
