package jc.game.texas;

/**
 * @author bombzj
 * @datetime 2010-1-3 
 * @explain 德州扑克里每个玩家的个人资料
 */
public class TexasUserBean {
	public static int MAX_RANK = 13;
	public static String[] rankNames = {"临时工", "正式工", "助理主管", "主管", "助理经理", "经理", "助理总裁", "总裁", "董事", "董事长", "赌王", "赌圣", "赌神"};
	public static int[] rankExps = {10000, 20000, 40000, 80000, 160000, 320000, 640000, 1280000, 2560000, 5120000, 10000000, 20000000, 40000000, 80000000, 160000000, 320000000};
	public static String getRankName(int rank) {
		return rankNames[rank];
	}
	public String getRankName() {
		return rankNames[rank];
	}
	
	
	int userId;
	int money;		// 积分
	int rank;		// 根据积分算的等级
	int money2;		// 乐币兑换的积分？
	
	long moneyTime;		// 上一次领取救济的时间
	int prizeCount;		// 领取救济的次数，最多领取?次
	
	int boardId = -1;	// 当前在哪个桌子
	long createTime;
	// 把积分换算为等级
	public int m2r(int pt) {
		return 0;
	}
	public void addMoney(int add) {
		money += add;
		if(money < 0)
			money = 0;
		rank = m2r(money);
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
		rank = m2r(money);
	}
	public int getMoney2() {
		return money2;
	}
	public void setMoney2(int money2) {
		this.money2 = money2;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public long getMoneyTime() {
		return moneyTime;
	}
	public void setMoneyTime(long moneyTime) {
		this.moneyTime = moneyTime;
	}
	public int getPrizeCount() {
		return prizeCount;
	}
	public void setPrizeCount(int prizeCount) {
		this.prizeCount = prizeCount;
	}
}
