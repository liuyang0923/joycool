<%@ page contentType="text/html;charset=utf-8" %><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><table border=1>
<%
String linkId = request.getParameter("linkId");
if(linkId == null){
	linkId = "0";
}
String day = request.getParameter("day");
if(day == null){
	day = "0";
}
DbOperation dbOp = new DbOperation();
dbOp.init();
ResultSet rs = dbOp.executeQuery("select distinct mobile from jc_log_mobile_ua where link_id = " + linkId + " and to_days(now()) - to_days(enter_datetime) = " + day);
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