<%@ page contentType="text/html;charset=utf-8" %><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%
	AdminAction action = new AdminAction(request);
	int id = action.getParameterInt("id");
	AdminUserBean p = AdminAction.getAdminUser("id="+id);
	List pl = AdminAction.getAdminUserList("id="+id);
	
	if(p!=null&&pl.size()>0) {
		p = (AdminUserBean)pl.get(0);
		if(!action.isMethodGet()) {
			p.setGroupId(action.getParameterInt("groupId"));
			p.setGroupId2(action.getParameterInt("groupId2"));
			p.setGroupId3(action.getParameterInt("groupId3"));
			AdminAction.updateUserPermission(p, false);
		}
	}
	List glist = new ArrayList(AdminAction.getGroupMap().values());
%>
<html>
<title>小店后台管理</title>
<script>
</script>
<script type="text/javascript" src="../js/JS_functions.js"></script>
<link href="/adult-admin/css/global.css" rel="stylesheet" type="text/css">
<body style="margin-left:12px;">
<br>
<%if(p!=null){%>
<form action="user.jsp?id=<%=p.getId()%>" method=post>
<table width="250" align=left cellpadding="3" cellspacing="1" bgcolor="#e8e8e8">
<tr><td height="30" align="center" bgcolor="#F8F8F8">用户名</td><td height="30" bgcolor="#F8F8F8"><%=p.getName()%>(id : <%=p.getId()%>)</td></tr>
<tr><td height="30" align="center" bgcolor="#F8F8F8">
权限组</td><td height="30" bgcolor="#F8F8F8"><select name="groupId">
<option value="0" <%if(0==p.getGroupId()){%>selected<%}%> >无</option>
<%for(int i=0;i<glist.size();i++){
AdminGroupBean g = (AdminGroupBean)glist.get(i);
%><option value="<%=g.getId()%>" <%if(g.getId()==p.getGroupId()){%>selected<%}%> ><%=g.getName()%></option>
<%}%>
</select>
</td></tr>
<tr><td height="30" align="center" bgcolor="#F8F8F8">
附加权限组</td><td height="30" bgcolor="#F8F8F8"><select name="groupId2">
<option value="0" <%if(0==p.getGroupId2()){%>selected<%}%> >无</option>
<%for(int i=0;i<glist.size();i++){
AdminGroupBean g = (AdminGroupBean)glist.get(i);
%><option value="<%=g.getId()%>" <%if(g.getId()==p.getGroupId2()){%>selected<%}%> ><%=g.getName()%></option>
<%}%>
</select>
</td></tr>
<tr><td height="30" align="center" bgcolor="#F8F8F8">
附加权限组</td><td height="30" bgcolor="#F8F8F8"><select name="groupId3">
<option value="0" <%if(0==p.getGroupId2()){%>selected<%}%> >无</option>
<%for(int i=0;i<glist.size();i++){
AdminGroupBean g = (AdminGroupBean)glist.get(i);
%><option value="<%=g.getId()%>" <%if(g.getId()==p.getGroupId3()){%>selected<%}%> ><%=g.getName()%></option>
<%}%>
</select>
</td></tr>
<tr><td height="30" align="center" bgcolor="#F8F8F8" colspan=2><input type="submit" value="确认修改"></td></tr>
</table>
</form>
<br>
<%}else{%>
用户不存在<br/>
<%}%>
</body>
</html>