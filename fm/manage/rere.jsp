<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int dissolve=action.dissolveFm(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(dissolve==0){
	%><card title="解散家族" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><timer value="30" /><p align="left">
	您没有解散家族的权限!(3秒后返回我的家族)<br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}else if(dissolve==1||dissolve==2){
	%><card title="解散家族" ontimer="<%=response.encodeURL("redissolve.jsp")%>"><timer value="30" /><p align="left"><%
	if(action.getTip()!=null){
		%><%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><br/><%
	}
	%><a href="redissolve.jsp">直接返回</a><br/><%
}else if(dissolve==3){
	%><card title="解散家族" ><p align="left">
	<%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><br/><%
}
%><a href="/fm/index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>