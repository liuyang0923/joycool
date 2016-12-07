package net.joycool.wap.bean.dummy;
import java.sql.Timestamp;
import java.util.List;

import net.joycool.wap.util.StringUtil;
/**
 * @author gp
 * @datetime 2007-7-11 下午01:40:49
 * @explain 虚拟物品表
 */
public class DummyProductBean {
	public static int CLASS_WEAPON = 1;		// 武器
	public static int CLASS_ARMOR = 2;			// 护甲
	public static int CLASS2_KNIFE = 1;		// 刀
	public static int CLASS2_SWORD = 2;		// 剑
	public static int CLASS2_STICK = 3;		// 棍子
	public static int CLASS2_CHEST = 11;		// 胸甲
	public static int CLASS2_HEAD = 12;			// 帽子
	public static int CLASS2_HANDS = 13;		// 手套
	public static int CLASS2_FEET = 14;			// 鞋子
	public static int CLASS2_FINGER = 15;		// 戒指
	public static int CLASS2_LEG = 16;			// 腿部
	public static int CLASS2_WRIST = 17;		// 护腕
	public static int CLASS2_SHOULDER = 18;		// 肩部
	public static int CLASS2_BACK = 19;		// 披风
	public static int CLASS2_TRINKET = 20;		// 饰品
	
	static int FLAG_SHOP = (1 << 0);		// 神秘商店可见（如果有需要填写buyPrice）
	static int FLAG_QUEST = (1 << 1);		// 任务物品
	static int FLAG_HAVE_QUEST = (1 << 2);	// 可触发任务
	static int FLAG_COLLECT = (1 << 3);	// 收藏品
	static int FLAG_NO_DROP = (1 << 4);	// nodrop
	static int FLAG_AUCTION = (1 << 5);	// 允许拍卖
	
	public static String[] flagString = {"神秘商店", "任务物品", "触发任务", "收藏品", "非掉落", "允许拍卖"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	
	public static String[] gradeSymbol = {"", "A", "B", "C", "D", "E", "F", "G", "H"};
	public String getGradeSymbol() {
		return gradeSymbol[grade];
	}
	public static String[] gradeString = {"其他", "粗糙", "普通", "优质", "精良", "卓越", "极品", "传说", "逆天"};
	public String getGradeName() {
		return gradeString[grade];
	}
	
	public static String[] typeString = {"", "[家具]", "[整人道具]", "", "", "[合成卡]", "", "", "", "", "", "", "", "", ""};
	public String getTypeName() {
		return typeString[dummyId];
	}
	
	public static String[] class2String = {
		"", "单手武器", "", "", "", "", "", "", "", "",
		"", "衣服", "帽子", "手套", "鞋子", "戒指", "项链", "裤子", "披风", "",
		"", "药材", "", "", "", "", "", "", "", "",
		"", "水晶", "符文", "", "", "", "", "", "", "",};
	public String getClass2Name() {
		return class2String[class2];
	}
	
	int id;

	String name;

	String introduction;

	int mode;

	int value;

	int time = 1;		// 每个可以使用的次数，可以堆叠的物品(stack>1)应该为1，如果是装备，则是耐久度

	int dummyId;

	int price;		//	系统回收价格

	String description;	// 描述
	
    int mark;		// 对应flag，物品的特殊标志，1表示在神秘商店可以购买
    
    Timestamp startTime;	// 神秘商店上次购买的时间
    
    int brush;		// 神秘商店刷新时间
    
    int buyPrice;	// 神秘商店购买价格
    
    int bind = 1;	// 1是绑定，0是不绑定，绑定后无法给别人或者拍卖，2是装备后绑定
    
    int due;	// 期限，0为无限制
    int rank;	// 可以使用的等级，还未有效
    int stack = 1;	// 可以堆叠的数量，默认1
    int unique;	// 物品唯一，一人无法同时拥有两个
    int cooldown;	// 冷却时间
    int seq;		// 某些地方的排序
    
    int class1;		// 大分类
    int class2;		// 小分类（不同大分类下的小分类id也不同）
    String attribute;		// 装备属性
    List attributeList;
    String usage;		// 使用效果
    List usageList;
    
    int grade;		// 物品颜色等级
    
    public void init() {
    	attributeList = StringUtil.toIntss(attribute);
    	usageList = StringUtil.toIntss(usage);
    }
    
	/**
	 * @return Returns the cooldown.
	 */
	public int getCooldown() {
		return cooldown;
	}

	/**
	 * @param cooldown The cooldown to set.
	 */
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	/**
	 * @return Returns the unique.
	 */
	public int getUnique() {
		return unique;
	}

	/**
	 * @param unique The unique to set.
	 */
	public void setUnique(int unique) {
		this.unique = unique;
	}

	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return Returns the bind.
	 */
	public int getBind() {
		return bind;
	}

	/**
	 * @param bind The bind to set.
	 */
	public void setBind(int bind) {
		this.bind = bind;
	}
	
	
	public int getFlag() {
		return mark;
	}
	public void setFlag(int flag) {
		this.mark = flag;
	}
	public boolean isFlag(int is) {
		return (mark & (1 << is)) != 0;
	}
	public boolean isFlagCollect() {
		return (mark & FLAG_COLLECT) != 0;
	}
	public boolean isFlagNoDrop() {
		return (mark & FLAG_NO_DROP) != 0;
	}
	public boolean isFlagQuest() {
		return (mark & FLAG_QUEST) != 0;
	}
	public boolean isFlagAuction() {
		return (mark & FLAG_AUCTION) != 0;
	}
	/**
	 * @return Returns the due.
	 */
	public int getDue() {
		return due;
	}

	/**
	 * @param due The due to set.
	 */
	public void setDue(int due) {
		this.due = due;
	}

	/**
	 * @return Returns the buyPrice.
	 */
	public int getBuyPrice() {
		return buyPrice;
	}

	/**
	 * @param buyPrice The buyPrice to set.
	 */
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}

