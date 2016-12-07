<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.punch.*" %><%
response.setHeader("Cache-Control","no-cache");
PunchAction action = new PunchAction(request);
action.index();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="打小强">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.getLoginUser()!=null){
%><%=action.getLoginUser().showImg("img/logo.gif")%><%
}else{
%><img src="img/logo.gif" alt="logo"/><br/><%}%>
<a href="view.jsp?r=0">去仓库打&gt;&gt;</a><br/>
<a href="view.jsp?r=1">去厨房打&gt;&gt;</a><br/>
<a href="view.jsp?r=2">去室外打&gt;&gt;</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>