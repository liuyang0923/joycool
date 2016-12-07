package jc.family.game.pvz;

//从数据库读取出来的僵尸原型
public class ZombieProtoBean {
	int id;// 类型
	int type; // 0普通,1撑杆跳,2跳跳
	int attack;// 攻击力
	int attackspace;// 攻击范围
	int attackmun;// 攻击人数
	int attackCd;// 攻击间隔
	int maxHp;// 最大血量
	int price;// 价格
	int moveCd;// 移动间隔
	int buildCd; // 种植间隔
	String name;// 名字
	String shortName; // 简称
	String pic;// 图片
	String describe;// 描述
	long buildTime; // 下一次可种植时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAttack() {
		return attack;
	}

	public int getBuildCd() {
		return buildCd;
	}

	public void setBuildCd(int buildCd) {
		this.buildCd = buildCd;
	}

	public long getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(long buildTime) {
		this.buildTime = buildTime;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getAttackspace() {
		return attackspace;
	}

	public void setAttackspace(int attackspace) {
		this.attackspace = attackspace;
	}

	public int getAttackmun() {
		return attackmun;
	}

	public void setAttackmun(int attackmun) {
		this.attackmun = attackmun;
	}

	public int getAttackCd() {
		return attackCd;
	}

	public void setAttackCd(int attackCd) {
		this.attackCd = attackCd;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getMoveCd() {
		return moveCd;
	}

	public void setMoveCd(int moveCd) {
		this.moveCd = moveCd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
