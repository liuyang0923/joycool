<%@ page contentType="text/html;charset=utf-8" %><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%
	AdminAction action = new AdminAction(request);
	int groupId = action.getParameterIntS("groupId");
	List list;
	if(groupId >= 0)
		list = AdminAction.getAdminUserList("group_id="+groupId + " order by id");
	else if(request.getParameter("u")!=null)
		list = AdminAction.getAdminUserList("name='"+request.getParameter("u")+"' order by id");
	else
		list = AdminAction.getAdminUserList("1 order by id");

%>
<html>
<title>小店后台管理</title>
<script>
</script>
<script type="text/javascript" src="../js/JS_functions.js"></script>
<link href="/adult-admin/css/global.css" rel="stylesheet" type="text/css">
<body style="margin-left:12px;" onload="document.f1.u.focus();document.f1.u.select();">
<br>
<form action="users.jsp" method=post name=f1>
<input type=text name="u" value="<%=adminUser.getName()%>"><input type=submit value="搜索用户名">
</form>
<br>
  <table width="90%" cellpadding="3" cellspacing="1" bgcolor="#e8e8e8">
      <tr bgcolor="#4688D6">
      <td width="100" align="center"><font color="#FFFFFF">id</font></td>
      <td width="100" align="center"><font color="#FFFFFF">username</font></td>
      <td align="center" width="100"><font color="#FFFFFF">权限组</font></td>
      <td align="center" width="100"><font color="#FFFFFF">附加权限组</font></td>
      <td align="center" width="100"><font color="#FFFFFF">附加权限组</font></td>
      <td align="center" width="100"><font color="#FFFFFF">隶属部门</font></td>
      <td align="center" width="100"><font color="#FFFFFF">用户级别</font></td>
      <td align="center"><font color="#FFFFFF">备注</font></td>
    </tr>
<%for(int i = 0;i < list.size();i++){
AdminUserBean p = (AdminUserBean)list.get(i);
AdminGroupBean g = AdminAction.getAdminGroup(p.getGroupId());
AdminGroupBean g2 = AdminAction.getAdminGroup(p.getGroupId2());
AdminGroupBean g3 = AdminAction.getAdminGroup(p.getGroupId3());
%><tr bgcolor='#F8F8F8'>
	<td width="100" align=left><%=p.getId()%></td>
	<td align=left width="100"><a href="user.jsp?id=<%=p.getId()%>"><%=p.getName()%></a></td>
	<td align=left><%=g.getName()%></td>
	<td align=left><%=g2.getName()%></td>
	<td align=left><%=g3.getName()%></td>
	<td align=left></td>
	<td align=left></td>
	<td align=left><%=g.getBak()%></td>
</tr>
<%}%>
</table>
<br>
<br>
</body>
</html>