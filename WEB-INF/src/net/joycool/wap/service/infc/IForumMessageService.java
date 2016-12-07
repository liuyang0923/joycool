/*
 * Created on 2005-12-24
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.forum.ForumMessageBean;

/**
 * @author lbj
 *
 */
public interface IForumMessageService {
    public boolean addForumMessage(ForumMessageBean message);
    public ForumMessageBean getForumMessage(String condition);
    public Vector getForumMessageList(String condition);
    public boolean deleteForumMessage(String condition);
    public boolean updateForumMessage(String set, String condition);
    public int getForumMessageCount(String condition);
    //zhul 2006-09-07 取出图片id
    public int[] getForumMessageId(String order);
    public int getNewForumMessageCount(String condition);
}
