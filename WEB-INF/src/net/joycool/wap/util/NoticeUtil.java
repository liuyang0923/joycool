/*
 * Created on 2006-5-17
 *
 */
package net.joycool.wap.util;

import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.INoticeService;

/**
 * @author lbj
 *
 */
public class NoticeUtil {
    public static INoticeService noticeService;
    
    /**
     * 取得接口。
     * @return
     */
    public static INoticeService getNoticeService(){
        if(noticeService == null){
            noticeService = ServiceFactory.createNoticeService();
        }
        return noticeService;
    }
    
    /**
     * 增加一个通知。
     * @param notice
     * @return
     */
    public static boolean addNotice(NoticeBean notice){
        return getNoticeService().addNotice(notice);
    }
    
    public static String getChatNoticeTitle(String fromNickname, String content){
        return fromNickname + ":" + StringUtil.limitString(content, 18);
    }
    
    public static String getInviteNoticeTitle(String fromNickname){
        return fromNickname + "邀请你聊天";
    }
}
