<%@ page contentType="text/html;charset=utf-8" %><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%
	AdminAction action = new AdminAction(request);
	if(!action.isMethodGet()) {
		PermissionBean g = new PermissionBean();
		g.setId(action.getParameterInt("id"));
		g.setParent(action.getParameterInt("parent"));
		g.setName(request.getParameter("name"));
		g.setBak(request.getParameter("bak"));
		AdminAction.updatePermission(g, true);
		session.setAttribute("promptMsg", "添加权限成功");
		response.sendRedirect("perms.jsp");
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
  <table width="90%" cellpadding="3" cellspacing="1" bgcolor="#e8e8e8">
      <tr bgcolor="#4688D6">
      <td width="30" align="center"><font color="#FFFFFF">id</font></td>
      <td width="150" align="center"><font color="#FFFFFF">名称</font></td>
      <td align="center"><font color="#FFFFFF">备注</font></td>
    </tr>
<%for(int i = 0;i < permList.size();i++){
PermissionBean perm = (PermissionBean)permList.get(i);
%><tr bgcolor='#F8F8F8'>
	<td width="30" align=left><%=perm.getId()%></td>
	<td align=left width="150"><a href="perm.jsp?id=<%=perm.getId()%>"><%=perm.getName()%></a></td>
	<td align=left><%=perm.getBak()%></td>
</tr><%}%>
</table>
<br><hr>
<form action="perms.jsp" method=post>
权限名称:<input type=text name="name" value="">(id : <input type=text name="id" value="">)<br/>
父权限:<input type=text name="catalog" value="0">(可以不填)<br/>
备注:<textarea name="bak"></textarea><br/><br/>
<input type="submit" value="确认添加权限">
</form>
<br>
</body>
</html>