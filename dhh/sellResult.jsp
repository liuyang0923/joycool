<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.sellresult();
String result =action.getResult();
String url=("/dhh/market.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(result.equals("failure")){%>
<%=action.getTip()%>(3秒后返回)<br/>
<%}else{%>
卖出成功，货款已经打入你的帐户(3秒后返回交易所)<br/>
<%}%>
<a href="<%=url%>">返回交易所</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
