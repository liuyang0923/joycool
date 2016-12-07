<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>==我的收获==<br/><%
String log = action.getFishUser().getLogString();
String log2 = action.getWorld().getLogString();
if(log.length() == 0){%>暂无<br/><%}else{%><%=log%><%}%>
==欢乐渔场公告==<br/>
<%=action.getWorld().getGlobalEventString()%>
==欢乐渔场快报==<br/><%if(log2.length() == 0){%>暂无<br/><%}else{%><%=log2%><%}%>
