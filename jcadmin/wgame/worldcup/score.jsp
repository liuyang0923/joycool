<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="jc.game.worldcup.*,jc.credit.UserInfo,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%
WorldCupAction action = new WorldCupAction(request);
String tip = "";
int mid = action.getParameterInt("mid");
WcMatch wcMatch = WorldCupAction.service.getMatch(" id=" + mid);
if (wcMatch == null){
	response.sendRedirect("manage.jsp");
	return;
}
int score = action.getParameterInt("s");
if (score == 1){
	int score1 = StringUtil.toInt(action.getParameterString("score1"));
	int score2 = StringUtil.toInt(action.getParameterString("score2"));
	if (!action.addScore(wcMatch.getId(),score1,score2)){
		tip = (String)request.getAttribute("tip");
	} else {
		response.sendRedirect("manage.jsp");
		return;
	}
}
%>
<html>
	<head>
		<title>世界杯</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%=tip %><br/>
		<font color="blue"><%=wcMatch.getTeamWml1()%></font>VS<font color="blue"><%=wcMatch.getTeamWml2()%></font><br/>
		<form action="score.jsp?s=1&mid=<%=wcMatch.getId()%>" method="post">
			<%=wcMatch.getTeamWml1()%>得分:<input type="text" name="score1"/><br/>
			<%=wcMatch.getTeamWml2()%>得分:<input type="text" name="score2"/><br/>
			<input type="submit" value="提交" />&nbsp;<input type="button" value="返回" onClick="javascript:window.location.href='manage.jsp'"><br/>
		</form>
	</body>
</html>