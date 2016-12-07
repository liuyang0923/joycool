package net.joycool.wap.action.pet;

public class MatchStakeBean {

	int id = 0;
	//投注人的id
    int user_id  = 0;
//  投注人的姓名
    String user_name;
    //投注对象,宠物id
    int pet_id = 0;
// 宠物的姓名
    String pet_name;
    //投注金额
    long stake = 0;
    

    
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getPet_id() {
		return pet_id;
	}
	
	public void setPet_id(int pet_id) {
		this.pet_id = pet_id;
	}
	
	public long getStake() {
		return stake;
	}
	
	public void setStake(long stake) {
		this.stake = stake;
	}
	
	public int getUser_id() {
		return user_id;
	}
	
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getPet_name() {
		return pet_name;
	}

	public void setPet_name(String pet_name) {
		this.pet_name = pet_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
}
