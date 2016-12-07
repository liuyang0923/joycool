<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page	import="jc.game.worldcup.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%!
	 static String betStr[] = {"胜","负","平"}; 
%><% response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
String tip = "";
UserBean loginUser = action.getLoginUser();
if (loginUser != null && ForbidUtil.isForbid("game",loginUser.getId())){
	tip = "您已被封禁游戏功能.";
}
WcUser wcUser = null;
WcMatch wcMatch = null;
WcInfo wcInfo = WorldCupAction.getWcInfo();
List matchList = null;
if (loginUser != null){
	wcUser = action.getUser(loginUser.getId());	
}
int betId = action.getParameterInt("bid");	//胜？负？平？
if (betId < 0 || betId > 2){
	betId = 0;
}
int matchId = action.getParameterInt("mid");
int type = action.getParameterInt("t");
if (type > 2 || type < 0){
	type = 0;
}
if (type == 0){
	// 比赛列表
	matchList = WorldCupAction.service.getMatchList(" `show`=1 and flag=0");	
} else if (type == 1){
	// 输入投注信息
	wcMatch = action.checkMatch(matchId);
	if (wcMatch == null){
		tip = (String)request.getAttribute("tip");
	} else if (betId == 2 && wcMatch.getTie() <= 0){
		tip = "您的选择错误.<br/><a href=\"bet.jsp\">回上一页</a>";
	}
} else if (type == 2){
	// 投注
	if (action.doBet(matchId,betId)){
		tip = "投注成功!<br/><a href=\"bet.jsp\">继续投注</a>";
	} else {
		tip = (String)request.getAttribute("tip");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="竞猜"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
if (type == 0){
// 今日赛事列表
%>以下是当前可下注的比赛.明天会推出新的场次竞猜,敬请关注.<br/>请您下注,注意赔率哦!<br/><%
if (matchList != null && matchList.size() > 0){
	for (int i = 0 ; i < matchList.size() ; i++){
		wcMatch = (WcMatch)matchList.get(i);
		if (wcMatch != null){
			%><%=wcMatch.getTeamWml1()%>VS<%=wcMatch.getTeamWml2()%><br/>
			<a href="bet.jsp?t=1&amp;mid=<%=wcMatch.getId()%>&amp;bid=0"><%=wcMatch.getTeamWml1()%>胜</a><%=wcMatch.getWinf()%><%if(wcMatch.getTie() > 0){%>,<a href="bet.jsp?t=1&amp;mid=<%=wcMatch.getId()%>&amp;bid=2">平</a><%=wcMatch.getTief()%><%}%>,<a href="bet.jsp?t=1&amp;mid=<%=wcMatch.getId()%>&amp;bid=1"><%=wcMatch.getTeamWml1()%>负</a><%=wcMatch.getLosef()%><br/><%
		}
	}
} else {
	%>目前还没有可下注的比赛.<br/><%
}
} else if (type == 1){
// 输入投注信息
if (betId == 2){
	%>您选择了<%=wcMatch.getTeamWml1()%>与<%=wcMatch.getTeamWml2()%>战平.<br/><%
} else {
	%>您选择了<%=wcMatch.getTeamWml1() + betStr[betId]%><br/><%
}
%>您当前余额:<%=wcUser==null?0:wcUser.getPoint()%>球币<br/>
请输入您的投注金额(每场比赛不得高于2000):<br/>
<input name="bet" value="" maxlength="4" format="*N"/><br/>
<anchor>
	确认
	<go href="bet.jsp?t=2&amp;mid=<%=wcMatch.getId()%>&amp;bid=<%=betId%>" method="post">
		<postfield name="bet" value="$bet" />
	</go>
</anchor><br/>
<a href="bet.jsp">返回上一页</a><br/>
<%
}
} else {
%><%=tip%><br/><%	
}%>
[<a href="index.jsp">返回投注首页</a>]<br/>
<%if (wcInfo != null && wcInfo.getSubjectId() > 0){
%>[<a href="/Column.do?columnId=<%=wcInfo.getSubjectId()%>">返回竞猜首页</a>]<br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>