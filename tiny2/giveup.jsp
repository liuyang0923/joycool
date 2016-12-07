<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
action.giveup();
if(action.getGame() != null) return;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="无标题">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%@include file="footer.jsp"%>
</p>
</card>
</wml>