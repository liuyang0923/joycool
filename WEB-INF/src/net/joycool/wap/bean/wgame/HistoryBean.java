/*
 * Created on 2006-4-29
 *
 */
package net.joycool.wap.bean.wgame;

/**
 * @author lbj
 *  
 */
public class HistoryBean {
    int id;

    int userId;

    String logDate;	

    int gameType;		// pk? hall? free?

    int gameId;			// 游戏编号

    int winCount;		// 当天输赢
    int loseCount;
    int drawCount;
    
    int winTotal;		// 历史总输赢
    int loseTotal;
    int drawTotal;
    
    long money;			// 今日输赢
    long moneyTotal;

    public long getMoneyTotal() {
		return moneyTotal;
	}

	public void setMoneyTotal(long moneyTotal) {
		this.moneyTotal = moneyTotal;
	}

	/**
     * @return Returns the drawCount.
     */
    public int getDrawCount() {
        return drawCount;
    }

    /**
     * @param drawCount
     *            The drawCount to set.
     */
    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    /**
     * @return Returns the gameId.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * @param gameId
     *            The gameId to set.
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * @return Returns the gameType.
     */
    public int getGameType() {
        return gameType;
    }

    /**
     * @param gameType
     *            The gameType to set.
     */
    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returns the logDate.
     */
    public String getLogDate() {
        return logDate;
    }

    /**
     * @param logDate
     *            The logDate to set.
     */
    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    /**
     * @return Returns the loseCount.
     */
    public int getLoseCount() {
        return loseCount;
    }

    /**
     * @param loseCount
     *            The loseCount to set.
     */
    public void setLoseCount(int loseCount) {
        this.loseCount = loseCount;
    }

    /**
	 * @return 返回 money。
	 */
	public long getMoney() {
		return money;
	}

	/**
	 * @param money 要设置的 money。
	 */
	public void setMoney(long money) {
		this.money = money;
	}

	/**
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return Returns the winCount.
     */
    public int getWinCount() {
        return winCount;
    }

    /**
     * @param winCount
     *            The winCount to set.
     */
    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

	public int getDrawTotal() {
		return drawTotal;
	}

	public void setDrawTotal(int drawTotal) {
		this.drawTotal = drawTotal;
	}

	public int getLoseTotal() {
		return loseTotal;
	}

	public void setLoseTotal(int loseTotal) {
		this.loseTotal = loseTotal;
	}

	public int getWinTotal() {
		return winTotal;
	}

	public void setWinTotal(int winTotal) {
		this.winTotal = winTotal;
	}
}
