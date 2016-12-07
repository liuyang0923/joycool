<%@ page contentType="text/html;charset=utf-8" %><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%
	AdminAction action = new AdminAction(request);
	if(!action.isMethodGet()) {
		AdminUserBean g = new AdminUserBean();
		g.setId(action.getParameterInt("id"));
		g.setFlags(action.getParameterFlag("flag", 5));
		g.setName(request.getParameter("name"));
		g.setBak(request.getParameter("bak"));
		AdminAction.addUserGroup(g);
		session.setAttribute("promptMsg", "添加权限组成功");
		response.sendRedirect("groups.jsp");
		return;
	}
	List permList = AdminAction.getPermissionList(" 1 order by id");
%>
<html>
<title>小店后台管理</title>
<script>
</script>
<script type="text/javascript" src="../js/JS_functions.js"></script>
<link href="/adult-admin/css/global.css" rel="stylesheet" type="text/css">
<body style="margin-left:12px;">
<br>
<form action="groupa.jsp" method=post>
名称:<input type=text name="name" value="">(id : <input type=text name="id" value="">)<br/>
备注:<textarea name="bak"></textarea><br/><br/>
权限:<br>
<%for(int i = 0;i < permList.size();i++){
PermissionBean perm = (PermissionBean)permList.get(i);
%>
<input type=checkbox name="flag" value="<%=perm.getId()%>"><%=perm.getId()%>.<%=perm.getName()%><br/>
<%}%>
<input type="submit" value="确认修改">
</form>
<br>
</body>
</html>