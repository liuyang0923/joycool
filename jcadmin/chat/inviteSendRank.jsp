<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="java.util.*" %><%@ page import="java.sql.*" %><%response.setHeader("Cache-Control","no-cache");%>
<%
DbOperation dbOp = new DbOperation();
dbOp.init();
int i=1;
//top 20，邀请好友最多的前20名
ResultSet rs = dbOp.executeQuery("select a.id,a.nickname,count(*) as count from user_info as a join jc_room_invite as b on a.id=b.user_id group by user_id order by count desc limit 0,20 ");

%>
<html>
<head>
</head>
<body>
累计发送最多的人(前20名)&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://wap.joycool.net/jcadmin/chat/inviteIndex.jsp">邀请好友来乐酷首页</a><br/>
<table width="800" border="1">
<th align="center" width="40">排名</th>
<th align="center">用户昵称</th>
<th align="center">用户id</td>
<th align="center">发送总数</th>
<%
try{
while(rs.next()){%>
<tr>
<td><%=i%></td>
<td><%=rs.getString("nickname") %></td>
<td><%=rs.getInt("id") %></td>
<td><%=rs.getInt("count") %></td>
</tr>
<%
i++;
}}catch(SQLException ex)
	{
		ex.printStackTrace();
		dbOp.release();
	} 
	dbOp.release();
%>
</table>
<br/>
<a href="http://wap.joycool.net/jcadmin/chat/inviteIndex.jsp">邀请好友来乐酷首页</a>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html>