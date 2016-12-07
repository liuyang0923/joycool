package jc.news.nba;

public class BeanMatch {
	private int id;
	private int del;
	private int flag;
	private int part;
	private int sumReply;
	private int sumLive;
	private int support1;
	private int support2;
	private int staticValue;
	private String code;
	private String team1;
	private String team2;
	private long createTime;
	private long startTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getStaticValue() {
		return staticValue;
	}

	public void setStaticValue(int staticValue) {
		this.staticValue = staticValue;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getSupport1() {
		return support1;
	}

	public void setSupport1(int support1) {
		this.support1 = support1;
	}

	public int getSupport2() {
		return support2;
	}

	public void setSupport2(int support2) {
		this.support2 = support2;
	}

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getSumReply() {
		return sumReply;
	}

	public void setSumReply(int sumReply) {
		this.sumReply = sumReply;
	}

	public int getSumLive() {
		return sumLive;
	}

	public void setSumLive(int sumLive) {
		this.sumLive = sumLive;
	}
}
