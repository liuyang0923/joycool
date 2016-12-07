<%@ page contentType="text/html;charset=utf-8" %><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%
	List list = new ArrayList(AdminAction.getGroupMap().values());
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
      <td align="center" width="80"><font color="#FFFFFF">查看组成员</font></td>
    </tr>
<%for(int i = 0;i < list.size();i++){
AdminGroupBean g = (AdminGroupBean)list.get(i);
%><tr bgcolor='#F8F8F8'>
	<td width="30" align=left><%=g.getId()%></td>
	<td align=left width="150"><a href="group.jsp?id=<%=g.getId()%>"><%=g.getName()%></a></td>
	<td align=left><%=g.getBak()%></td>
	<td align="center" width="80"><a href="users.jsp?groupId=<%=g.getId()%>">查看</a></td>
</tr>
<%}%>
</table>
<br>
<a href="groupa.jsp">添加权限组</a><br/>
<br>
<a href="clearCache.jsp" onclick="return confirm('此操作将清除所有权限缓存，确认？')">清除权限组缓存</a><br/>
<br>
<br>

</body>
</html>