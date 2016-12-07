<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null) {response.sendRedirect("war.jsp");return;}//观众可以直接进入
if (vsUser.getRole() != null){response.sendRedirect("seat.jsp");return;}//选过角色的用户
if (!vsUser.isHasAddToWait()) {
	vsGame.addUser(vsUser);
}
List chooseList = vsGame.getFmAChooseRoleList();
if (vsUser.getSide() == 1) {
	chooseList = vsGame.getFmBChooseRoleList();
}
long coolTime = vsGame.getStartTime() - System.currentTimeMillis();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="角色列表"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if (coolTime <= 0) {%>已开始<%} else {%>距开始时间:<%=GameAction.getFormatDifferTime(coolTime)%><%}%>!<a href="choro.jsp">刷新</a><br/><%
if (vsUser.getRole() == null) {
%> 选择角色参与战斗吧!<br/><%
	for (int i =0;i<chooseList.size();i++){
		EmperorChooseRoleBean chooseBean = (EmperorChooseRoleBean) chooseList.get(i);
		if (chooseBean.isBeChoose()) {
		%><%=chooseBean.getRole().getName()%>|<%=chooseBean.getChooseUserNameWml()%>选择<%
		} else {
		%><a href="role.jsp?cid=<%=i%>"><%=chooseBean.getRole().getName()%></a><%
		}
		%><br/><%
	}
}
%>
<%if(vsUser==null){%>
<%=vsGame.getChat().getChatString(0, 3)%><a href="chat2.jsp">更多聊天信息</a><br/>
<%}else{%>
聊天信息&gt;&gt;家族|<a href="chat2.jsp">公共</a><br/>
<%=vsGame.getChat(vsUser.getSide()).getChatString(0, 3)%>
<input name="fchat"  maxlength="100"/>
<anchor>发言<go href="chat.jsp" method="post"><postfield name="content" value="$fchat"/></go></anchor><br/>
<a href="chat.jsp">更多聊天信息</a><br/>
<%}%>
<a href="/fm/index.jsp">&lt;&lt;返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>