<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
int fund=action.getFund(fmLoginUser);
if(fund==0){
	%><card title="提款" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><%
}else{
	%><card title="提款" ontimer="<%=response.encodeURL("fund.jsp?")%>"><%
}
%><timer value="30" /><p align="left"><%
if(action.getTip()!=null){
	%><%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><br/><%
}
%><a href="fund.jsp">直接返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>