<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
if(request.getParameter("cmd")!=null&&"y".equals(request.getParameter("cmd"))){
	%><wml><%
	int invite=action.inviteUserRe(fmLoginUser.getFm_id());
	if(invite==0){
		%><card title="家族邀请" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><%
	}else{
		%><card title="家族邀请" ontimer="<%=response.encodeURL("recruit.jsp")%>"><timer value="30" /><%
	}
	%><timer value="30" /><p align="left">
	<%=action.getTip()%><br/><%
	if(invite==0){
		%><a href="/fm/myfamily.jsp">直接返回</a><br/><%
	}else{
		%><a href="recruit.jsp">直接返回</a><br/><%
	}
}else{
	net.joycool.wap.bean.UserBean user=action.inviteUser();
	if(user==null){%>
		<wml><card title="家族邀请" ontimer="<%=response.encodeURL("recruit.jsp?")%>"><timer value="30" /><p align="left">
		<%=action.getTip()%><br/><%
	}else{
		%><wml><card title="家族邀请"><p align="left">
		<%=user.getNickNameWml()%><br/>
		<%=net.joycool.wap.util.StringUtil.toWml(user.getCityname()==null?"未知":user.getCityname())%>/<%=user.getAge()%>岁/<%=user.getGender()==0?"女":"男"%>/<%=user.getCityno()%>级<br/>
		您确认要邀请:<%=user.getNickNameWml()%>加入本家族么?<br/>
		<a href="invite.jsp?cmd=y&amp;userid=<%=user.getId()%>">确认</a><br/><%
	}
		%><a href="recruit.jsp">直接返回</a><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>