	/**
	 * @return Returns the brush.
	 */
	public int getBrush() {
		return brush;
	}

	/**
	 * @param brush The brush to set.
	 */
	public void setBrush(int brush) {
		this.brush = brush;
	}

	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return Returns the startTime.
	 */
	public Timestamp getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime The startTime to set.
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return dummyId
	 */
	public int getDummyId() {
		return dummyId;
	}

	/**
	 * @param dummyId
	 *            要设置的 dummyId
	 */
	public void setDummyId(int dummyId) {
		this.dummyId = dummyId;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return introduction
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * @param introduction
	 *            要设置的 introduction
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            要设置的 price
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time
	 *            要设置的 time
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            要设置的 mode
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            要设置的 value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isBind() {
		return bind == 1;
	}

	/**
	 * @return Returns the stack.
	 */
	public int getStack() {
		return stack;
	}

	/**
	 * @param stack The stack to set.
	 */
	public void setStack(int stack) {
		this.stack = stack;
	}
	
	// 能否叠起来
	public boolean canStack() {
		return stack > 1;
	}
	public boolean isUnique() {
		return unique == 1;
	}

	public String getTimeString() {
		if(time > 1) {
			if(time >= 100)
				return "(??)";
			else
				return "(" + time + ")";
		}
		return "";
	}

	public boolean isEquipBind() {
		return bind == 2;
	}

	/**
	 * @return Returns the seq.
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq The seq to set.
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getClass1() {
		return class1;
	}

	public void setClass1(int class1) {
		this.class1 = class1;
	}

	public int getClass2() {
		return class2;
	}

	public void setClass2(int class2) {
		this.class2 = class2;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public List getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List attributeList) {
		this.attributeList = attributeList;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public List getUsageList() {
		return usageList;
	}

	public void setUsageList(List usageList) {
		this.usageList = usageList;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
}
