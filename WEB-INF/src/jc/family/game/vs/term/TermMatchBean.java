package jc.family.game.vs.term;

public class TermMatchBean {

	int id;
	int termId;
	int challengeId;
	long startTime;
	int fmidA;
	int fmidB;
	int wager;
	int state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTermId() {
		return termId;
	}

	public void setTermId(int termId) {
		this.termId = termId;
	}

	public int getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}


	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getFmidA() {
		return fmidA;
	}

	public void setFmidA(int fmidA) {
		this.fmidA = fmidA;
	}

	public int getFmidB() {
		return fmidB;
	}

	public void setFmidB(int fmidB) {
		this.fmidB = fmidB;
	}

	public int getWager() {
		return wager;
	}

	public void setWager(int wager) {
		this.wager = wager;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
