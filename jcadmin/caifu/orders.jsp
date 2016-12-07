<%@ page contentType="text/html;charset=utf-8" %><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.wxsj.util.*"%><%
int deleteId = StringUtil.toInt(request.getParameter("delete"));
if(deleteId != -1){
	DbOperation dbOp = new DbOperation();
	dbOp.init();
	dbOp.executeUpdate("delete from caifu_order where id = " + deleteId);
	dbOp.release();
	response.sendRedirect("orders.jsp");
	return;
}
%>
字段说明：<br>
title：“您要的是”<br>
name：“姓名”<br>
mobile：“手机”<br>
address：“地址”<br>
postcode：“邮编”<br>
real_mobile：“真实手机号（没取到则为空）”<br>
user_id：“用户ID（没登陆则为0）”<br>
create_datetime：“提交时间”<br>
<table border=1 width="100%">
<%
DbOperation dbOp = new DbOperation();
dbOp.init();
ResultSet rs = dbOp.executeQuery("select * from caifu_order order by id desc");
{
int col = rs.getMetaData().getColumnCount();
out.println("<tr>");
for(int i = 0;i < col;i++)
{
	out.println("<td><b>");
	out.println(rs.getMetaData().getColumnName(i + 1));
	out.println("</b></td>");
}
out.println("<td><b>");
out.println("操作");
out.println("</b></td>");
out.println("</tr>");
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
	out.println("<td>");
    out.println("<a href=\"orders.jsp?delete=" + rs.getInt("id") + "\">删除</a>");
    out.println("</td>");
	out.println("</tr>");
}
}
dbOp.release();
%>
</table>