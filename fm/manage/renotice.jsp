<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
%><wml><%
int notice=action.insertNotice(fmLoginUser.getFm_id());
if(notice==0||notice==1){
	%><card title="发送通知" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><%
}else{
	%><card title="发送通知" ontimer="<%=response.encodeURL("noticemgt.jsp")%>"><%
}
%><timer value="30" /><p align="left">
<%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><br/>
<a href="noticemgt.jsp">直接返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>