<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
int userId = action.getParameterInt("id");
int proId = action.getParameterIntS("pro");
FarmUserBean user=null;
if(userId>0){
user = FarmWorld.one.getFarmUserCache(userId);
user.setRank(9);
user.setExp(780);
}

%>
			<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
<%if(user!=null){%>经验值:<%=user.getExp()%><%}%>
	</body>
</html>