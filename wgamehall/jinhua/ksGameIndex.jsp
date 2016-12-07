<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.PositionUtil"%><%
response.setHeader("Cache-Control","no-cache");
JinhuaAction action = new JinhuaAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean usTemp = UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="砸金花">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/jinhua.gif" alt="砸金花"/><br/>

<a href="ksGame.jsp?type=0">5百底1千赌注局</a><br/>

<a href="ksGame.jsp?type=1">1千底1万赌注局</a><br/>

<a href="ksGame.jsp?type=2">2千底2万赌注局</a><br/>

<a href="ksGame.jsp?type=3">5千底5万赌注局</a><br/>

<a href="ksGame.jsp?type=4">1万底10万赌注局</a><br/>

<a href="ksGame.jsp?type=5">10万底100万赌注局</a><br/>

<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>