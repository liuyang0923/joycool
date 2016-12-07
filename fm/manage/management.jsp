<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmUser=action.getFmUser();
if(fmUser==null||fmUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fm = FamilyAction.getFmByID(fmUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
if(fmUser==null){
	%><wml><card title="家族管理" ontimer="<%=response.encodeURL("/fm/index.jsp")%>" ><p align="left"><%=BaseAction.getTop(request, response)%><timer value="30"/>
	您不是该家族的成员,无权进行管理,(3秒后自动返回)<br/><%
}else{
	int flag=fmUser.getFm_flags();
	if(true){
		%><wml><card title="家族管理"><p align="left"><%=BaseAction.getTop(request, response)%>【家族管理】<br/>
		<%if(fmUser.isflagRemoveMemberint()){%><a href="membermgt.jsp" >成员管理</a><%}else if(fmUser.isflagNewMember()){%><a href="recruit.jsp">招募新人</a><%}else{%>成员管理<%}%>|<%if(fmUser.isflagAppoint()){%><a href="power.jsp">权限管理</a><%}else{%>权限管理<%}%><br/>
		<%if(fmUser.isflagMoney()){%><a href="fund.jsp">基金管理</a><%}else{%>基金管理<%}%>|<%if(fmUser.isflagNotice()){%><a href="noticemgt.jsp">家族通知</a><%}else{%>家族通知<%}%><br/>
		<%if(fmUser.isflagLeader()){%><a href="rename.jsp">家族易帜</a><%}else{%>家族易帜<%}%>|<%if(fmUser.isBulletin()){%><a href="bulletin.jsp">家族公告</a><%}else{%>家族公告<%}%><br/>
		<%if(fmUser.isflagPublic()){%><a href="allyset.jsp">设置友联</a><%}else{%>设置友联<%}%>|<%if(fmUser.isflagPublic()){%><a href="familydes.jsp">家族简介</a><%}else{%>家族简介<%}%><br/>
		<%if(fmUser.isflagLeader()){%><a href="level.jsp">提高等级</a>|<a href="abdicate.jsp">族长让位</a><%}else{%>提高等级|族长让位<%}%><br/>
		<%if(fmUser.isflagPublic()){%><a href="logo.jsp">家族徽记</a><%}else{%>家族徽记<%}%>|<%if(fmUser.isflagAppoint()){%><a href="chat.jsp">聊天室设置</a><%}else{%>聊天室设置<%}%><br/><%
	if(fmUser.isflagLeader()){
		if(fm!=null&&fm.getForumId()==0){
			%><a href="forum.jsp">创建论坛</a><br/><%
		}
		if(!fm.isEatery()){
			%><a href="eatery.jsp">餐厅开张</a><br/><%
		}
	}%>
	<%if(fmUser.isflagLeader()){%>解散家族<%}%><br/>
【我的管理权限】<br/><%=fmUser.getFmFlags()%><br/>
<a href="/Column.do?columnId=12775">!!家族管理说明</a><br/>
&lt;<a href="/fm/myfamily.jsp">我的家族</a>&lt;<a href="/fm/index.jsp">家族首页</a><br/><%
	}
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>