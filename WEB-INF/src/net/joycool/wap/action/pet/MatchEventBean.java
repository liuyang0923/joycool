package net.joycool.wap.action.pet;

public class MatchEventBean {

	int id;
	
	int gameid;
	
	float factor;
	
	String description;

	//随机事件的概率
	public static  int  PROBABILITY = 20;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getFactor() {
		return factor;
	}

	public void setFactor(float factor) {
		this.factor = factor;
	}

	public int getGameid() {
		return gameid;
	}

	public void setGameid(int gameid) {
		this.gameid = gameid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
