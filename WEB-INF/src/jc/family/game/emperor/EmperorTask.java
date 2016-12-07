package jc.family.game.emperor;

import java.util.TimerTask;

public class EmperorTask extends TimerTask {
	EmperorGameBean game;
	
	public EmperorGameBean getGame() {
		return game;
	}

	public void setGame(EmperorGameBean game) {
		this.game = game;
	}

	public void run() {
		try {
			game.calculate();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
