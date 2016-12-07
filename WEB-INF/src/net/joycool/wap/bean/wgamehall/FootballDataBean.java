/*
 * Created on 2006-3-9
 *
 */
package net.joycool.wap.bean.wgamehall;

/**
 * @author lbj
 *  
 */
public class FootballDataBean {
    public static int ACTION_KICK = 0;

    public static int ACTION_SAVE = 9;

    public static int ACTION_GIVE_UP = 1;

    public static int ACTION_SUE_FOR_PEACE = 2;

    public static int ACTION_DENY_PEACE = 6;

    public static int ACTION_INVITE = 3;

    public static int ACTION_ACCEPT_INVITATION = 4;

    public static int ACTION_DENY_INVITATION = 5;
    
    public static int GOAL = 1;
    
    public static int SAVED = 2;

    long lastActiveTime;

    int lastActiveUserId;

    String uniqueMark;

    int actionType;

    int sueForPeaceUserId1;

    int sueForPeaceUserId2;

    int leftRound;

    int rightRound;

    int leftWinCount;

    int rightWinCount;
    
    String historyKick;
    
    String historySave;

    String lastKick;

    String lastSave;        

    int[] leftResults;

    int[] rightResults;

    /**
     * @return Returns the historyKick.
     */
    public String getHistoryKick() {
        return historyKick;
    }
    /**
     * @param historyKick The historyKick to set.
     */
    public void setHistoryKick(String historyKick) {
        this.historyKick = historyKick;
    }
    /**
     * @return Returns the historySave.
     */
    public String getHistorySave() {
        return historySave;
    }
    /**
     * @param historySave The historySave to set.
     */
    public void setHistorySave(String historySave) {
        this.historySave = historySave;
    }
    /**
     * @return Returns the leftResults.
     */
    public int[] getLeftResults() {
        return leftResults;
    }

    /**
     * @param leftResults
     *            The leftResults to set.
     */
    public void setLeftResults(int[] leftResults) {
        this.leftResults = leftResults;
    }

    /**
     * @return Returns the rightResults.
     */
    public int[] getRightResults() {
        return rightResults;
    }

    /**
     * @param rightResults
     *            The rightResults to set.
     */
    public void setRightResults(int[] rightResults) {
        this.rightResults = rightResults;
    }

    /**
     * @return Returns the leftRound.
     */
    public int getLeftRound() {
        return leftRound;
    }

    /**
     * @param leftRound
     *            The leftRound to set.
     */
    public void setLeftRound(int leftRound) {
        this.leftRound = leftRound;
    }

    /**
     * @return Returns the rightRound.
     */
    public int getRightRound() {
        return rightRound;
    }

    /**
     * @param rightRound
     *            The rightRound to set.
     */
    public void setRightRound(int rightRound) {
        this.rightRound = rightRound;
    }

    /**
     * @return Returns the lastKick.
     */
    public String getLastKick() {
        return lastKick;
    }

    /**
     * @param lastKick
     *            The lastKick to set.
     */
    public void setLastKick(String lastKick) {
        this.lastKick = lastKick;
    }

    /**
     * @return Returns the lastSave.
     */
    public String getLastSave() {
        return lastSave;
    }

    /**
     * @param lastSave
     *            The lastSave to set.
     */
    public void setLastSave(String lastSave) {
        this.lastSave = lastSave;
    }

    /**
     * @return Returns the leftWinCount.
     */
    public int getLeftWinCount() {
        return leftWinCount;
    }

    /**
     * @param leftWinCount
     *            The leftWinCount to set.
     */
    public void setLeftWinCount(int leftWinCount) {
        this.leftWinCount = leftWinCount;
    }

    /**
     * @return Returns the rightWinCount.
     */
    public int getRightWinCount() {
        return rightWinCount;
    }

    /**
     * @param rightWinCount
     *            The rightWinCount to set.
     */
    public void setRightWinCount(int rightWinCount) {
        this.rightWinCount = rightWinCount;
    }

    /**
     * @return Returns the sueForPeaceUserId1.
     */
    public int getSueForPeaceUserId1() {
        return sueForPeaceUserId1;
    }

    /**
     * @param sueForPeaceUserId1
     *            The sueForPeaceUserId1 to set.
     */
    public void setSueForPeaceUserId1(int sueForPeaceUserId1) {
        this.sueForPeaceUserId1 = sueForPeaceUserId1;
    }

    /**
     * @return Returns the sueForPeaceUserId2.
     */
    public int getSueForPeaceUserId2() {
        return sueForPeaceUserId2;
    }

    /**
     * @param sueForPeaceUserId2
     *            The sueForPeaceUserId2 to set.
     */
    public void setSueForPeaceUserId2(int sueForPeaceUserId2) {
        this.sueForPeaceUserId2 = sueForPeaceUserId2;
    }

    /**
     * @return Returns the actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * @param actionType
     *            The actionType to set.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public FootballDataBean() {
        leftResults = new int[10];
        rightResults = new int[10];
    }

    /**
     * @return Returns the uniqueMark.
     */
    public String getUniqueMark() {
        return uniqueMark;
    }

    /**
     * @param uniqueMark
     *            The uniqueMark to set.
     */
    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    /**
     * @return Returns the lastActiveTime.
     */
    public long getLastActiveTime() {
        return lastActiveTime;
    }

    /**
     * @param lastActiveTime
     *            The lastActiveTime to set.
     */
    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    /**
     * @return Returns the lastActiveUserId.
     */
    public int getLastActiveUserId() {
        return lastActiveUserId;
    }

    /**
     * @param lastActiveUserId
     *            The lastActiveUserId to set.
     */
    public void setLastActiveUserId(int lastActiveUserId) {
        this.lastActiveUserId = lastActiveUserId;
    }
}
