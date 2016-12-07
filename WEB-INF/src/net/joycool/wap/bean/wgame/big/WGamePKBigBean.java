/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.bean.wgame.big;

import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 * 
 */
public class WGamePKBigBean {
	// 要乐还要酷
	public static int YLHYK = 1;

	//
	public static int PK_MARK_FREE = 0;

	// 坐庄
	public static int PK_MARK_BKING = 1;

	// pk
	public static int PK_MARK_PKING = 2;

	// 结束
	public static int PK_MARK_END = 3;

	// 输
	public static int RESULT_MARK_LOSE = 0;

	// 赢
	public static int RESULT_MARK_WIN = 1;

	// 平局
	public static int RESULT_MARK_DRAW = 2;

	public static int MAX_BK_COUNT = 5;

	int id;

	int leftUserId;

	String leftNickname;

	String leftCardsStr;

	int rightUserId;

	String rightNickname;

	String rightCardsStr;

	String content;

	long wager;

	String startDatetime;

	String pkDatetime;

	String endDatetime;

	int mark;

	int winUserId;

	int gameId;

	int[] leftDices;

	int[] rightDices;

	int rightViewed;

	int leftViewed;

	int flag;	// 0 普通 1 大富豪骰子

	/**
	 * @return 返回 flag。
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            要设置的 flag。
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * @return Returns the rightViewed.
	 */
	public int getRightViewed() {
		return rightViewed;
	}

	/**
	 * @param rightViewed
	 *            The rightViewed to set.
	 */
	public void setRightViewed(int rightViewed) {
		this.rightViewed = rightViewed;
	}

	/**
	 * @return Returns the endDatetime.
	 */
	public String getEndDatetime() {
		return endDatetime;
	}

	/**
	 * @param endDatetime
	 *            The endDatetime to set.
	 */
	public void setEndDatetime(String endDatetime) {
		this.endDatetime = endDatetime;
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
	 * @return Returns the leftCardsStr.
	 */
	public String getLeftCardsStr() {
		return leftCardsStr;
	}
	
	public int getLeftCardsToInt() {
		return StringUtil.toInt(leftCardsStr);
	}

	/**
	 * @param leftCardsStr
	 *            The leftCardsStr to set.
	 */
	public void setLeftCardsStr(String leftCardsStr) {
		this.leftCardsStr = leftCardsStr;
	}

	/**
	 * @return Returns the leftNickname.
	 */
	public String getLeftNickname() {
		return leftNickname;
	}

	/**
	 * @param leftNickname
	 *            The leftNickname to set.
	 */
	public void setLeftNickname(String leftNickname) {
		this.leftNickname = leftNickname;
	}

	/**
	 * @return Returns the leftUserId.
	 */
	public int getLeftUserId() {
		return leftUserId;
	}

	/**
	 * @param leftUserId
	 *            The leftUserId to set.
	 */
	public void setLeftUserId(int leftUserId) {
		this.leftUserId = leftUserId;
	}

	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return Returns the pkDatetime.
	 */
	public String getPkDatetime() {
		return pkDatetime;
	}

	/**
	 * @param pkDatetime
	 *            The pkDatetime to set.
	 */
	public void setPkDatetime(String pkDatetime) {
		this.pkDatetime = pkDatetime;
	}

	/**
	 * @return Returns the rightCardsStr.
	 */
	public String getRightCardsStr() {
		return rightCardsStr;
	}

	/**
	 * @param rightCardsStr
	 *            The rightCardsStr to set.
	 */
	public void setRightCardsStr(String rightCardsStr) {
		this.rightCardsStr = rightCardsStr;
	}

	/**
	 * @return Returns the rightNickname.
	 */
	public String getRightNickname() {
		return rightNickname;
	}

	/**
	 * @param rightNickname
	 *            The rightNickname to set.
	 */
	public void setRightNickname(String rightNickname) {
		this.rightNickname = rightNickname;
	}

	/**
	 * @return Returns the rightUserId.
	 */
	public int getRightUserId() {
		return rightUserId;
	}

	/**
	 * @param rightUserId
	 *            The rightUserId to set.
	 */
	public void setRightUserId(int rightUserId) {
		this.rightUserId = rightUserId;
	}

	/**
	 * @return Returns the startDatetime.
	 */
	public String getStartDatetime() {
		return startDatetime;
	}

	/**
	 * @param startDatetime
	 *            The startDatetime to set.
	 */
	public void setStartDatetime(String startDatetime) {
		this.startDatetime = startDatetime;
	}

	/**
	 * @return Returns the wager.
	 */
	public long getWager() {
		return wager;
	}

	/**
	 * @param wager
	 *            The wager to set.
	 */
	public void setWager(long wager) {
		this.wager = wager;
	}

	/**
	 * @return Returns the winUserId.
	 */
	public int getWinUserId() {
		return winUserId;
	}

	/**
	 * @param winUserId
	 *            The winUserId to set.
	 */
	public void setWinUserId(int winUserId) {
		this.winUserId = winUserId;
	}

	/**
	 * @param leftDices
	 *            The leftDices to set.
	 */
	public void setLeftDices(int[] leftDices) {
		this.leftDices = leftDices;
	}

	/**
	 * @param rightDices
	 *            The rightDices to set.
	 */
	public void setRightDices(int[] rightDices) {
		this.rightDices = rightDices;
	}

	/**
	 * @return Returns the rightDices.
	 */
	public int[] getRightDices() {
		if (rightDices == null) {
			if (rightCardsStr != null) {
				String[] ds = rightCardsStr.split(",");
				if (ds != null && ds.length == 3) {
					rightDices = new int[3];
					rightDices[0] = Integer.parseInt(ds[0]);
					rightDices[1] = Integer.parseInt(ds[1]);
					rightDices[2] = Integer.parseInt(ds[2]);
				}
			}
		}
		return rightDices;
	}

	/**
	 * @return Returns the leftDices.
	 */
	public int[] getLeftDices() {
		if (leftDices == null) {
			if (leftCardsStr != null) {
				String[] ds = leftCardsStr.split(",");
				if (ds != null && ds.length == 3) {
					leftDices = new int[3];
					leftDices[0] = Integer.parseInt(ds[0]);
					leftDices[1] = Integer.parseInt(ds[1]);
					leftDices[2] = Integer.parseInt(ds[2]);
				}
			}
		}
		return leftDices;
	}

	/**
	 * @return Returns the leftViewed.
	 */
	public int getLeftViewed() {
		return leftViewed;
	}

	/**
	 * @param leftViewed
	 *            The leftViewed to set.
	 */
	public void setLeftViewed(int leftViewed) {
		this.leftViewed = leftViewed;
	}

	/**
	 * @return 返回 content。
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            要设置的 content。
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
