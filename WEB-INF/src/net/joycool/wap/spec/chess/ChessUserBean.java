package net.joycool.wap.spec.chess;

import java.util.*;

/**
 * @author zhouj
 * @explain： 中国象棋玩家数据
 * @datetime:1007-10-24
 */
public class ChessUserBean {
	int userId;
	int win;
	int lose;
	int draw;
	int flee;		// 逃跑次数
	int point;		// 积分，初始100
	int current;	// 当前局id，保存chessId
	long createTime;
	
	public ChessUserBean() {
		
	}
	// 初始化，创建一个玩家数据的时候调用
	public ChessUserBean(int userId) {
		point = 1500;
		this.userId = userId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getFlee() {
		return flee;
	}
	public void setFlee(int flee) {
		this.flee = flee;
	}
	public int getLose() {
		return lose;
	}
	public void setLose(int lose) {
		this.lose = lose;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
}
