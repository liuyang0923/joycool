<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int userid=action.getParameterInt("uid");
FamilyUserBean fmUser=action.getFmUserByID(userid);
FamilyHomeBean fm=FamilyAction.getFmByID(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(!fmLoginUser.isflagAppoint()){
	%><card title="权限管理" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><timer value="30" /><p align="left"><%=BaseAction.getTop(request, response)%>
	您没有管理权限!(3秒后返回我的家族)<br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}else if(fmUser==null||fmUser.getFm_id()!=fm.getId()){
	%><card title="权限管理"><p align="left"><%=BaseAction.getTop(request, response)%>
	族员和家族不相符,您不能对其任命!<br/>
	<a href="power.jsp">返回权限管理</a><br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}else if(false){
	%><card title="权限管理"><p align="left"><%=BaseAction.getTop(request, response)%>
	族长的权限不能进行修改!<br/>
	<a href="power.jsp">返回权限管理</a><br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}else{ 
	String p="";
	if(request.getParameter("p")!=null){
		p="?p="+request.getParameter("p");
	}
	if(fmUser.getFm_name()==null||"".equals(fmUser.getFm_name())){
		%><card title="权限管理"><p align="left"><%=BaseAction.getTop(request, response)%>
		任命权限－<a href="/user/ViewUserInfo.do?userId=<%=fmUser.getId()%>"><%=fmUser.getNickNameWml()%></a><br/>
		您需要先为其设置称号后,才可赋予权限.<br/>
		设置称号(最多4个字)<br/>
		<input name="fmname" maxlength="4"/>
		<anchor title="设置">确定
		   <go href="repower.jsp?uid=<%=fmUser.getId()%>" method="post">
		   	<postfield name="fmname" value="$(fmname)" /></go>
		</anchor><br/><%
	}else{
		if(request.getParameter("flag")!=null){
			action.updateFmFlagsById(fmLoginUser.getFm_id());
		}
		%><card title="权限管理"><p align="left"><%=BaseAction.getTop(request, response)%>
	<%if(action.isResult("tip")){%>!!<%=action.getTip()%><br/><%}%>
		任命权限－<a href="/fm/fmuserinfo.jsp?userid=<%=fmUser.getId()%>"><%=fmUser.getNickNameWml()%></a><br/>
		当前称号:<%=net.joycool.wap.util.StringUtil.toWml(fmUser.getFm_name())%>&#160;<a href="pupdate.jsp?uid=<%=fmUser.getId()%>">修改</a><br/>
		<%if(!fmUser.isflagLeader()){%><a href="recall.jsp?uid=<%=fmUser.getId()%>">罢免他</a><br/><%}%>
		设置权限:<br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=0">招募新人</a>(<%=FamilyUserBean.isflag_new_member(fmUser.getFm_flags())?"有":"无"%>)<br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=1">踢除成员</a>(<%=FamilyUserBean.isflag_remove_memberint(fmUser.getFm_flags())?"有":"无"%>)<br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=2">家族公告</a>(<%=FamilyUserBean.isbulletin(fmUser.getFm_flags())?"有":"无"%>)<br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=9">餐厅大厨</a>(<%=fmUser.isflagYard()?"有":"无"%>)<br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=10">相册管理</a>(<%=fmUser.isflagPhoto()?"有":"无"%>)<br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=3">家族通知</a>(<%=FamilyUserBean.isflag_notice(fmUser.getFm_flags())?"有":"无"%>)<br/><%
		if(fm.getForumId()!=0){
			%><a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=4">论坛管理</a>(<%=FamilyUserBean.isflag_bbs(fmUser.getFm_flags())?"有":"无"%>)<br/><%
		}
		%><a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=6">游戏管理</a>(<%=FamilyUserBean.isflag_game(fmUser.getFm_flags())?"有":"无"%>)<br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=7">基金管理</a>(<%=FamilyUserBean.isflag_money(fmUser.getFm_flags())?"有":"无"%>)<br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=5">聊天管理</a>(<%=fmUser.isflagChat()?"有":"无"%>)<br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=12">宣传部长</a>(<%=fmUser.isflagPublic()?"有":"无"%>)<br/>
		<%if(!fmUser.isflagLeader()&&fmLoginUser.isflagLeader()){%><a href="powermgt.jsp?uid=<%=fmUser.getId()%>&amp;flag=11">权限助理</a>(<%=fmUser.isflagAppoint()?"有":"无"%>)<br/><%}%>
		<%} %>
&gt;&gt;<a href="/Column.do?columnId=12775">权限说明</a><br/>
&lt;<a href="power.jsp<%=p%>">权限管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>