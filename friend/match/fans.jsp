<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.UserBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
MatchAction.todayFans();
UserBean loginUser = action.getLoginUser();
//if (loginUser == null){
//	response.sendRedirect("/user/login.jsp");
//	return;
//}
List list = null;
String tip = "";
String title = "";
String feetLink = "";
int type = action.getParameterInt("t");
if (type < 0 || type > 1){
	type = 0;
}
MatchVoted matchVoted = null;
MatchFansRank fansRank = null;
MatchInfo matchInfo = MatchAction.getCurrentMatch();
if (matchInfo == null){
	tip = "比赛尚未开始,敬请期待.";
} else if (matchInfo.getFalg() == 2){
	type =1;	// 如果比赛结束，就只能看总粉丝排行榜了。
}
if (loginUser != null){
	fansRank = MatchAction.service.getMatchFansRank(" user_id=" + loginUser.getId());
}
if (type == 0){
	list = MatchAction.service.getFansRankList(" 1 limit 10");
	title = "=热情粉丝本日排行=";
	feetLink="<a href=\"fans.jsp?t=1\">粉丝累积排名</a><br/>";
} else {
	list = MatchAction.service.getVotedList(" 1 order by prices desc limit 10");
	title = "=热情粉丝总排行=";
	if (matchInfo != null && matchInfo.getFalg() != 2){
		feetLink="<a href=\"fans.jsp\">热情粉丝本日排名</a><br/>";
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="粉丝排行榜"><p>
<%=BaseAction.getTop(request, response)%>
<%if("".equals(tip)){
%>热情粉丝大血拼!<br/>
目前已有<%=matchInfo.getFansCount()%>名粉丝参与投票!大家快加油,博取美人一笑啊!<br/>
<%if (fansRank != null){
	%>您本日已给<%=fansRank.getVoteCount()%>位乐后投过票,共投<%=fansRank.getPrices()%>个靓点,排名第<%=fansRank.getId()%>.<br/><%
}%>
<%=title%><br/>
<%if (list != null && list.size() > 0){
	if (type == 0){
		for (int i = 0 ; i < list.size() ; i++){
			fansRank = (MatchFansRank)list.get(i);
			if (fansRank != null){
				%><%=fansRank.getId()%>.<a href="/user/ViewUserInfo.do?userId=<%=fansRank.getUserId()%>"><%=UserInfoUtil.getUser(fansRank.getUserId()).getNickNameWml()%></a><br/>
				共投送<%=fansRank.getPrices()%>个靓点.<br/><%
			}
		}
	} else if (type == 1){
		for (int i = 0 ; i < list.size() ; i++){
			matchVoted = (MatchVoted)list.get(i);
			if (matchVoted != null){
				%><%=i+1%>.<a href="/user/ViewUserInfo.do?userId=<%=matchVoted.getUserId()%>"><%=UserInfoUtil.getUser(matchVoted.getUserId()).getNickNameWml()%></a><br/>
				共投送<%=matchVoted.getPrices()%>个靓点.<br/><%
			}
		}
	}
}%>
<%=feetLink%>
<a href="index.jsp">乐后首页</a>|<a href="join.jsp">参赛规则</a>|<a href="/jcforum/forum.jsp?forumId=11090">靓女答疑</a><br/>
<a href="rank.jsp?t=2">十大乐后</a>|<a href="rank.jsp?t=1">最新靓女</a>|<a href="rank.jsp?t=3">官方推荐</a><br/>
<%
} else {
%><%=tip%><br/><%
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>