package jc.game.worldcup;

/**
 * 赛事基本信息
 * @author maning
 *
 */
public class WcInfo {
	int id;
	int limitTime;		// 开赛禁止投注时限。单位：分钟
	int prizeId;		// 奖项跳转ID
	int helpId;			// 说明跳转ID
	int subjectId;		// 专题ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}
	public int getPrizeId() {
		return prizeId;
	}
	public void setPrizeId(int prizeId) {
		this.prizeId = prizeId;
	}
	public int getHelpId() {
		return helpId;
	}
	public void setHelpId(int helpId) {
		this.helpId = helpId;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
}
