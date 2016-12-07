package jc.family;

/**
 * 积分
 * @author qiuranke
 *
 */
public class FmScore {

	int id;
	int askScore;
	int boatScore;
	int snowScore;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFamilyName() {
		if (id == 0) {
			return "";
		}
		FamilyHomeBean fm = FamilyAction.getFmByID(id);
		if (fm == null) {
			return "";
		}
		return fm.getFm_nameWml();
	}

	public int getAskScore() {
		return askScore;
	}

	public void setAskScore(int askScore) {
		this.askScore = askScore;
	}

	public int getBoatScore() {
		return boatScore;
	}

	public void setBoatScore(int boatScore) {
		this.boatScore = boatScore;
	}

	public int getSnowScore() {
		return snowScore;
	}

	public void setSnowScore(int snowScore) {
		this.snowScore = snowScore;
	}

	/**
	 * @param a
	 * @return
	 */
	public int getScore(int a) {
		if (a == 2) {
			return askScore;
		}
		if (a == 3) {
			return boatScore;
		}
		return snowScore;
	}
}