/*
 * Created on 2006-3-9
 *
 */
package net.joycool.wap.bean.wgamehall;

import java.awt.image.BufferedImage;

/**
 * @author lbj
 *  
 */
public class GoBangDataBean {
    public static int ACTION_CONTINUE = 0;

    public static int ACTION_GIVE_UP = 1;

    public static int ACTION_SUE_FOR_PEACE = 2;
    
    public static int ACTION_DENY_PEACE = 6;

    public static int ACTION_INVITE = 3;

    public static int ACTION_ACCEPT_INVITATION = 4;

    public static int ACTION_DENY_INVITATION = 5;

    int[][] points;

    long lastActiveTime;

    int lastActiveUserId;

    String uniqueMark;

    int actionType;

    BufferedImage image;
    
    int sueForPeaceUserId1;
    
    int sueForPeaceUserId2;
    
    int moveCount;

    /**
     * @return Returns the moveCount.
     */
    public int getMoveCount() {
        return moveCount;
    }
    /**
     * @param moveCount The moveCount to set.
     */
    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }
    /**
     * @return Returns the sueForPeaceUserId1.
     */
    public int getSueForPeaceUserId1() {
        return sueForPeaceUserId1;
    }
    /**
     * @param sueForPeaceUserId1 The sueForPeaceUserId1 to set.
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
     * @param sueForPeaceUserId2 The sueForPeaceUserId2 to set.
     */
    public void setSueForPeaceUserId2(int sueForPeaceUserId2) {
        this.sueForPeaceUserId2 = sueForPeaceUserId2;
    }
    /**
     * @return Returns the image.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param image
     *            The image to set.
     */
    public void setImage(BufferedImage image) {
        this.image = image;
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

    public GoBangDataBean() {
        points = new int[15][15];
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

    /**
     * @return Returns the points.
     */
    public int[][] getPoints() {
        return points;
    }

    /**
     * @param points
     *            The points to set.
     */
    public void setPoints(int[][] points) {
        this.points = points;
    }
}
