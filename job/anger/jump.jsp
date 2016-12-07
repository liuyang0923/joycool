<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IJobService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.action.job.AngerAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
AngerAction action=new AngerAction(request);
if(!action.isCooldown("chat",2000)) {
    response.sendRedirect("ventAnger.jsp");
    return;
}
action.jump(request);
String bleed=(String)session.getAttribute("angerbleed");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String expression=(String)session.getAttribute("angerexpression");
if (session.getAttribute("angerrefresh") != null && "0".equals(bleed)) {
//response.sendRedirect("/job/anger/result.jsp");
BaseAction.sendRedirect("/job/anger/result.jsp", response);
return;}
if(session.getAttribute("angerrefresh") != null) {
//response.sendRedirect("/job/anger/ventAnger.jsp");
BaseAction.sendRedirect("/job/anger/ventAnger.jsp", response);
return;}
if((String)session.getAttribute("angergender")==null){
//response.sendRedirect("/job/anger/index.jsp");
BaseAction.sendRedirect("/job/anger/index.jsp", response);
return;}
String images=(String)session.getAttribute("angerimg");
String name=(String)session.getAttribute("angername");
String gender=(String)session.getAttribute("angergender");
String relation=(String)session.getAttribute("angerrelation");
String tip=(String)request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>

<% if(null!=tip){%>
<card title="出气筒游戏" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
<a href="/bank/bank.jsp">银行取钱</a><br/>
<a href="/job/anger/index.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if("0".equals(bleed)){
session.setAttribute("angerrefresh","true");%>
<card title="出气筒游戏"  ontimer="<%=response.encodeURL("/job/anger/result.jsp")%>">
<timer value="10"/>

<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=loginUser.showImg("/img/job/anger/"+images)%>
<!--<img src="/img/job/anger/<%=images %>" alt="出气对象"/><br/>-->
<%=StringUtil.toWml(name)%><%=expression.substring(0,4)%><br/>
(1秒钟跳转)<br/>
<anchor title="提交">直接跳转
    <go  href="/job/anger/result.jsp" method="post">
      </go>
</anchor><br/>
<a href="/job/anger/index.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
session.setAttribute("angerrefresh","true");%>
<card title="出气筒游戏" ontimer="<%=response.encodeURL("/job/anger/ventAnger.jsp")%>">
<timer value="10"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=loginUser.showImg("/img/job/anger/"+images)%>
<!--<img src="/img/job/anger/<%=images %>" alt="出气对象"/><br/>-->
<%=StringUtil.toWml(name)%><%=expression.substring(0,2)%><br/>
(1秒钟跳转)<br/>
<anchor title="提交">直接跳转
    <go  href="/job/anger/ventAnger.jsp" method="post">
      </go>
</anchor><br/>
<a href="/job/anger/index.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>