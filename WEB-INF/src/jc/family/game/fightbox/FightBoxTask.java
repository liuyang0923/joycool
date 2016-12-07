package jc.family.game.fightbox;

import java.util.TimerTask;

/**
 * 20秒方块大决斗
 * 
 * 
 */
public class FightBoxTask extends TimerTask {
	BoxGameBean game;

	public BoxGameBean getGame() {
		return game;
	}

	public void setGame(BoxGameBean game) {
		this.game = game;
	}

	public void run() {
		try {
			game.caculate();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
