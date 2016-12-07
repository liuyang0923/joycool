package net.joycool.wap.spec.castle;


public class BuildingBean {

	private int id;
	//有很多种建筑类型
	private int buildType;
	//当前建筑等级
	private int grade;
	private int cid;
	private int people;
	private int buildPos;
	
	public int getBuildPos() {
		return buildPos;
	}

	public void setBuildPos(int buildPos) {
		this.buildPos = buildPos;
	}

	public BuildingBean(){}
	
//	public BuildingBean(int buildType, int grade, int uid, int buildPos) {
//		this.buildType = buildType;
//		this.grade = grade;
//		this.uid = uid;
//		this.buildPos = buildPos;
//	}
//	
	public BuildingBean(int buildType, int grade, int cid, int people, int buildPos) {
		this.buildType = buildType;
		this.grade = grade;
		this.cid = cid;
		this.people = people;
		this.buildPos = buildPos;
	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}
}
