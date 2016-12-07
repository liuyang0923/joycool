<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.SquareWheelAction"%><%
response.setHeader("Cache-Control","no-cache");
SquareWheelAction action= new SquareWheelAction(request);

if(action.getSession()){
action.chipIn(request);
action.remSession();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if("failure".equals((String)request.getAttribute("result"))&& "您的乐币不够买赌注".equals((String)request.getAttribute("tip"))){
	String tip=(String)request.getAttribute("tip");
%><card title="失败页面" ontimer="<%=response.encodeURL("/bank/bank.jsp")%>"><timer value="100"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
（10秒后跳转到银行页面）<br/>
<a href="/bank/bank.jsp">银行</a><br/>
<a href="/job/wheel/StartWheel.jsp">俄罗斯轮盘首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%}else if("failure".equals((String)request.getAttribute("result"))){
	String tip=(String)request.getAttribute("tip");
%><card title="失败页面" ontimer="<%=response.encodeURL("StartWheel.jsp")%>"><timer value="10"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
（3秒后跳转到俄罗斯轮盘页面）<br/>
<a href="/job/wheel/StartWheel.jsp">俄罗斯轮盘首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%}else{
%><card title="俄罗斯轮盘" ontimer="<%=response.encodeURL("result.jsp")%>"><timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/img/job/wheel/10.gif" alt="图片"/><br/>
(1秒钟跳转)<br/><a href="result.jsp">直接看结果..</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%}%>
	
	
