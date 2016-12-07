<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction"%><%@ page import="net.joycool.wap.bean.pk.PKUserBean"%><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.pkUserInfo(request);
PKUserBean player=(PKUserBean)request.getAttribute("player");
String result =(String)request.getAttribute("result");
String url=null ;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
url=("/pk/index.jsp");%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转回场景)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
PKUserBean pkUser =action.getPkUser();
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/pk/userInfo.gif" alt="任务属性"/><br/>
<%=pkUser.toDetail()%>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>