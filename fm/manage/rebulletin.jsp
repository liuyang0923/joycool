<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
	response.sendRedirect("/fm/index.jsp");
	return;
}
FamilyHomeBean fHome =FamilyAction.getFmByID(fmLoginUser.getFm_id());
if(fHome==null){
	response.sendRedirect("/fm/index.jsp");
	return;
}
int set=action.setBulletin(fmLoginUser.getFm_id());
String title="修改失败";
if(set==3){
	title="修改成功";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title%>"<%
if(set==0){
	if(fmLoginUser==null){
		%>ontimer="<%=response.encodeURL("/fm/index.jsp")%><%
	}else{
		%>ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"<%
	}
}else{ 
	%> ontimer="<%=response.encodeURL("bulletin.jsp?")%>"<%
} 
%> ><timer value="30" /><p align="left">
<%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><br/>
<a href="bulletin.jsp">直接返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>