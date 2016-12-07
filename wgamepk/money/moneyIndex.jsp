<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赚钱">
<p align="center">赚钱</p>
<p align="left">
<a href="/wgamepk/money/money1.jsp">灭蟑螂</a><br/>
<a href="/wgamepk/money/money2.jsp">喂小狗</a><br/>
<br/>
<a href="/wgame/hall.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>