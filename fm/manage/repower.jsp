<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int userid=action.getParameterInt("uid");
FamilyUserBean fmUser=action.getFmUserByID(userid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(fmUser==null||fmUser.getFm_id()!=fmLoginUser.getFm_id()){
	%><card title="权限管理"><p align="left">
	族员和家族不相符,您不能对其任命!<br/>
	<a href="power.jsp">返回权限管理</a><br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}else{
	if(action.updateUserPosition(fmLoginUser.getFm_id())){
		%><card title="权限管理" ontimer="<%=response.encodeURL("powermgt.jsp?uid="+fmUser.getId())%>"><timer value="0.1" /><p align="left"><%
	}else{
		%><card title="权限管理" ontimer="<%=response.encodeURL("powermgt.jsp?uid="+fmUser.getId())%>"><timer value="30" /><p align="left">
		<%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>">直接返回</a><br/>
		<a href="/fm/index.jsp">返回家族首页</a><br/><%
	}
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>