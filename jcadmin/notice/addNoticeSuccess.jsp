<%@ page contentType="text/html;charset=utf-8"%>

<%
String addCount = request.getParameter("addCount");
out.print("<font color=red><b>发送成功！共发送给" + addCount + "个用户</b></font>");

%>