/*
 * Created on 2006-1-23
 *
 */
package net.joycool.wap.bean.wgame;

/**
 * @author lbj
 *  
 */
public class WGameBean {
    public static int GT_DC = 1;

    public static int GT_PK = 2;

    public static int GT_HALL = 3;

    public static int DC_DICE = 1;

    public static int DC_G21 = 2;

    public static int DC_JSB = 3;

    public static int DC_TIGER = 4;

    public static int DC_SHOWHAND = 5;

    public static int DC_GONG3 = 6;

    public static int DC_FOOTBALL = 7;
    
//  2007.4.2 LIq篮球游戏
    public static int DC_BASKETBALL = 10;
    
    //2007.4.2 LIq老虎杠子鸡
    public static int DC_LGJ = 11;
    
    //2007.4.2 LIq单双
    public static int DC_WORE = 12;
    

    public static int DC_DICEDX = 8;
    
    public static int PK_DICE = 1;
    
    public static int PK_GONG3 = 2;
    
    public static int PK_JSB = 3;
    
    public static int PK_FOOTBALL = 4;
    //2007.4.2 LIq篮球游戏
    public static int PK_BASKETBALL = 6;
    
    //2007.4.2 LIq老虎杠子鸡
    public static int PK_LGJ = 7;
    
    public static int MAX_BK_COUNT = 5;
    
    public static int MAX_PK_COUNT = 5;
    
    public static int MAX_INVITE_COUNT = 1;
    
    public static int HALL_GOBANG = 1;
    
    public static int HALL_OTHELLO = 2;
    
    public static int HALL_FOOTBALL = 3;
//2007.5.21_Liq_砸金花
    public static int HALL_JINHUA = 4;
    
    int girlId;

    int wins;

    /**
     * @return Returns the girlId.
     */
    public int getGirlId() {
        return girlId;
    }

    /**
     * @param girlId
     *            The girlId to set.
     */
    public void setGirlId(int girlId) {
        this.girlId = girlId;
    }

    /**
     * @return Returns the wins.
     */
    public int getWins() {
        return wins;
    }

    /**
     * @param wins
     *            The wins to set.
     */
    public void setWins(int wins) {
        this.wins = wins;
    }
}
