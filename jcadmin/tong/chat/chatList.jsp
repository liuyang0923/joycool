<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.world.*" %><%@ page import="java.util.LinkedList" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.Iterator" %><%@ page import="net.joycool.wap.framework.BaseAction" %><%response.setHeader("Cache-Control", "no-cache");			
if (session.getAttribute("adminLogin") == null) {
		BaseAction.sendRedirect("/jcadmin/login.jsp", response);
		return;
	}
%>
<html><head>
<link href="../../farm/common.css" rel="stylesheet" type="text/css">
</head>
<br>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br/>
<br/>
<%
String key = request.getParameter("key");
if(key != null){%>
<a href="/jcadmin/tong/chat/clearCache.jsp?key=<%=key%>">清除当前页面缓存</a><br/>
<br/>
<br/>
<table width="600px">
  <tr>
  	<td width="80">ID</td>
    <td>作者</td>
    <td>内容</td>
    <td>时间</td>
    <td>操作</td>
  </tr>
<%
LinkedList chatList = ChatWorld.get(key,ChatWorldBean.TONG_CHAT);
Iterator it = chatList.iterator();
int i=1;
while(it.hasNext()){
	ChatWorldBean chatWorld = (ChatWorldBean)it.next();
	UserBean user = UserInfoUtil.getUser(chatWorld.getUserId());
	if(user==null)continue;
	%>
  <tr>
 	 <td><%=i%></td>
 	<td><%=user.getNickName()%></td>
  	<td><%= chatWorld.getContent()%></td>
    <td><%= chatWorld.getCreateDatetime() %></td>
    <td><a href="/jcadmin/tong/chat/delResult.jsp?key=<%=key%>&amp;id=<%= chatWorld.getId()%>" >删除</a></td>
  </tr>
	<%
i++;}
%>
</table>
<%}%>
</html>