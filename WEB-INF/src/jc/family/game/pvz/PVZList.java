package jc.family.game.pvz;

import java.util.LinkedList;

public class PVZList extends LinkedList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String[] name = { "空地", "大门", "攻陷", "单个", "多个" };

	/**
	 * 空地
	 */
	static int stateNull = 0;
	/**
	 * 大门
	 */
	static int stateDoor = 1;
	/**
	 * 结束
	 */
	static int stateOver = 2;
	/**
	 * 一个植物或僵尸
	 */
	static int stateOnly = 3;
	/**
	 * 多个植物或僵尸
	 */
	static int stateMore = 4;

	int state;// 是什么东西

	PVZ pvza;

	PVZList(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String toString() {
		return super.toString();
	}

}
