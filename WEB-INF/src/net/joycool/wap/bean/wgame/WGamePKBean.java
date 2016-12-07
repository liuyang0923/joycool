/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.bean.wgame;

import java.util.Vector;

/**
 * @author lbj
 * 
 */
public class WGamePKBean {
	public static int DICE = 1;

	public static int GONG3 = 2;

	public static int JSB = 3;

	public static int FOOTBALL = 4;

	// 2007.4.2 liq 篮球游戏
	public static int BASKETBALL = 6;

	// 2007.4.2 liq 老虎杠子鸡
	public static int LGJ = 7;

	public static int PK_MARK_FREE = 0;

	public static int PK_MARK_BKING = 1;

	public static int PK_MARK_PKING = 2;

	public static int PK_MARK_END = 3;

	public static int RESULT_MARK_LOSE = 0;

	public static int RESULT_MARK_WIN = 1;

	public static int RESULT_MARK_DRAW = 2;

	int id;

	int leftUserId;

	String leftNickname;

	String leftCardsStr;

	int rightUserId;

	String rightNickname;

	String rightCardsStr;

	int wager;

	String startDatetime;

	String pkDatetime;

	String endDatetime;

	int mark;

	int winUserId;

	int gameId;

	int[] leftDices;

	int[] rightDices;

	Vector leftGong3s;

	Vector rightGong3s;

	int rightViewed;

	int leftViewed;

	int flag;

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
	 * @return Returns the leftGong3s.
	 */
	public Vector getLeftGong3s() {
		if (leftGong3s == null) {
			if (leftCardsStr != null) {
				String[] pai = leftCardsStr.split(",");
				if (pai != null && pai.length == 3) {
					String[] a = pai[0].split("_");
					int aa = Integer.parseInt(a[0]);
					int bb = Integer.parseInt(a[1]);
					String[] b = pai[1].split("_");
					int cc = Integer.parseInt(b[0]);
					int dd = Integer.parseInt(b[1]);
					String[] c = pai[2].split("_");
					int ee = Integer.parseInt(c[0]);
					int ff = Integer.parseInt(c[1]);
					CardBean card1 = new CardBean(aa, bb,
							"http://wap.joycool.net/wgame/cardImg/" + aa + "_"
									+ bb + ".gif");
					CardBean card2 = new CardBean(cc, dd,
							"http://wap.joycool.net/wgame/cardImg/" + cc + "_"
									+ dd + ".gif");
					CardBean card3 = new CardBean(ee, ff,
							"http://wap.joycool.net/wgame/cardImg/" + ee + "_"
									+ ff + ".gif");
					leftGong3s = new Vector();
					leftGong3s.add(card1);
					leftGong3s.add(card2);
					leftGong3s.add(card3);
				}
			}
		}
		return leftGong3s;
	}

	/**
	 * @param leftGong3s
	 *            The leftGong3s to set.
	 */
	public void setLeftGong3s(Vector leftGong3s) {
		this.leftGong3s = leftGong3s;
	}

	/**
	 * @return Returns the rightGong3s.
	 */
	public Vector getRightGong3s() {
		if (rightGong3s == null) {
			if (rightCardsStr != null) {
				String[] pai = rightCardsStr.split(",");
				if (pai != null && pai.length == 3) {
					String[] a = pai[0].split("_");
					int aa = Integer.parseInt(a[0]);
					int bb = Integer.parseInt(a[1]);
					String[] b = pai[1].split("_");
					int cc = Integer.parseInt(b[0]);
					int dd = Integer.parseInt(b[1]);
					String[] c = pai[2].split("_");
					int ee = Integer.parseInt(c[0]);
					int ff = Integer.parseInt(c[1]);
					CardBean card1 = new CardBean(aa, bb,
							"http://wap.joycool.net/wgame/cardImg/" + aa + "_"
									+ bb + ".gif");
					CardBean card2 = new CardBean(cc, dd,
							"http://wap.joycool.net/wgame/cardImg/" + cc + "_"
									+ dd + ".gif");
					CardBean card3 = new CardBean(ee, ff,
							"http://wap.joycool.net/wgame/cardImg/" + ee + "_"
									+ ff + ".gif");
					rightGong3s = new Vector();
					rightGong3s.add(card1);
					rightGong3s.add(card2);
					rightGong3s.add(card3);
				}
			}
		}
		return rightGong3s;
	}

	/**
	 * @param rightGong3s
	 *            The rightGong3s to set.
	 */
	public void setRightGong3s(Vector rightGong3s) {
		this.rightGong3s = rightGong3s;
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
	public int getWager() {
		return wager;
	}

	/**
	 * @param wager
	 *            The wager to set.
	 */
	public void setWager(int wager) {
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
}
