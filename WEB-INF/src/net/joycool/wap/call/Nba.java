package net.joycool.wap.call;

import java.util.List;

import jc.news.nba.BeanMatch;
import jc.news.nba.NbaAction;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

public class Nba {
	/**
	 * 取得N条NBA赛事
	 */
	public static String getNbaMatch(CallParam callParam){
		BeanMatch bean = null;
		StringBuilder sb = new StringBuilder();
		String status[] = {"未赛","已完赛","直播中"};
		String[] params = callParam.getParams();
		int count=2;
		if(params.length > 0) {
			count = StringUtil.toId(params[0]);
		}
		List list = NbaAction.service.getMatchList(" 1 order by id desc limit " + count);
		for (int i = 0 ; i < list.size() ; i++){
			bean = (BeanMatch)list.get(i);
			if (bean != null){
				sb.append("<a href=\"/news/nba/alive.jsp?mid=" + bean.getId());
				sb.append("\">");
				sb.append(StringUtil.toWml(bean.getTeam1()));
				sb.append("VS");
				sb.append(StringUtil.toWml(bean.getTeam2()));
				sb.append("</a>");
				sb.append(bean.getCode());
				sb.append(":");
				//StaticValue:0=未赛;1=已完赛;2=直播中
				sb.append(status[bean.getStaticValue()]);
				sb.append("<br/>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 取得NBA直播赛事
	 */
	public static String getNbaLive(CallParam callParam){
		BeanMatch bean = null;
		StringBuilder sb = new StringBuilder();
//		String[] params = callParam.getParams();
//		int count=2;
//		if(params.length > 0) {
//			count = StringUtil.toId(params[0]);
//		}
		List list = NbaAction.service.getMatchList(" static_value=2");
		if (list != null && list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				bean = (BeanMatch)list.get(i);
				if (bean != null){
					sb.append("<a href=\"/news/nba/alive.jsp?mid=" + bean.getId());
					sb.append("\">");
					sb.append(StringUtil.toWml(bean.getTeam1()));
					sb.append("VS");
					sb.append(StringUtil.toWml(bean.getTeam2()));
					sb.append("</a>");
					sb.append(bean.getCode());
					sb.append("<br/>");
				}
			}
		} else {
			sb.append("(暂无)");
			sb.append("<a href=\"/news/nba/match.jsp\">");
			sb.append("查看最近赛事");
			sb.append("</a><br/>");
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
				sb.append("\">");
				sb.append(StringUtil.toWml(StringUtil.limitString(con.getTitle(),24)));
				sb.append("</a><br/>");
			}
		}
		return sb.toString();
	}
}
