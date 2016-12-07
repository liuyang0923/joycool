<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.framework.*"%><%response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
%>
<html>
<head>
</head>
<body>
<a href="http://wap.joycool.net/jcadmin/friend/friendCenterStat.jsp">友好度</a><br/>
<a href="http://wap.joycool.net/jcadmin/friend/marriageStat.jsp">结婚统计</a><br/>
<a href="http://wap.joycool.net/jcadmin/friend/jyStat.jsp">结义统计</a><br/>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html>