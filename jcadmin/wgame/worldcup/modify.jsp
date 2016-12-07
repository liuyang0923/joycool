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
int modify = action.getParameterInt("m");
if (modify == 1){
		String matchTime = action.getParameterString("mt");
		String team1 = action.getParameterNoEnter("team1");
		String team2 = action.getParameterNoEnter("team2");
		int win = action.getParameterInt("win");
		int tie = action.getParameterInt("tie");
		int lose = action.getParameterInt("lose");
		if (team1 == null || "".equals(team1) || team2 == null || "".equals(team2)){
			tip = "没有输入球队的名子？";
		} else	if (win <= 0 || lose <= 0){
			tip = "胜、负、平赔率不可<=0";
		} else {
			SqlUtil.executeUpdate("update wc_match set match_time='" + matchTime + "',team1='" + StringUtil.toSql(team1) + "',team2='" + StringUtil.toSql(team2)
					+ "',win=" + win + ",tie=" + tie + ",lose=" + lose + " where id=" + wcMatch.getId(),5);
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
		<form action="modify.jsp?m=1&mid=<%=wcMatch.getId()%>" method="post">
			开赛时间:<input type="text" name="mt" value="<%=DateUtil.formatSqlDatetime(wcMatch.getMatchTime())%>" /><br/>
			球队1:<input type="text" name="team1" value="<%=wcMatch.getTeam1()%>"><br/>
			球队2:<input type="text" name="team2" value="<%=wcMatch.getTeam2()%>"/><br/>
			胜:<input type="text" name="win" value="<%=wcMatch.getWin()%>"/><br/>
			平:<input type="text" name="tie" value="<%=wcMatch.getTie()%>"/><br/>
			负:<input type="text" name="lose" value="<%=wcMatch.getLose()%>"/><br/>
			<input type="submit" value="修改" />&nbsp;<input type="button" value="返回" onClick="javascript:window.location.href='manage.jsp'"><br/>
		</form>
	</body>
</html>