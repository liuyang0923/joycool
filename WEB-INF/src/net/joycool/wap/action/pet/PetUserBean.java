package net.joycool.wap.action.pet;

/**
 * 
 * @author guiping
 *
 */
public class PetUserBean {

	int id = 0;// 宠物表id

	int user_id = 0; // 主人id

	int type = 0; // 宠物类型 1_乌龟 1_猫 1_狗

	String name = ""; // 宠物名称

	int sex = 0; // 宠物性别 false是女,true是男

	int age = 0; // 宠物年龄

	int exp = 0; // 经验
	
	int rank = 0; // 级别

	int health = 0; // 体力

	int hungry = 0; // 饥饿度

	int intel = 0; // 智商

	int strength = 0; // 力量
	
	int clear = 0; // 清洁

	int agile = 0; // 敏捷

	int tenacious = 0; // 声明顽强度

	int friend = 0; // 友好
	
	String createtime = "";
	
	boolean change = false; //数据库中的数据是否修改过

	long lasttime = 0;
	
	int spot = 0;//剩余升级点
	//游戏类型
	int matchtype = 0;
	//游戏id
	int matchid = 0;
	//游戏积分
	int integral = 0;
//	今天游戏积分
	int today = 0;
//	昨天游戏积分
	int yesterday = 0;
//	剩余积分点
	int leftintegral = 0;
//  用户宠物显示标志。0显示，1不显示	
	int mark =0;
	
	int order;	// 在比赛中的名次
	
	/**
	 * @return Returns the order.
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order The order to set.
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	public int getSpot() {
		return spot;
	}

	public void setSpot(int spot) {
		this.spot = spot;
	}

	public PetUserBean() {

	}

	public PetUserBean(int user_id, PetTypeBean petBean, int sex) {
		PetService server = new PetService();
		if (petBean != null) {
				this.setAge(petBean.getAge());
				this.setAgile(petBean.getAgile());
				this.setExp(0);
				this.setFriend(petBean.getFriend());
				this.setHealth(petBean.getFriend());
				this.setHungry(petBean.getHungry());
				this.setIntel(petBean.getIntel());
				this.setStrength(petBean.getStrength());
				this.setTenacious(petBean.getTenacious());
				this.setSpot(0);
				this.setClear(100);
				this.setType(petBean.getId());
			}
		this.setRank(1);
		this.setUser_id(user_id);
		this.setSex(sex);
		this.setName("我的宠物");
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		if(exp > 80000)
			exp = 80000;
		this.exp = exp;
		ifUpRank();
	}

	/**
	 * 
	 * @author liq
	 * @explain：判断是否升级
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void ifUpRank() {
		// 经验公式
		// (n*n + n)*50 + 100
		// 升级了
		if (exp > ((rank * rank + rank) * 50 + 100)) {
			rank ++;
			// 每升一级给10经验点
			spot += 10;
		}
	}
	
	public int getFriend() {
		return friend;
	}

	public void setFriend(int friend) {
		if(friend < 0)
			friend = 0;
		if(friend > 100)
			friend = 100;
		this.friend = friend;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if(health < 0)
			health = 0;
		if(health > 100)
			health = 100;
		this.health = health;
	}

	public int getHungry() {
		return hungry;
	}

	public void setHungry(int hungry) {
		if(hungry < 0)
			hungry = 0;
		if(hungry > 100)
			hungry = 100;
		this.hungry = hungry;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIntel() {
		return intel;
	}

	public void setIntel(int intel) {
		this.intel = intel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getAgile() {
		return agile;
	}

	public void setAgile(int agile) {
		this.agile = agile;
	}

	public int getTenacious() {
		return tenacious;
	}

	public void setTenacious(int tenacious) {
		this.tenacious = tenacious;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public long getLasttime() {
		return lasttime;
	}

	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}

	public int getClear() {
		return clear;
	}

	public void setClear(int clear) {
		if(clear < 0)
			clear = 0;
		if(clear > 100)
			clear = 100;
		this.clear = clear;
	}
	
	public float getExpMod(){
		if (friend < 50) {
			return 0.3f;
		} else if (friend < 80) {
			return 0.6f;
		} else {
			return 1f;
		}
	}

	public int getMatchid() {
		return matchid;
	}

	public void setMatchid(int matchid) {
		this.matchid = matchid;
	}

	public int getMatchtype() {
		return matchtype;
	}

	public void setMatchtype(int matchtype) {
		this.matchtype = matchtype;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getToday() {
		return today;
	}

	public void setToday(int today) {
		this.today = today;
	}

	public int getYesterday() {
		return yesterday;
	}

	public void setYesterday(int yesterday) {
		this.yesterday = yesterday;
	}

	public int getLeftintegral() {
		return leftintegral;
	}

	public void setLeftintegral(int leftintegral) {
		this.leftintegral = leftintegral;
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
		
}
