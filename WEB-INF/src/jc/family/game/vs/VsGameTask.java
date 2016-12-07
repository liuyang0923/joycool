package jc.family.game.vs;

import java.util.TimerTask;

/**
 * 用来定时开始,结束游戏
 * 
 * @author qiuranke
 * 
 */
public class VsGameTask extends TimerTask {
	int vsGameid;
	int act; // 1 比赛开始 2 比赛结束

	public VsGameTask(int vsGameid) {
		this.vsGameid = vsGameid;
		act = 1;
	}

	public VsGameTask(int mid, int act) {
		this.vsGameid = mid;
		this.act = act;
	}

	public int getMid() {
		return vsGameid;
	}

	public void setMid(int mid) {
		this.vsGameid = mid;
	}

	public void run() {
		VsGameBean vsgame = VsAction.getVsGameById(vsGameid);
		switch (act) {
		case 1:
			vsgame.startGame();
			break;
		case 2:
			vsgame.endGame();
			break;
		}
	}
}
