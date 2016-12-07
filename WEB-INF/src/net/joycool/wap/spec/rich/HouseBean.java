package net.joycool.wap.spec.rich;

import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 大富翁
 * @datetime:1007-10-24
 */
public class HouseBean {
	public static int MAX_HOUSE_LEVEL = 5;	// 房子最高级别
	public static int MAX_PASSBY = 5;		// 最多能经过的人次
	
	RichUserBean owner = null;		// 所属玩家, null表示不属于任何人 
	int level = 0;		// 房屋级别 0 - 5
	int type = 0;		// 0 住宅 1 研究所 2 连锁超市 3 饭店
	int passby = 0;		// 有人踩过的次数，暂时已经废弃
	public static String levelNames[][] = {
		{"住宅建地", "1级住宅", "2级住宅", "3级住宅", "4级住宅", "5级住宅"},
		{"商业用地", "1级研究所", "2级研究所", "3级研究所", "4级研究所", "5级研究所"},
		{"商业用地", "1级连锁超市", "2级连锁超市", "3级连锁超市", "4级连锁超市", "5级连锁超市"},
		{"商业用地", "1级饭店", "2级饭店", "3级饭店", "4级饭店", "5级饭店"},
	};
	public static int[] rentPrices = {1,3,5,10,15,25};		// 不同等级对应的租金价格系数，0-5
	public static int buyPrice = 6	;		// 购买需要的价格系数
	
	public String getLevelName() {
		return levelNames[type][level];
	}

	public void reset() {
		level = 0;
		owner = null;
		if(type > 0)		// 如果是商业建筑，随机产生一个
			type = RandomUtil.nextInt(3) + 1;
	}
	/**
	 * @return Returns the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level The level to set.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isMaxPassby() {
		return passby >= MAX_PASSBY;
	}
	public void addPassby() {
		passby++;
	}
	public int getPassby() {
		return passby;
	}

	/**
	 * @param passby The passby to set.
	 */
	public void setPassby(int passby) {
		this.passby = passby;
	}

	public void addLevel(int value) {
		level += value;
		if(level > MAX_HOUSE_LEVEL)
			level = MAX_HOUSE_LEVEL;
		else if(level < 0)
			level = 0;
	}
	public void addLevel() {
		if(level < MAX_HOUSE_LEVEL)
			level++;
	}

	public boolean noOwner() {
		return owner == null;
	}
	public boolean isMaxLevel() {
		return level >= MAX_HOUSE_LEVEL;
	}

	/**
	 * @return Returns the owner.
	 */
	public RichUserBean getOwner() {
		return owner;
	}

	/**
	 * @param owner The owner to set.
	 */
	public void setOwner(RichUserBean owner) {
		this.owner = owner;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	
}
