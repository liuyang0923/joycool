package net.joycool.wap.spec.garden.flower;

public class FlowerBean {
	int  id;
	int  type;
	String name;
	int state;
	int price;
	String compose;
	int time;
	int compTime;
	int successExp;
	int failExp;
	int growExp;
	
	public int getGrowExp() {
		return growExp;
	}
	public void setGrowExp(int growExp) {
		this.growExp = growExp;
	}
	public int getCompTime() {
		return compTime;
	}
	public void setCompTime(int compTime) {
		this.compTime = compTime;
	}

	public int getSuccessExp() {
		return successExp;
	}
	public void setSuccessExp(int successExp) {
		this.successExp = successExp;
	}
	public int getFailExp() {
		return failExp;
	}
	public void setFailExp(int failExp) {
		this.failExp = failExp;
	}
	public FlowerBean() {
		
	}
	public FlowerBean(int type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getState() {
		return state;
	}
	public void setState(int count) {
		this.state = count;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCompose() {
		return compose;
	}
	public void setCompose(String compose) {
		this.compose = compose;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}