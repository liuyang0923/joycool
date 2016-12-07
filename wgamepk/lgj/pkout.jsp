<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%
response.setHeader("Cache-Control","no-cache");
LgjAction action = new LgjAction(request);
action.pkOut(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String bkId=(String) request.getAttribute("bkId");
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="选择挑战手式">
<p align="center">选择挑战手式</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//不够庄家的乐币数币
if("failure".equals(result)){
%>
<%=tip%><br/>
<a href="index.jsp">返回上一级</a><br/>
<%
} else if("success".equals(result)){
%>
请选择挑战手式：<br/>
<a href="chlStart.jsp?action=a&amp;bkId=<%=bkId%>">杠子</a><br/>
<a href="chlStart.jsp?action=b&amp;bkId=<%=bkId%>">老虎</a><br/>
<a href="chlStart.jsp?action=c&amp;bkId=<%=bkId%>">鸡</a><br/>
<a href="chlStart.jsp?action=d&amp;bkId=<%=bkId%>">虫子</a><br/>
<%
} 
%>
<br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>