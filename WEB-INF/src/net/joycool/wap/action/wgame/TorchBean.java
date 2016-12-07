package net.joycool.wap.action.wgame;

import java.util.*;


public class TorchBean {
	int id;
	List users = new LinkedList();		// 传递到过的玩家id
	String name;
	long startTime;
	int userId = 431;		// 在谁手里
	int userCount;		// 经过的玩家
	long life = 0;		// 火炬可以传递的时间
	
	// user count对应的火炬指数
	public float getPoint() {
		if(userCount < 10)
			return 0;
		
		return (float)Math.pow(userCount - 10, 0.7) / 2; 
	}
	
	public TorchBean() {
		name = "火炬";
		startTime = System.currentTimeMillis();
	}
	public TorchBean(String name, long start) {
		this.name = name;
		this.startTime = start;
	}
	
	public boolean isUser(int id) {
		return users.contains(Integer.valueOf(id));
	}
	
	public boolean isStart() {
		return userId > 0;
	}
	
	public List getUsers() {
		return users;
	}
	
	public synchronized void add(int id) {
		users.add(0, Integer.valueOf(id));
		userId = id;
		if(users.size() > 50)
			users.remove(users.size() - 1);
	}
	
	public synchronized void clear() {
		users.clear();
	}
	
	public synchronized void adds(List list) {
		users.addAll(list);
		if(list.size() > 0)
			userId = ((Integer)list.get(list.size() - 1)).intValue();
	}
	public boolean isOver() {
		return startTime + life < System.currentTimeMillis();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public long getLife() {
		return life;
	}
	public void setLife(long life) {
		this.life = life;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public void addUserCount() {
		userCount++;
	}
}
