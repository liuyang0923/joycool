package jc.family.game;

import java.util.TimerTask;

import net.joycool.wap.util.LogUtil;

import jc.family.game.boat.BoatAction;

/**
 * 用来定时开始,结束游戏
 * 
 * @author qiuranke
 * 
 */
public class FamilyGameTask extends TimerTask {
	int mid;
	int act; // 1 比赛开始 2 比赛结束 3 龙舟8秒 4 龙舟60秒suijishijian

	public FamilyGameTask(int mid) {
		this.mid = mid;
		act = 1;
	}

	public FamilyGameTask(int mid, int act) {
		this.mid = mid;
		this.act = act;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public void run() {
		LogUtil.logTime("FamilyGameTask*****start");
		switch (act) {
		case 1:
			GameAction.startGame(mid);
			break;
		case 2:
			GameAction.endGame(mid);
			break;
		case 3:
			BoatAction.operate(mid);
			break;
		case 4:
			BoatAction.operate2(mid);
			break;
		}
		LogUtil.logTime("FamilyGameTask*****end");
	}

}
