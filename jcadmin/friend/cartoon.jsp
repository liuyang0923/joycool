<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.framework.*"%><%response.setHeader("Cache-Control","no-cache");
			if (session.getAttribute("adminLogin") == null) {
				//response.sendRedirect("../login.jsp");
				BaseAction.sendRedirect("/jcadmin/login.jsp", response);
				return;
			}
%>
<html>
<head>
</head>
<body>
交友卡通维护<br/><br/>
<table width="800" border="1">
<tr><td>编号</td><td >类别</td><td>操作</td></tr>
<tr>
<form method="post" action="cartoonList.jsp?update=1" >
<td>1</td>
<td>男头像</td>
<td><a href="cartoonList.jsp?id=2">查看详细</a>
</td></tr>
<tr>
<td>2</td>
<td>女头像</td>
<td><a href="cartoonList.jsp?id=1">查看详细</a>
</td></tr>
<tr>
<td>3</td>
<td>宠物头像</td>
<td><a href="cartoonList.jsp?id=3">查看详细</a>
</td></tr>
</form>
<tr>
</table>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html> 