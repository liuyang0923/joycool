<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.framework.log.*"%><%
response.setHeader("Cache-Control","no-cache");
String date = request.getParameter("date");
TempLogParser.parse(date);
%>
解析完毕。