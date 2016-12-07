package jc.family.game.vs;

public class VsConfig {

	int id;
	String name; // 游戏名
	int preTime; // 开始时间
	int enterTime; // 停止进入时间
	int maxTime; // 结束时间
	int hide; // 隐藏
	int flag; // 权限
	int minPlayer; // 人数下限
	int maxPlayer; // 人数上限
	String gameClass; // 游戏类
	int columnId; // 说明页ID
	int matchScoreCount; // 获得积分场数
	int matchCount; // 总场数
	Class game;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPreTime() {
		return preTime;
	}

	public void setPreTime(int preTime) {
		this.preTime = preTime;
	}

	public int getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(int enterTime) {
		this.enterTime = enterTime;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public int getHide() {
		return hide;
	}

	public void setHide(int hide) {
		this.hide = hide;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getMinPlayer() {
		return minPlayer;
	}

	public void setMinPlayer(int minPlayer) {
		this.minPlayer = minPlayer;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}

	public String getGameClass() {
		return gameClass;
	}

	public void setGameClass(String gameClass) {
		this.gameClass = gameClass;
		try {
			game = Class.forName(gameClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}

	public int getMatchScoreCount() {
		return matchScoreCount;
	}

	public void setMatchScoreCount(int matchScoreCount) {
		this.matchScoreCount = matchScoreCount;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public Object createGame() {
		if (game != null) {
			try {
				return game.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
