<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int flag=action.shaikhAbdicate(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(flag==2){
	%><card title="族长让位" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><timer value="30" /><p align="left">
	权限不够,无法让位!(3秒后自动返回我的家族)<br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}else{
	%><card title="族长让位" ontimer="<%=response.encodeURL("abdicate.jsp")%>"><timer value="30" /><p align="left"><%
	if(flag==0){
		%>非本家族成员(3秒后自动返回)<br/>
	<a href="abdicate.jsp">直接返回</a><%
	}else if(flag==4){
		%>自己折腾什么呢.(3秒后自动返回)<br/>
		<a href="abdicate.jsp">直接返回</a><br/><%
	}else if(flag==3){
		%>该用户不是本家族成员,无法让位.(3秒后自动返回)<br/>
		<a href="abdicate.jsp">直接返回</a><br/><%
	}else if(flag==1){
		%>您在之前一小时内发送过转让通知.请一小时后再次发送(3秒后跳转到家族管理页面)<br/>
		<a href="abdicate.jsp">直接返回</a><br/><%
	}else if(flag==6){
		%>该用户未达到成为族长条件,无法转让(3秒后跳转到家族管理页面)<br/>
		<a href="abdicate.jsp">直接返回</a><br/><%
	}else{
		%>族长转让通知已经成功发出,请等待对方确认!(3秒后跳转到家族管理页面)<br/>
		<a href="management.jsp">返回家族管理页面</a><br/><%
	}
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>