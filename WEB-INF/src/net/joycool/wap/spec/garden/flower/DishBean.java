package net.joycool.wap.spec.garden.flower;

public class DishBean {
	int id;
	int userId;			//用户名
	int cultureDish1;	//一号培养皿
	int cultureDish2;	//二......
	int cultureDish3;	//三......
	int cultureDish4;	//四......
	int state;
	long time;
	boolean isSpecGoodsUsed = false;
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCultureDish1() {
		return cultureDish1;
	}
	public void setCultureDish1(int cultureDish1) {
		this.cultureDish1 = cultureDish1;
	}
	public int getCultureDish2() {
		return cultureDish2;
	}
	public void setCultureDish2(int cultureDish2) {
		this.cultureDish2 = cultureDish2;
	}
	public int getCultureDish3() {
		return cultureDish3;
	}
	public void setCultureDish3(int cultureDish3) {
		this.cultureDish3 = cultureDish3;
	}
	public int getCultureDish4() {
		return cultureDish4;
	}
	public void setCultureDish4(int cultureDish4) {
		this.cultureDish4 = cultureDish4;
	}
	public void clearDish(){
		cultureDish1 = 0;
		cultureDish2 = 0;
		cultureDish3 = 0;
		cultureDish4 = 0;
		state = 0;
		time = -28800000;
		isSpecGoodsUsed = false;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isSpecGoodsUsed() {
		return isSpecGoodsUsed;
	}
	public void setSpecGoodsUsed(boolean isSpecGoodsUsed) {
		this.isSpecGoodsUsed = isSpecGoodsUsed;
	}
}
