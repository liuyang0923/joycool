<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
int uid=action.getParameterInt("uid");
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
	response.sendRedirect("/fm/index.jsp");
	return;
}
String p="";
if(request.getParameter("p")!=null){
	p="?p="+request.getParameter("p");
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(request.getParameter("cmd")==null){
	if(uid==0){
		%><card title="开除成员" ontimer="<%=response.encodeURL("membermgt.jsp"+p)%>"><timer value="30" /><p align="left">
		ID输入错误,请重新输入.(3秒后自动返回)<br/><a href="membermgt.jsp<%=p%>">直接返回</a><br/><%
	}else{
		UserBean user=UserInfoUtil.getUser(uid);
		if(user==null){
			%><card title="开除成员" ontimer="<%=response.encodeURL("membermgt.jsp"+p)%>"><timer value="30" /><p align="left">
			该用户不存在!(3秒后自动返回)<br/>
			<a href="membermgt.jsp<%=p%>">直接返回</a><br/><%
		}else{
			%><card title="开除成员"><p align="left">
			您确认要开除家族成员:<%=user.getNickNameWml()%>?<br/>
			<a href="tofire.jsp?cmd=y&amp;uid=<%=uid%>&amp;p=<%=request.getParameter("p")%>">确认开除</a><br/>
			<a href="membermgt.jsp<%=p%>">返回</a><br/><%
		}
	}
}else if(request.getParameter("cmd")!=null&&"y".equals(request.getParameter("cmd"))){ 
	int fire=action.toFireOut(fmLoginUser);
	if(fire==0){
		int fmid = action.getFmId();
		%><card title="开除成员" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><%
	}else{
		%><card title="开除成员" ontimer="<%=response.encodeURL("membermgt.jsp"+p)%>"><%
	}%><timer value="30" /><p align="left">
	<%=action.getTip()%><br/>
	<a href="membermgt.jsp<%=p%>">直接返回</a><br/>
	<a href="management.jsp">返回家族管理</a><br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>