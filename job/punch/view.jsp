<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.punch.*" %><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
PunchAction action = new PunchAction(request);
action.view();
String data = action.getWorld().getDataString(response);
String url = ("view.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="c" title="打小强" ontimer="<%=response.encodeURL(url)%>">
<timer value="100"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getLoginUser().showImg("img/logo.gif")%>
<%=data%><br/>
<a href="<%=url%>">刷新</a><br/>
<a href="index.jsp">换个地方打</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>