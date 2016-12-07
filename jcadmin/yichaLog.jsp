<%@ page contentType="text/html;charset=utf-8" %><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.framework.*"%><table border=1>
<%
//lbj_��¼����_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_��¼����_end

DbOperation dbOp = new DbOperation();
dbOp.init();
ResultSet rs = dbOp.executeQuery("SELECT left(jump_datetime, 10) as date, count(distinct mobile) as count FROM `jc_yicha_log` group by to_days(jump_datetime)");
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