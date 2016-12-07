package net.joycool.wap.call;

import java.util.*;

import net.joycool.wap.action.jcforum.ForumxAction;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 刷新闻
 */
public class News {
	
	// 显示新闻列表.点击新闻标题后进入新闻内容
	public static String news(CallParam callParam){
		String[] params = callParam.getParams();
		int type = 9, count1 = 2;	// 新闻类型默认是乐酷热点，取出2个
		if(params.length > 0) {
			type = StringUtil.toId(params[0]);
			if(params.length > 1) {
				count1 = StringUtil.toId(params[1]);
//				if(params.length > 2) {
//					count2 = StringUtil.toId(params[1]);
//				}
			}
		}

		if(count1 == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		List list = ForumxAction.getRandomLatestNews(type,count1);
		ForumContentBean con = null;
		for (int i = 0 ; i < list.size() ; i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con != null){
				sb.append("<a href=\"/news/news.jsp?id=");
				sb.append(con.getId());
				sb.append("&amp;tid=");
				sb.append(type);
				sb.append("\">");
				sb.append(StringUtil.toWml(StringUtil.limitString(con.getTitle(),24)));
				sb.append("</a><br/>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 取得最新的N条新闻
	 * @param callParam
	 * @return
	 */
	public static String news2(CallParam callParam){
		String[] params = callParam.getParams();
		int type = 9, count1 = 2;	// 新闻类型默认是乐酷热点，取出2个
		if(params.length > 0) {
			type = StringUtil.toId(params[0]);
			if(params.length > 1) {
				count1 = StringUtil.toId(params[1]);
			}
		}

		if(count1 == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		List list = SqlUtil.getIntList("select id from forum_news where type=" + type + " order by id desc limit " + count1, 2);
		ForumContentBean con = null;
		for (int i = 0 ; i < list.size() ; i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con != null){
				sb.append("<a href=\"/news/news.jsp?id=");
				sb.append(con.getId());
				sb.append("&amp;tid=");
				sb.append(type);
				sb.append("\">");
				sb.append(StringUtil.toWml(StringUtil.limitString(con.getTitle(),24)));
				sb.append("</a><br/>");
			}
		}
		return sb.toString();
	}

}