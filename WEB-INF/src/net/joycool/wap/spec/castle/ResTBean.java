package net.joycool.wap.spec.castle;


public class ResTBean {
	private int id;
	//有很多种建筑类型
	private int buildType;
	//当前建筑等级
	private int grade;
	private int people;
	
	//升级所需要的材料和时间,通过ResNeed类来计算
	private int wood;
	private int fe;
	private int grain;
	private int stone;
	private int time;
	
	public ResTBean(BuildingBean bean) {
		id = bean.getId();
		buildType = bean.getBuildType();
		grade = bean.getGrade();
		people = bean.getPeople();
	}
	
	public ResTBean(){}
	
	public ResTBean(ResTBean src, float factor){
		fe = (int) (src.getFe() * factor);
		wood = (int) (src.getWood() * factor);
		grain = (int) (src.getGrain() * factor);
		stone = (int)(src.getStone() * factor);
	}
		
	public ResTBean(int res1, int res2, int res3, int res4, int res5) {		
		wood = res1;
		fe = res2;
		grain = res3;
		stone = res4;
		time = res5;
	}
	
	public ResTBean(int res1, int res2, int res3, int res4, int time, float factor) {		
		wood = Math.round (res1 * factor * 0.2f) * 5;
		fe = Math.round (res2 * factor * 0.2f) * 5;
		grain = Math.round (res3 * factor * 0.2f) * 5;
		stone = Math.round (res4 * factor * 0.2f) * 5;
		this.time = Math.round(time * factor);
	}

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
}
