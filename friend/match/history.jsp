<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.credit.UserBase,jc.credit.CreditAction,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%! static int COUNT_PRE_PAGE = 5; %>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
MatchUser matchUser = null;
MatchHistory matchHistory = null;
MatchAreaHistory matchAreaHistory = null;
MatchInfo matchInfo = MatchAction.getCurrentMatch();
String tip = "";
String topString = "";
UserBean user = null;
PagingBean paging = null;
//List list = null;
List areaList = null;
if (matchInfo == null){
	tip = "本次比赛尚未开始.";
} else {
	if (matchInfo.getFalg() != 2){
		response.sendRedirect("index.jsp");
		return;
	}
	topString = "本期比赛已结束,共有" + matchInfo.getUserCount() + "名选手参与,";
	if (loginUser != null){
		matchHistory = MatchAction.service.getHistory(" match_id=" + matchInfo.getId() + " and user_id=" + loginUser.getId());
		if (matchHistory != null){
			topString += "您靓点" + matchHistory.getVoteCount() + "排名" + matchHistory.getRank() + ".<br/>可以开始兑换奖品和礼品.";
		} else {
			topString += "以下是赛事回顾.";
		}
	} else {
		topString += "以下是赛事回顾.";
	}
	paging = new PagingBean(action,matchInfo.getUserCount(),COUNT_PRE_PAGE,"p");
//	list = MatchAction.service.getHistoryList(" match_id=" + matchInfo.getId() + " order by rank asc limit "  + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	areaList = MatchAction.service.getAreaHistoryList(" match_id=" + matchInfo.getId());
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="乐后比赛"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%><%=topString%><br/>
<%if(matchHistory==null){%><a href="fans.jsp">粉丝排行</a><%}else{%><a href="my.jsp">我的赛况</a><%}%>|<a href="hlist.jsp">历届成绩</a>|<a href="goods.jsp">奖品兑换</a><br/>
<a href="join.jsp">比赛规则</a>|<a href="/jcforum/forum.jsp?forumId=11091">靓女论坛</a>|<a href="/chat/hall.jsp">聊天室</a><br/>
各地区乐后排名:<br/>
<% if (areaList != null && areaList.size() > 0){
	for (int i = 0 ; i < areaList.size() ; i++){
		matchAreaHistory = (MatchAreaHistory)areaList.get(i);
		if (matchAreaHistory != null){
			matchUser = MatchAction.service.getMatchUser(" area_id=" + matchAreaHistory.getAreaId() + " order by vote_count desc,user_id desc limit 1");
			%><%=i+1%>.<a href="hlist.jsp?t=1&amp;iid=<%=matchAreaHistory.getMatchId()%>&amp;aid=<%=matchAreaHistory.getAreaId()%>"><%=matchAreaHistory.getAreaNameWml()%>[<%=matchAreaHistory.getCount()%>名靓女]</a><br/><%
			if (matchUser != null){
				user = UserInfoUtil.getUser(matchUser.getUserId());
				if (user != null){
					%><%=user.getNickNameWml()%><br/><img src="<%=action.getCurrentPhoto(matchUser,true)%>" alt="o" /><br/><a href="vote.jsp?uid=<%=matchUser.getUserId()%>">资料</a>|<a href="focus.jsp?uid=<%=matchUser.getUserId()%>">关注</a><br/><%
				}
			}
		}
	}
}
%>
ID搜索<br/>
<input name="uid" format="*N"/><br/>
<anchor>
	搜索
	<go href="vote.jsp" method="post">
		<postfield name="uid" value="$uid" />
	</go>
</anchor><br/>
<a href="goods.jsp">奖品兑换</a><br/>
<%
} else {
%><%=tip%><br/><a href="/lswjs/index.jsp">返回导航</a><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>