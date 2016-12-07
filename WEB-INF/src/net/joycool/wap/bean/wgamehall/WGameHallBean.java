/*
 * Created on 2006-3-6
 *
 */
package net.joycool.wap.bean.wgamehall;

/**
 * @author lbj
 *  
 */
public class WGameHallBean {
    public static int GOBANG = 1;
    
    public static int OTHELLO = 2;
    
    public static int FOOTBALL = 3;
    
    //赌局状态，邀请中/游戏中/结束
    public static int GS_WAITING = 0;

    public static int GS_PLAYING = 1;

    public static int GS_END = 2;   
    
    //赌局的用户状态，就绪/未就绪
    public static int PS_NOT_READY = 0;
    
    public static int PS_READY = 1;
    
    public static int PS_EXIT = 2;
    
    int id;

    int leftUserId;

    String leftNickname;

    int rightUserId;

    String rightNickname;

    String startDatetime;

    String endDatetime;

    int leftStatus;

    int rightStatus;

    int winUserId;

    int leftViewed;

    int rightViewed;

    int mark;

    String leftSessionId;

    String rightSessionId;

    int wager;

    String uniqueMark;

    String result;

    int gameId;

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
     * @return Returns the result.
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result
     *            The result to set.
     */
    public void setResult(String result) {
        this.result = result;
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
     * @return Returns the leftSessionId.
     */
    public String getLeftSessionId() {
        return leftSessionId;
    }

    /**
     * @param leftSessionId
     *            The leftSessionId to set.
     */
    public void setLeftSessionId(String leftSessionId) {
        this.leftSessionId = leftSessionId;
    }

    /**
     * @return Returns the leftStatus.
     */
    public int getLeftStatus() {
        return leftStatus;
    }

    /**
     * @param leftStatus
     *            The leftStatus to set.
     */
    public void setLeftStatus(int leftStatus) {
        this.leftStatus = leftStatus;
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
     * @return Returns the rightSessionId.
     */
    public String getRightSessionId() {
        return rightSessionId;
    }

    /**
     * @param rightSessionId
     *            The rightSessionId to set.
     */
    public void setRightSessionId(String rightSessionId) {
        this.rightSessionId = rightSessionId;
    }

    /**
     * @return Returns the rightStatus.
     */
    public int getRightStatus() {
        return rightStatus;
    }

    /**
     * @param rightStatus
     *            The rightStatus to set.
     */
    public void setRightStatus(int rightStatus) {
        this.rightStatus = rightStatus;
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
}
