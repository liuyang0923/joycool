<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.game.worldcup.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%>
<% response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
UserBean loginUser = action.getLoginUser();
String tip = "";
if (loginUser != null && ForbidUtil.isForbid("game",loginUser.getId())){
	tip = "您已被封禁游戏功能.";
}
WcUser wcUser = null;
WcMatch wcMatch = null;
if (loginUser != null){
	wcUser = action.getUser(loginUser.getId());	
}
WcInfo wcInfo = WorldCupAction.getWcInfo();
List matchList = WorldCupAction.service.getMatchList(" `show`=1 and flag=0");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="竞猜"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){%>
==有奖竞猜==<br/>
<a href="/Column.do?columnId=<%=wcInfo==null?0:wcInfo.getPrizeId()%>">奖品</a>|<a href="/Column.do?columnId=<%=wcInfo==null?0:wcInfo.getHelpId()%>">规则</a>|<a href="rank.jsp">排行</a>|<a href="/jcforum/forum.jsp?forumId=1268">论坛</a><br/>
您的账户余额:<%=wcUser==null?0:wcUser.getPoint()%>彩币<br/>
<%if (matchList != null && matchList.size() > 0){
	%>【进行中的竞猜】<br/><%
	for (int i = 0 ; i < matchList.size() ; i++){
		wcMatch = (WcMatch)matchList.get(i);
		if (wcMatch != null){
			%><%=wcMatch.getTeamWml1()%>VS<%=wcMatch.getTeamWml2()%><br/>胜:<%=wcMatch.getWinf()%>/负:<%=wcMatch.getLosef()%><%if (wcMatch.getTie() > 0) {%>/平:<%=wcMatch.getTief()%><%}%><br/><%
		}
	}
} else {
	%>目前还没有可下注的比赛.<br/><%
}%>
<a href="bet.jsp">我要投注!!</a><br/>
[<a href="history.jsp">查看我的投注历史</a>]<br/>
<%if (wcInfo != null && wcInfo.getSubjectId() > 0){
%>[<a href="/Column.do?columnId=<%=wcInfo.getSubjectId()%>">返回竞猜首页</a>]<br/><%	
}
} else {
%><%=tip%><br/><a href="/lswjs/index.jsp">返回导航</a><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>