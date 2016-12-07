<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
int uid = action.getParameterInt("uid");
// 根据uid 获得相应人物bean
EmperorUserBean eub = (EmperorUserBean) vsGame.getVsUser(uid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="帝王之争"><p align="left"><%=BaseAction.getTop(request, response)%><% 
if (eub != null) {
%>
玩家:<%=eub.getNickNameWml()%><br/>
贡献:<%=eub.getContribute()%>点<br/>
最大血:<%=eub.getRole().getSumBlood()%>点<br/>
当前血:<%=eub.getBlood()%>点<br/>
使用角色:<%=eub.getRole().getName()%><br/>
角色介绍:<%=eub.getRole().getPeopleIntroduction()%><br/>
人物技能:<%=eub.getRole().getSkillName()%><br/>
<% 
int skillType = eub.getRole().getSkillType();
String skillTypeStr = "主动";
if (skillType == 2) {skillTypeStr = "辅助";} else if (skillType == 3) {skillTypeStr = "被动";}
%>
<%=skillTypeStr%>技能:<%=eub.getRole().getSkillIntroduction()%><br/>
<%
} else {
	%>无此人参战!<br/><%
}

 %>
<a href="war.jsp">返回战场</a><br/>
<%if (vsGame.getState()==2) {
%><a href="hero.jsp">本场MVP榜</a><br/><%
}%>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>