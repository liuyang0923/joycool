<%@ page contentType="text/html;charset=utf-8" %><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><table border=1>
<%
DbOperation dbOp = new DbOperation();
dbOp.init();
ResultSet rs = dbOp.executeQuery("select id, user_name, nickname, create_datetime, mobile from user_info where nickname is not null and nickname != '' and mobile like '13%' order by id desc");
{
int col = rs.getMetaData().getColumnCount();
out.println("<td>");
out.println("序号");
out.println("</td>");
for(int i = 0;i < col;i++)
{
	out.println("<td>");
	out.println(rs.getMetaData().getColumnName(i + 1));
	out.println("</td>");
}

int index = 0;

while(rs.next())
{
	index ++;
	out.println("<tr>");
	out.println("<td>");
    out.println(index);
    out.println("</td>");
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
}
dbOp.release();
%>
</table>