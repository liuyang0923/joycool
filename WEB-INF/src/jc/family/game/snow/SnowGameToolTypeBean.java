package jc.family.game.snow;

public class SnowGameToolTypeBean {

	private int id;
	private String tName;
	private int useTime;
	private int actionType;// 道具类别:1.投的2.清扫的
	private int spendTime;
	private int snowEffect;
	private int spendMoney;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tTame) {
		this.tName = tTame;
	}

	public int getUseTime() {
		return useTime;
	}

	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public int getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(int spendTime) {
		this.spendTime = spendTime;
	}

	public int getSnowEffect() {
		return snowEffect;
	}

	public void setSnowEffect(int snowEffect) {
		this.snowEffect = snowEffect;
	}

	public int getSpendMoney() {
		return spendMoney;
	}

	public void setSpendMoney(int spendMoney) {
		this.spendMoney = spendMoney;
	}

}
