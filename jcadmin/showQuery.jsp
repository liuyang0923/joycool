<%@ page contentType="text/html;charset=utf-8" %>
<table cellpadding=2><tr">
<%
{
int col = rs.getMetaData().getColumnCount();
for(int i = 0;i < col;i++)
{
	out.println("<td>");
	out.println(rs.getMetaData().getColumnName(i + 1));
	out.println("</td>");
}
out.println("</tr>");
while(rs.next())
{
	out.println("<tr>");
	col = rs.getMetaData().getColumnCount();
	for(int i = 0;i < col;i++)
	{
		out.println("<td>");
		String str = rs.getString(i + 1);
//		if(str.length() == 11)
//			out.println("<a href=\"phonestat.jsp?phone=" + str + "\">" + str + " </a>");
//		else
		if(str!=null)
			out.println(str.replace("<","&lt;").replace(">","&gt;"));
		out.println("</td>");
	}
	out.println("</tr>");
}
}
%>
</table>