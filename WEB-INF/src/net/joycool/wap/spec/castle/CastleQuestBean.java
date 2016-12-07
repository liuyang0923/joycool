package net.joycool.wap.spec.castle;

/**
 * @author bomb
 *	新手任务
 */
public class CastleQuestBean {
	int id;
	String title;	//任务标题
	String startMsg;
	String endMsg;	// 任务结束语句
	int type;		// 任务类型
	int reward;		// 额外奖励
	int sp;			// 奖励n天的sp帐号
	int wood, stone, fe, grain, gold;	// 奖励资源和金币
	int value, value2, value3;		// 一个参数保留
	String rewardMsg;		// 奖励的描述
	String mission;			// 命令
	public String getMission() {
		return mission;
	}
	public void setMission(String mission) {
		this.mission = mission;
	}
	public String getRewardMsg() {
		return rewardMsg;
	}
	public void setRewardMsg(String rewardMsg) {
		this.rewardMsg = rewardMsg;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getEndMsg() {
		return endMsg;
	}
	public void setEndMsg(String endMsg) {
		this.endMsg = endMsg;
	}
	public int getFe() {
		return fe;
	}
	public void setFe(int fe) {
		this.fe = fe;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getGrain() {
		return grain;
	}
	public void setGrain(int grain) {
		this.grain = grain;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getReward() {
		return reward;
	}
	public void setReward(int reward) {
		this.reward = reward;
	}
	public String getStartMsg() {
		return startMsg;
	}
	public void setStartMsg(String startMsg) {
		this.startMsg = startMsg;
	}
	public int getStone() {
		return stone;
	}
	public void setStone(int stone) {
		this.stone = stone;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getWood() {
		return wood;
	}
	public void setWood(int wood) {
		this.wood = wood;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getValue2() {
		return value2;
	}
	public void setValue2(int value2) {
		this.value2 = value2;
	}
	public int getValue3() {
		return value3;
	}
	public void setValue3(int value3) {
		this.value3 = value3;
	}
	public int getSp() {
		return sp;
	}
	public void setSp(int sp) {
		this.sp = sp;
	}
}
