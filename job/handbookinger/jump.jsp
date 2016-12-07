<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HandbookingerAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
HandbookingerAction action=new HandbookingerAction(request);
if(session.getAttribute("chipIn")==null)
{//response.sendRedirect("/job/handbookinger/index.jsp");
BaseAction.sendRedirect("/job/handbookinger/index.jsp", response);
 return;
 }
action.chipIn(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if("failure".equals((String)request.getAttribute("result"))&& "您的乐币不够买赌注".equals((String)request.getAttribute("tip"))){
String tip=(String)request.getAttribute("tip");%>
<card title="失败页面" ontimer="<%=response.encodeURL("/bank/bank.jsp")%>">
<timer value="100"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
（10秒后跳转到银行页面）<br/>
<a href="/bank/bank.jsp">银行</a><br/>
<a href="/job/handbookinger/index.jsp">跑马首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%}else if("failure".equals((String)request.getAttribute("result"))){
String tip=(String)request.getAttribute("tip");%>
<card title="失败页面" ontimer="<%=response.encodeURL("/job/handbookinger/index.jsp")%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
（3秒后跳转到跑马页面）<br/>
<a href="/job/handbookinger/index.jsp">跑马首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%}
else {
session.setAttribute("jump","true");
int horseId=StringUtil.toInt((String)request.getAttribute("horseId"));
int compensateName=StringUtil.toInt((String)request.getAttribute("compensateId"));
int money=StringUtil.toInt((String)request.getAttribute("money"));
int id=StringUtil.toInt((String)request.getAttribute("id"));
%>
<card title="跳转页面" ontimer="<%=response.encodeURL("/job/handbookinger/result.jsp?money="+money+"&amp;horseId="+horseId+"&amp;compensateId="+compensateName+"&amp;id="+id)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
比赛进行中......<br/>
<img src="<%=("/img/job/handbookinger/horse.gif" )%>" alt="跳转图片"/><br/>
（比赛5秒后结束）<br/>
心急了？<a href="<%=("/job/handbookinger/result.jsp?money="+money+"&amp;horseId="+horseId+"&amp;compensateId="+compensateName+"&amp;id="+id)%>">现在看结果</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%}%>