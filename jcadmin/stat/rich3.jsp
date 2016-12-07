<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="java.util.*"%><%!

%><%
	CustomAction action = new CustomAction(request);
	int userId=action.getParameterInt("id");
	
	RichUserBean ru = RichWorld.getRichUser(userId);
	if(ru==null){
		response.sendRedirect("rich2.jsp");
		return;
	}
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3>大富翁数据3</h3><br/>
<%=ru.log.getLogString(100)%><br/>
	<a href="rich2.jsp">返回</a><br/></br>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>