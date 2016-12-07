<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmUser=action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(fmUser==null){ 
	%><card title="捐款"><p align="left">
	页面超时或您未加入该家族!<br/>
	<a href="index.jsp">返回家族首页</a><br/><%
}else{
	action.setFund();
	%><card title="捐款" ontimer="<%=response.encodeURL("fundmgt.jsp")%>"><timer value="30" /><p align="left">
	<%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><br/>
<a href="fundmgt.jsp">直接返回</a><br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>