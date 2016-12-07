package net.joycool.wap.action.pet;

public class MatchUserBean {

	//昨日排名
	int id;
	//宠物id
	int pet_user;
	//昨日积分
	int yesterday;
	//宠物名称
	String name;
	//此人今天赢了多少乐币
	long totalstake;
	//此人今天得的冠军次数
	int wintime;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getPet_user() {
		return pet_user;
	}
	
	public void setPet_user(int pet_user) {
		this.pet_user = pet_user;
	}
	
	public int getYesterday() {
		return yesterday;
	}
	
	public void setYesterday(int yesterday) {
		this.yesterday = yesterday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTotalstake() {
		return totalstake;
	}

	public void setTotalstake(long totalstake) {
		this.totalstake = totalstake;
	}

	public int getWintime() {
		return wintime;
	}

	public void setWintime(int wintime) {
		this.wintime = wintime;
	}
	
}
