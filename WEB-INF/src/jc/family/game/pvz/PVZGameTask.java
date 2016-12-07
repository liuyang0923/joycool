package jc.family.game.pvz;

import java.util.TimerTask;

public class PVZGameTask extends TimerTask {
	PVZGameBean game;

	public PVZGameBean getGame() {
		return game;
	}

	public void setGame(PVZGameBean game) {
		this.game = game;
	}

	public void run() {
		try {
			game.calculate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
