<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
String tmp = "";
String tip = "";
String tip2 = "";
String voteList = "";
String voteCount = "";
boolean result = false;
int type = action.getParameterInt("t");
if (type < 0 || type > 1){
	type = 1;
}
int uid = action.getParameterInt("uid");
MatchUser matchUser = MatchAction.getMatchUser(uid);
if (matchUser == null){
	tip = "参赛用户不存在.";
} else {
	if (type == 0){
		// 确认投票清单
		if (action.checkVote(request)){
			voteList = (String)request.getAttribute("voteList");
			voteCount = (String)request.getAttribute("voteCount");
		} else {
			tip2 = (String)request.getAttribute("tip");
			type = 1;
		}
	} else {
		// 投票
		result = action.doVote(uid);
		tip2 = (String)request.getAttribute("tip");
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="乐后投票"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (type == 0){
// 确认投票清单
request.setAttribute("voteCount",voteCount);
%>您将为<%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%>投票,赠送给她<%=voteList%>!她可以获得<%=voteCount%>个靓点.<br/>
<a href="doVote.jsp?t=1&amp;uid=<%=uid%>">确认投票</a><br/>
<a href="vote2.jsp?uid=<%=uid%>">返回上一级</a><br/><%
} else {
// 投票	
%><%=tip2%><br/><%
if (!result){
%><a href="shop.jsp">进入奢侈品商城</a><br/><%
}%><a href="vote2.jsp?uid=<%=uid%>">返回投票页面</a><br/><a href="index.jsp">返回乐后首页</a><br/><%	
}
} else {
%><%=tip%><br/><%	
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>