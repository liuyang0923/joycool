/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.bean.wgamehall;

/**
 * @author lbj
 *  
 */
public class HallBean {
    public static int GOBANG = 1;
    
    public static int OTHELLO = 2;
    
    public static int FOOTBALL = 3;
    
    public static int JINHUA = 4;
    
    //赌局状态，邀请中/游戏中/结束
    public static int GS_WAITING = 0;

    public static int GS_PLAYING = 1;

    public static int GS_END = 2;   
    
    //赌局的用户状态，就绪/未就绪
    public static int PS_NOT_READY = 0;
    
    public static int PS_READY = 1;
    
    public static int PS_EXIT = 2;
    
    //结果
    public static int RESULT_MARK_LOSE = 0;

    public static int RESULT_MARK_WIN = 1;

    public static int RESULT_MARK_DRAW = 2;
}
