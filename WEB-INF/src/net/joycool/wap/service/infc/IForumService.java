/*
 * Created on 2005-12-24
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.forum.ForumBean;

/**
 * @author lbj
 *
 */
public interface IForumService {
    public boolean addForum(ForumBean forum);
    public ForumBean getForum(String condition);
    public Vector getForumList(String condition);
}
