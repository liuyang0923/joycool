package net.joycool.wap.service.infc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import net.joycool.wap.bean.jcforum.ForumBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.bean.jcforum.ForumRcmdBean;
import net.joycool.wap.bean.jcforum.ForumRcmdBean2;
import net.joycool.wap.bean.jcforum.ForumReplyBean;
import net.joycool.wap.bean.jcforum.ForumUserBean;



public interface IJcForumService {
	public ForumBean getForum(String condition);
	public Vector getForumList(String condition);
	public boolean addForum(ForumBean bean);
	public boolean delForum(String condition);
	public boolean updateForum(String set, String condition);
	public int getForumCount(String condition);
	
	public ForumContentBean getForumContent(String condition);
	public Vector getForumContentList(String condition);
	public Vector getForumContentListNew(String condition);
	public boolean addForumContent(ForumContentBean bean);
	public boolean delForumContent(String condition);
	public boolean updateForumContent(String set, String condition);
	public int getForumContentCount(String condition);
	
	public ForumReplyBean getForumReply(String condition);
	public Vector getForumReplyList(String condition);
	public boolean addForumReply(ForumReplyBean bean);
	public boolean delForumReply(String condition);
	public boolean updateForumReply(String set, String condition);
	public int getForumReplyCount(String condition);
	
	public Vector getForumIdCountList(String condition);
	public ForumUserBean getForumUser(int userId);
	public boolean addForumUser(ForumUserBean bean);
	public boolean updateForumUser(String set, String condition);
	public void updateVote(int userId, int contentId, int vote);
	public void addVote(int userId, int contentId, int vote);

	public ForumRcmdBean getRcmd(String cond);
	public int addRcmd(ForumRcmdBean bean);
	public HashMap getRcmdMap(String cond);
	
	public ForumRcmdBean2 getRcmd2(String cond);
	public int addRcmd2(ForumRcmdBean2 bean);
	public LinkedHashMap getRcmdMap2(String cond);
}
