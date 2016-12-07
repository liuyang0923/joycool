<%@ page contentType="text/html;charset=utf-8" %><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><table border=1>
<%
DbOperation dbOp = new DbOperation();
dbOp.init();
ResultSet rs = dbOp.executeQuery("select * from jc_log_mobile_ua where to_days(now()) - to_days(enter_datetime) <= 1 order by id");
{
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
}
dbOp.release();
%>
</table>