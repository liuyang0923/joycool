<%@ page contentType="text/html;charset=utf-8" %><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.util.db.DbOperation"%>最大私聊刷屏数（所有）：<br>
<table border=1>
<%
DbOperation dbOp = new DbOperation();
dbOp.init();
ResultSet rs = dbOp.executeQuery("select * from (SELECT count(id) as count, left(send_datetime, 16) as `minute` FROM `jc_room_content` where is_private=1 and mark=0 and to_days(now()) - to_days(send_datetime) <= 2 group by left(send_datetime, 16) order by count desc) as temp limit 10000");

int col = rs.getMetaData().getColumnCount();
for(int i = 0;i < col;i++)
{
	out.println("<td>");
	out.println(rs.getMetaData().getColumnName(i + 1));
	out.println("</td>");
}

while(rs.next())
{
	out.println("<tr>");
	col = rs.getMetaData().getColumnCount();
	for(int i = 0;i < col;i++)
	{
		out.println("<td>");
		String str = rs.getString(i + 1);
		out.println(str);
		out.println("</td>");
	}
	out.println("</tr>");
}
%>
</table>
最大公聊刷屏数（所有）：<br>
<table border=1>
<%
rs = dbOp.executeQuery("select * from (SELECT count(id) as count, left(send_datetime, 16) as `minute` FROM `jc_room_content` where is_private=0 and mark=0 and to_days(now()) - to_days(send_datetime) <= 2 group by left(send_datetime, 16) order by count desc) as temp limit 10000");

col = rs.getMetaData().getColumnCount();
for(int i = 0;i < col;i++)
{
	out.println("<td>");
	out.println(rs.getMetaData().getColumnName(i + 1));
	out.println("</td>");
}

while(rs.next())
{
	out.println("<tr>");
	col = rs.getMetaData().getColumnCount();
	for(int i = 0;i < col;i++)
	{
		out.println("<td>");
		String str = rs.getString(i + 1);
		out.println(str);
		out.println("</td>");
	}
	out.println("</tr>");
}
%>
</table>
最大刷屏数（所有）：<br>
<table border=1>
<%
rs = dbOp.executeQuery("select * from (SELECT count(id) as count, left(send_datetime, 16) as `minute` FROM `jc_room_content` where mark=0  and to_days(now()) - to_days(send_datetime) <= 2 group by left(send_datetime, 16) order by count desc) as temp limit 10000");

col = rs.getMetaData().getColumnCount();
for(int i = 0;i < col;i++)
{
	out.println("<td>");
	out.println(rs.getMetaData().getColumnName(i + 1));
	out.println("</td>");
}

while(rs.next())
{
	out.println("<tr>");
	col = rs.getMetaData().getColumnCount();
	for(int i = 0;i < col;i++)
	{
		out.println("<td>");
		String str = rs.getString(i + 1);
		out.println(str);
		out.println("</td>");
	}
	out.println("</tr>");
}
dbOp.release();
%>
</table>