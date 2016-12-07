<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.text.SimpleDateFormat,java.util.Calendar,java.util.Date"%><title>财富项目统计</title>
财富项目统计<br>
<strong>注意：本统计不在数据库内保存，如需长期保存请自己保存网页！</strong><br>
<%
Calendar now = Calendar.getInstance();
Calendar cal = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Date date = sdf.parse("2007-02-05");
cal.setTime(date);
String str = null;
while(cal.before(now)){
    str = sdf.format(cal.getTime());
%>
<a href="tempLogDate.jsp?date=<%=str%>"><%=str%></a><br>
<%
    cal.add(Calendar.DATE, 1);
}
%>