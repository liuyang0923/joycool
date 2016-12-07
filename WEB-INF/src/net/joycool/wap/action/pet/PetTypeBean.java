package net.joycool.wap.action.pet;

/**
 * @author liq
 * @explain： 乐宠
 * @datetime:2007-5-31
 */
public class PetTypeBean {
	 
	int id;	//宠物类型  1_乌龟 1_猫 1_狗
	int health;	//体力
	int intel;	//智商
	int strength;	//力量
	int agile;	//敏捷
	int tenacious;	//声明顽强度
	int friend;	//友好
	int age;	//宠物年龄
	int exp;	 //级别
	int hungry;	 //饥饿度 
	String name;	 //宠物名称
	int price;	//价格
	String image;	//图片位置
	int al;	//升级点
	int in;	//升级点
	int st;	//升级点
	int mark;	//标记位 0,普通宠物 1,特殊宠物
	String shot;	//简短介绍
	String lon;	//详细介绍
	
	public PetTypeBean(){
		
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAgile() {
		return agile;
	}

	public void setAgile(int agile) {
		this.agile = agile;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getFriend() {
		return friend;
	}

	public void setFriend(int friend) {
		this.friend = friend;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHungry() {
		return hungry;
	}

	public void setHungry(int hungry) {
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

	public int getTenacious() {
		return tenacious;
	}

	public void setTenacious(int tenacious) {
		this.tenacious = tenacious;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getAl() {
		return al;
	}

	public void setAl(int al) {
		this.al = al;
	}

	public int getIn() {
		return in;
	}

	public void setIn(int in) {
		this.in = in;
	}

	public int getSt() {
		return st;
	}

	public void setSt(int st) {
		this.st = st;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getShot() {
		return shot;
	}

	public void setShot(String shot) {
		this.shot = shot;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

}