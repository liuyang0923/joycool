<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=10; %>
<%
MatchAction action = new MatchAction(request);
MatchUser matchUser = null;
String tip = "";
int uid = action.getParameterInt("uid");
int desc = action.getParameterInt("d");
int vote = action.getParameterInt("vote");
if (uid > 0){
	matchUser = MatchAction.getMatchUser(uid);
	if (matchUser == null){
		tip = "参赛用户不存在。";
	} else {
		if (desc > 0){
			matchUser = action.updateVote(matchUser,vote);
		}
	}
}
%>
<html>
	<head>
		<title>用户靓点</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a>|<a href="userVote.jsp">重新查找</a><br/>
		<%=tip%><br/>
		<form action="userVote.jsp" method="post">
			请输入UID：<input type="text" name="uid">
			<input type="submit" value="查找">
		</form>
		<%if (matchUser != null){
			%>用户名:<%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%>(<%=matchUser.getUserId()%>)<br/>
			  本赛季靓点:<%=matchUser.getVoteCount()%><br/>
			  总靓点/花费的靓点:<%=matchUser.getTotalVote()%>/<%=matchUser.getTotalConsume()%><br/>
			  <form action="userVote.jsp?d=1&uid=<%=matchUser.getUserId()%>" method="post">
			  		请输入要操作的靓点:<input type="text" name="vote">(输入负数表示减少)
			  		<input type="submit" value="操作" onclick="return confirm('确认操作靓点？')">
			  </form>
			<%
		}
		%>
	</body>
</html>