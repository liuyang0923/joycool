<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int fmint=action.newMan(fmLoginUser.getFm_id());
String username=(String)request.getAttribute("username");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="招募新人" ontimer="<%=response.encodeURL("recruit.jsp")%>" ><timer value="30" /><p align="left"><%
if(fmint==2){
	%>操作成功,已拒绝了<%=net.joycool.wap.util.StringUtil.toWml(username)%>的加入申请(3秒后自动返回)<br/><%
}if(fmint==1){
	%>操作成功,已将<%=net.joycool.wap.util.StringUtil.toWml(username)%>加入本家族(3秒后自动返回)<br/><%
}if(fmint==3){
	%>操作成功,您已取消了对<%=net.joycool.wap.util.StringUtil.toWml(username)%>的邀请.(3秒后自动返回)<br/><%
}if(fmint==5){
	%>操作失败,您的家族人数已满,加油升级家族吧.(3秒后自动返回)<br/><%
}if(fmint==7){
	%>操作失败,该用户已加入家族/帮会,无法邀请<br/><%
}if(fmint==4){
	%>操作失败,<%=net.joycool.wap.util.StringUtil.toWml(username)%>已加入家族.(3秒后自动返回)<br/><%
}if(fmint==6){
	%>取消邀请失败!(3秒后自动返回)<br/><%
}
%><a href="recruit.jsp">直接返回</a><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>