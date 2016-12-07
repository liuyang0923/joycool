<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session="false"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
// 这个页面是其他页面inner Redirect过来的，所以所有链接不能是相对
TinyAction action = new TinyAction(request);
action.r();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>