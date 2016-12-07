package jc.family.game.fruit;

import java.util.TimerTask;

/**
 * 
 * 用于水果大战的一些线程
 * @author guigui
 *
 */
public class FruitTask extends TimerTask {
	
	OrchardBean o1;
	OrchardBean o2;
	int userID;
	int fruitCount;
	FruitGameBean game;
	int taskID;
	int arrivalTime;
	String trends;
	
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}


	public OrchardBean getO1() {
		return o1;
	}

	public void setO1(OrchardBean o1) {
		this.o1 = o1;
	}

	public OrchardBean getO2() {
		return o2;
	}

	public void setO2(OrchardBean o2) {
		this.o2 = o2;
	}

	public String getTrends() {
		return trends;
	}

	public void setTrends(String trends) {
		this.trends = trends;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getFruitCount() {
		return fruitCount;
	}

	public void setFruitCount(int fruitCount) {
		this.fruitCount = fruitCount;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	private long startTime;	// 发兵的时间
	
	
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public FruitTask(OrchardBean o1, OrchardBean o2 ,FruitGameBean game,int fruitCount,int arrivalTime,int userID){
		this.o1 = o1;
		this.o2 = o2;
		this.fruitCount = fruitCount;
		this.game = game;
		this.taskID = game.getTrendId();
		this.startTime = System.currentTimeMillis();
		this.arrivalTime = arrivalTime;
		this.userID = userID;
	}

	public FruitGameBean getGame() {
		return game;
	}

	public void setGame(FruitGameBean game) {
		this.game = game;
	}

	public void run() {
		// 从map里移掉 
		game.getFruitHitsMode().remove(new Integer(this.taskID));
		try{
			game.getAttack(o1, o2,fruitCount,userID);
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
}
