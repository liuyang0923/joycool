<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null) {response.sendRedirect("war.jsp");return;}//观众可以直接进入
if (vsUser.getRole() == null) {response.sendRedirect("choro.jsp");return;} // 未选择角色用户跳回选择角色页面
//if (vsGame.getState() != 0) { // 比赛开始,直接跳入
	//if (System.currentTimeMillis() > vsGame.getStopEnter() + 120000){vsGame.setDie(vsUser);} // 超过一定时间的直接死亡
//}
Object[] seats = action.getSeats(vsGame,vsUser);
if (action.hasParam("sid")) {action.sit(seats,vsGame,vsUser);}
long coolTime = vsGame.getStartTime() - System.currentTimeMillis();
if (vsGame.getState() != 0) { // 比赛开始,直接跳入
	action.addVsUser(vsGame,vsUser);
	response.sendRedirect("war.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="出场顺序"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (request.getAttribute("tip") != null) {%><%=request.getAttribute("tip")%><br/><%}
if (coolTime <= 0) {%>已开始<%} else {%>距开始时间:<%=GameAction.getFormatDifferTime(coolTime)%><%}%>!<a href="choro.jsp">刷新</a><br/><%
for (int i = 0; i < seats.length;i++) {
	EmperorUserBean seatUser = (EmperorUserBean) seats[i];
	%><%=i+1%>.<%
	if (seatUser != null) {
		%><%=seatUser.getRole().getName()%><%if (vsUser.getUserId() == seatUser.getUserId()){%><a href="seat.jsp?d=1&amp;sid=<%=i%>">站起</a><%} %><br/><%
	} else {
		if (vsUser.isHasSit()) {
		%>坐下<br/><%
		} else {
		%><a href="seat.jsp?sid=<%=i%>">坐下</a><br/><%
		}
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