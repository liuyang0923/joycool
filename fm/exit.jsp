<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(request.getParameter("cmd")==null){
	%><card title="退出家族"><p align="left">
	退出家族会令您损失全部家族贡献值,是否确认退出?<br/>
	<a href="exit.jsp?cmd=e">确认</a><br/>
	<a href="myfamily.jsp">算了</a><br/><%
}else{
	%><card title="退出家族" ontimer="<%=response.encodeURL("myfamily.jsp")%>" ><timer value="30"/><p align="left"><%
	int result=action.exitFamily(fmLoginUser.getFm_id());
	String fmName=(String)request.getAttribute("fmName");
	if(result==1){
		%>您与<%=net.joycool.wap.util.StringUtil.toWml(fmName)%>家族恩断义绝,从此分道扬镳.(3秒后自动返回)<br/><%
	}else if(result==2){
		%>您是族长,不能退出.<br/>
		<a href="myfamily.jsp">返回我的家族</a><br/><%
	}else if(result==3){
		%>您加入家族不足3天,不能退出<br/>
		<a href="myfamily.jsp">返回我的家族</a><br/><%
	}
	%><a href="index.jsp">返回家族首页</a><br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>