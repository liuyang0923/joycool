/**
 *作者：李北金
 *创建日期：2006-08-07
 *说明：本类用于提高通知系统效率。
 */
package net.joycool.wap.cache;

import java.util.Hashtable;

/**
 * @author lbj
 *
 */
public class NoticeCacheUtil {
    /**
     * 没有新通知的用户的ID列表。
     */
    public static Hashtable noNoticeUserIds;
    
    /**
     * 取得没有新通知的用户的ID列表。
     * @return
     */
    public static Hashtable getNoNoticeUserIds(){
        if(noNoticeUserIds == null){
            noNoticeUserIds = new Hashtable();
        }
        return noNoticeUserIds;
    }
    
    /**
     * 记录一个用户没有新通知。
     * 
     * @param userId
     */
    public static void addNoNoticeUserId(int userId){
        getNoNoticeUserIds().put("" + userId, "true");
    }
    
    /**
     * 当有一个用户有新通知时，从列表中删除。
     * 如果正确删除返回true
     * @param userId
     */
    public static void removeNoNoticeUserId(int userId){
        getNoNoticeUserIds().remove(String.valueOf(userId));
    }
    
    /**
     * 判断一个用户是否在列表中（是否有新通知）。
     * @param userId
     * @return
     */
    public static boolean isInNoNoticeList(int userId){
        if(getNoNoticeUserIds().get("" + userId) != null){
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * 清空列表。
     * @param userId
     */
    public static void removeAll(){
        getNoNoticeUserIds().clear();
    }
}
