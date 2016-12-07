<%@ page contentType="text/html;charset=utf-8" %><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%
	
	AdminAction action = new AdminAction(request);
	int id = action.getParameterInt("id");
	PermissionBean g = AdminAction.getPermission(id);
	if(g==null){
		response.sendRedirect("perms.jsp");
		return;
	}
	
	if(!action.isMethodGet()) {
		g.setParent(action.getParameterInt("parent"));
		g.setName(request.getParameter("name"));
		g.setBak(request.getParameter("bak"));
		AdminAction.updatePermission(g, false);
		session.setAttribute("promptMsg", "修改权限成功");
		response.sendRedirect("perms.jsp");
		return;
	}
%>
<html>
<title>小店后台管理</title>
<script>
</script>
<script type="text/javascript" src="../js/JS_functions.js"></script>
<link href="/adult-admin/css/global.css" rel="stylesheet" type="text/css">
<body style="margin-left:12px;">
<br>
<form action="perm.jsp?id=<%=g.getId()%>" method=post>
名称:<input type=text name="name" value="<%=g.getName()%>">(id : <%=g.getId()%>)<br/>
备注:<textarea name="bak" cols=60 rows=5><%=g.getBak()%></textarea><br/><br/>
父权限:<input type=text name="parent" value="<%=g.getParent()%>"><br/>
<input type="submit" value="确认修改">
</form>
<br>

</body>
</html>