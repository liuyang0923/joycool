package net.joycool.wap.spec.castle;

import java.util.List;


public class BuildingTBean {
	public static int FLAG_REBUILD = (1 << 0);			// 可重复建造，当有满级的之后
	public static int FLAG_MAIN = (1 << 1);			// 只能在主城建造
	public static int FLAG_NOT_MAIN = (1 << 2);			// 只能在分城建造
	public static int FLAG_UNIQUE = (1 << 3);			// 所有城堡唯一
	public static int FLAG_NATAR = (1 << 4);			// 只能在纳塔村建造
	public static int FLAG_NOT_NATAR = (1 << 5);			// 不能在纳塔村建造
	public static int FLAG_ART = (1 << 6);			// 拥有神器后可以建造
	private int id;
	
	String name;
	String info;
	int maxGrade;
	String pre;
	List preList;
	//有很多种建筑类型
	private int buildType;
	//当前建筑等级
	private int grade;
	private int people;
	int totalPeople;		// 到该等级的总人口
	private int buildPos;
	
	//升级所需要的材料和时间,通过ResNeed类来计算
	private int wood;
	private int fe;
	private int grain;
	private int stone;
	private int time;
	int civil;
	int value;
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
	public boolean isFlagRebuild() {
		return (flag & FLAG_REBUILD) != 0;
	}
	public boolean isFlagMain() {
		return (flag & FLAG_MAIN) != 0;
	}
	public boolean isFlagNotMain() {
		return (flag & FLAG_NOT_MAIN) != 0;
	}
	public boolean isFlagUnique() {
		return (flag & FLAG_UNIQUE) != 0;
	}
	public boolean isFlagNatar() {
		return (flag & FLAG_NATAR) != 0;
	}
	public boolean isFlagNotNatar() {
		return (flag & FLAG_NOT_NATAR) != 0;
	}
	public boolean isFlagArt() {
		return (flag & FLAG_ART) != 0;
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public BuildingTBean(BuildingBean bean) {
		id = bean.getId();
		buildType = bean.getBuildType();
		grade = bean.getGrade();
		people = bean.getPeople();
		this.buildPos = bean.getBuildPos();
	}
	
	public BuildingTBean(){}
	
//	public BuildingTBean(int buildType, int grade, int buildPos) {
//		this.buildType = buildType;
//		this.grade = grade;
//		this.buildPos = buildPos;
//	}
	
//	public BuildingTBean(int buildType, int grade, int uid, int buildPos) {
//		this.buildType = buildType;
//		this.grade = grade;
//		this.buildPos = buildPos;
//	}
//	
//	public BuildingTBean(int buildType, int grade, int uid, int people, int buildPos) {
//		this.buildType = buildType;
//		this.grade = grade;
//		this.people = people;
//		this.buildPos = buildPos;
//	}
//	public BuildingTBean(int buildType, int grade, int people,int buildPos, int res1, int res2, int res3, int res4, int res5) {
//		this.buildType = buildType;
//		this.grade = grade;
//		this.people = people;
//		this.buildPos = buildPos;
//		
//		wood = res1;
//		fe = res2;
//		grain = res3;
//		time = res4;
//		stone = res5;
//	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public int getFe() {
		return fe;
	}

	public void setFe(int fe) {
		this.fe = fe;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getGrain() {
		return grain;
	}

	public void setGrain(int grain) {
		this.grain = grain;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public int getStone() {
		return stone;
	}

	public void setStone(int stone) {
		this.stone = stone;
	}

	public int getBuildPos() {
		return buildPos;
	}

	public void setBuildPos(int buildPos) {
		this.buildPos = buildPos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(int maxGrade) {
		this.maxGrade = maxGrade;
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

	public int getCivil() {
		return civil;
	}

	public void setCivil(int civil) {
		this.civil = civil;
	}
	public int getTotalPeople() {
		return totalPeople;
	}
	public void setTotalPeople(int totalPeople) {
		this.totalPeople = totalPeople;
	}
}
