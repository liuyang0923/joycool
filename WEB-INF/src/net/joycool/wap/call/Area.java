package net.joycool.wap.call;

import java.util.List;

import jc.answer.BeanProblem;
import jc.answer.HelpAction;
import jc.exam.bag.ExamAction;
import jc.exam.bag.ExamLib;
import jc.match.MatchAction;
import jc.match.MatchRank;
import jc.match.MatchTrends;
import jc.match.MatchUser;

import net.joycool.wap.action.jcforum.ForumxAction;
import net.joycool.wap.bean.friendadver.FriendAdverBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IFriendAdverService;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 所有专区的函数
 * @author maning
 */
public class Area {
	
	/**
	 * 取得交友广告
	 */
	public static String getFriAdv(CallParam callParam){
		StringBuilder sb = new StringBuilder();
		String[] params = callParam.getParams();
		int gender = 0,count = 1;	//默认从女性的交友广告里取出1个
		if(params.length > 0) {
			gender = StringUtil.toId(params[0]);
			if(params.length > 1)
				count = StringUtil.toId(params[1]);
		}
		if (gender < 0 || gender > 1){
			gender = 0;
		}
		IFriendAdverService service = ServiceFactory.createFriendAdverService();
		
		List list = service.getFriendAdverList(" gender=" + gender + " ORDER BY jc_friend_adver.id DESC LIMIT " + count);
		if (list == null || list.size() == 0){
			return "";
		} 
		FriendAdverBean fa = null;
		for (int i = 0 ; i < list.size() ; i++){
			fa = (FriendAdverBean)list.get(i);
			if (fa != null){
				sb.append("<a href=\"/friendadver/friendAdverMessage.jsp?id=");
				sb.append(fa.getId());
				sb.append("\">");
				sb.append(StringUtil.toWml(StringUtil.limitString(fa.getTitle(),24)));
				sb.append("</a><br/>");
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 取得论坛的N条最新贴子
	 */
	public static String getContent(CallParam callParam){
		StringBuilder sb = new StringBuilder();
		String[] params = callParam.getParams();
		int type = 5,count = 1;	//默认从情感论坛里取出1个
		if(params.length > 0) {
			type = StringUtil.toId(params[0]);
			if(params.length > 1)
				count = StringUtil.toId(params[1]);
		}
		List list = ForumCacheUtil.getContentsCache(type,0,count);
		if (list == null || list.size() == 0){
			return "";
		} 
		ForumContentBean con = null;
		for (int i = 0 ; i < list.size() ; i++){
			con = ForumCacheUtil.getForumContent(StringUtil.toInt(String.valueOf(list.get(i))));
			if (con != null){
				sb.append("<a href=\"/jcforum/viewContent.jsp?contentId=");
				sb.append(con.getId());
				sb.append("\">");
				sb.append(StringUtil.toWml(StringUtil.limitString(con.getTitle(),24)));
				sb.append("</a><br/>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 取得论坛的N条精品贴子
	 */
	public static String getContent2(CallParam callParam){
		StringBuilder sb = new StringBuilder();
		String[] params = callParam.getParams();
		int type = 5,count = 1;	//默认从情感论坛里取出1个
		if(params.length > 0) {
			type = StringUtil.toId(params[0]);
			if(params.length > 1)
				count = StringUtil.toId(params[1]);
		}
		List list = SqlUtil.getIntList("select id from jc_forum_prime where forum_id=" + type + " order by id desc limit " + count,2);
		if (list == null || list.size() == 0){
			return "";
		} 
		ForumContentBean con = null;
		for (int i = 0 ; i < list.size() ; i++){
			con =ForumCacheUtil.getForumContent(StringUtil.toInt(list.get(i).toString()));
			if (con != null){
				sb.append("<a href=\"/jcforum/viewContent.jsp?contentId=");
				sb.append(con.getId());
				sb.append("\">");
				sb.append(StringUtil.toWml(StringUtil.limitString(con.getTitle(),24)));
				sb.append("</a><br/>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 取得2条女性求助最新的贴子
	 */
	public static String getFemHelp(CallParam callParam){
		StringBuilder sb = new StringBuilder();
		String[] params = callParam.getParams();
		int count=2;
		if(params.length > 0) {
			count = StringUtil.toId(params[0]);
		}
		List list = HelpAction.service.get222ProblemList(" del=0 order by id desc limit " + count);
		BeanProblem bean = null;
		for (int i = 0 ; i < list.size() ; i++){
			bean = (BeanProblem)list.get(i);
			if (bean != null){
				sb.append("<a href=\"/friend/help/hppost.jsp?pid=");
				sb.append(bean.getId());
				sb.append("\">");
				sb.append(StringUtil.toWml(StringUtil.limitString(bean.getPTitle(),24)));
				sb.append("</a><br/>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 取得5条美体的新闻
	 */
	public static String getForumNews24(CallParam callParam){
		StringBuilder sb = new StringBuilder();
		List list = ForumxAction.getRandomLatestNews(24,5);
		ForumContentBean con = null;
		for (int i = 0 ; i < list.size() ; i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con != null){
				sb.append("<a href=\"/news/news.jsp?id=");
				sb.append(con.getId());
				sb.append("&amp;tid=24");
				sb.append("\">");
				sb.append(StringUtil.toWml(StringUtil.limitString(con.getTitle(),24)));
				sb.append("</a><br/>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 从书包里取记录
	 */
	public static String getExam(CallParam callParam){
		StringBuilder sb = new StringBuilder();
		String[] params = callParam.getParams();
		int type = 10,count = 1;	//默认从数学公式里取出一个
		if(params.length > 0) {
			type = StringUtil.toId(params[0]);
			if(params.length > 1)
				count = StringUtil.toId(params[1]);
		}
		ExamLib lib = null;
		List list = ExamAction.service.getLibList(" type=" + type + " limit " + RandomUtil.nextIntNoZero(10) + "," + count);
		if (list != null && list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				lib = (ExamLib)list.get(i);
				if (lib != null){
					sb.append("<a href=\"/exam/bag/seelib.jsp?lid=");
					sb.append(lib.getId());
					sb.append("\">");
					sb.append(StringUtil.toWml(StringUtil.limitString(lib.getTitle(),24)));
					sb.append("</a><br/>");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 选美动态
	 */
	public static String getTrends(CallParam callParam){
		StringBuilder sb = new StringBuilder();
		String[] params = callParam.getParams();
		int count = 1;
		if(params.length > 0) {
			count = StringUtil.toId(params[0]);
		}
		String tmp = "";
		MatchTrends matchTrend = null;
		List list = MatchAction.service.getTrendsList(" 1 order by id desc limit " + count);
		if (list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				matchTrend = (MatchTrends)list.get(i);
				if (matchTrend != null){
					tmp = StringUtil.toWml(matchTrend.getContent());
					if (tmp != null && !"".equals(tmp)){
						tmp = tmp.replace("%l","<a href=\"/user/ViewUserInfo.do?userId=" + matchTrend.getLeftUid() + "\">" + UserInfoUtil.getUser(matchTrend.getLeftUid()).getNickNameWml() + "</a>");
						if (tmp.indexOf("%r") > 0){
							tmp = tmp.replace("%r","<a href=\"/user/ViewUserInfo.do?userId=" + matchTrend.getRightUid() + "\">" + UserInfoUtil.getUser(matchTrend.getRightUid()).getNickNameWml() + "</a>");
						}
						sb.append(tmp);
						sb.append("<br/>");
					}
				}
			}
		}
		return sb.toString();
	}
	
	public static String matchUserPhoto(CallParam callParam) {
		MatchAction action = new MatchAction();
		MatchRank rank = null;
		MatchUser matchUser = null;
		List list = MatchAction.getTopTenList();
		if (list == null || list.size() == 0){
			return "";
		} else {
			int start = MatchAction.getRandomInt(0, list.size(), 2);
			StringBuilder girlsShow = new StringBuilder();
			rank = (MatchRank)list.get(start);
			if (rank != null){
				matchUser = MatchAction.getMatchUser(rank.getUserId());
				if (matchUser != null){
					girlsShow.append("<a href=\"/friend/match/vote.jsp?uid=" + matchUser.getUserId() + "\"><img src=\"" + action.getCurrentPhoto(matchUser,true) + "\" alt=\"o\" /></a>");
				}
			}
			rank = (MatchRank)list.get(start + 1);
			if (rank != null){
				matchUser = MatchAction.getMatchUser(rank.getUserId());
				if (matchUser != null){
					girlsShow.append("<a href=\"/friend/match/vote.jsp?uid=" + matchUser.getUserId() + "\"><img src=\"" + action.getCurrentPhoto(matchUser,true) + "\" alt=\"o\" /></a><br/>");
				}
			}
			return girlsShow.toString();
		}
	}
}
