<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int rename=action.setFmName(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(rename==0){
	int fmId=action.getFmId(); 
	if(fmId<1||fmId!=fmLoginUser.getFm_id()){
		%><card title="家族易帜" ontimer="<%=response.encodeURL("/fm/index.jsp")%>"><%
	}else{
		%><card title="家族易帜" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><%
	}
}else{
	%><card title="家族易帜" ontimer="<%=response.encodeURL("rename.jsp")%>"><%
}
%><timer value="30" /><p align="left">
<%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><br/>
<a href="rename.jsp">直接返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>