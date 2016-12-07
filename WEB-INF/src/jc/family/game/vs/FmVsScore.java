package jc.family.game.vs;

public class FmVsScore {

	int fmId;
	int gameId;
	int win;
	int lose;
	int tie;
	int score;
	int vsTime;

	public FmVsScore() {

	}

	public FmVsScore(int fmId, int gameId) {
		this.fmId = fmId;
		this.gameId = gameId;
		this.score = 100;
	}

	public int getFmId() {
		return fmId;
	}

	public void setFmId(int fmId) {
		this.fmId = fmId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	public int getTie() {
		return tie;
	}

	public void setTie(int tie) {
		this.tie = tie;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getVsTime() {
		return vsTime;
	}

	public void setVsTime(int vsTime) {
		this.vsTime = vsTime;
	}

}
