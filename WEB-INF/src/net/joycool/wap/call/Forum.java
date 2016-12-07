package net.joycool.wap.call;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJcForumService;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 用户相关
 * 
 * @author bomb
 *  
 */
public class Forum {

	static IJcForumService forumService = ServiceFactory.createJcForumService();
	// 随机选择最近的一条精华，限制字数长度
    public static String random(CallParam callParam) {
    	String[] params = callParam.getParams();
        int forumId = StringUtil.toId(params[0]);
        int limitChar = 18;
        if(params.length > 1)
        	limitChar = StringUtil.toId(params[1]);
		String condition = "select id from jc_forum_prime where forum_id= " + forumId + " order by id DESC limit 0,20";// 需要修改查询条件
		List formList = (List) SqlUtil.getIntListCache(condition, 15, 2);
		if (formList.size() == 0) {
			return "";
		}
		Integer temp = (Integer)RandomUtil.randomObject(formList);
		ForumContentBean forum = (ForumContentBean) ForumCacheUtil.getForumContent(temp.intValue());

		if (forum == null)
			return "";
		
		StringBuilder ret = new StringBuilder(64);
		ret.append("<a href=\"");
		ret.append("/jcforum/viewContent.jsp?forumId="
						+ forum.getForumId() + "&amp;contentId=" + forum.getId());
		ret.append("\">");
		ret.append(StringUtil.toWml(StringUtil.limitString(forum.getTitle(), limitChar)));
		ret.append("</a>");
		return ret.toString();
    }
    // 取出最近随机n条
    public static String randoms(CallParam callParam) {
        String[] params = callParam.getParams();
        int forumId = StringUtil.toId(params[0]);
        int count = params.length > 1 ? StringUtil.toId(params[1]) : 1;
        List list = ForumCacheUtil.getContentsCache(forumId);
        List list2;
		synchronized(list) {
	        if(list.size() <= count) {
	        	list2 = new ArrayList(list.size());
	        	Iterator iter = list.iterator();
				for(int i = 0;i < count;i++)
					list2.add(ForumCacheUtil.getForumContent(((Integer)iter.next()).intValue()));
	        } else {
				int start = RandomUtil.nextInt(list.size() - count);
				list2 = new ArrayList(count);
				Iterator iter = list.iterator();
				for(int i = 0;i < start;i++)
					iter.next();
				for(int i = 0;i < count;i++)
					list2.add(ForumCacheUtil.getForumContent(((Integer)iter.next()).intValue()));
	        }
		}
        
        StringBuilder ret = new StringBuilder(64);

		for (int i = 0; i < list2.size(); i++) {
			ForumContentBean con = (ForumContentBean) list2.get(i);
			if (con == null) continue;
			ret.append(i + 1);
			ret.append('.');
			ret.append("<a href=\"");;
			ret.append("/jcforum/viewContent.jsp?contentId=");
			ret.append(con.getId());
			ret.append("\">");
			ret.append(StringUtil.toWml(StringUtil.limitString(con.getTitle(), 24)));
			ret.append("</a><br/>");
		}
		return ret.toString();
    }
}