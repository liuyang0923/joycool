<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null) {response.sendRedirect("check.jsp");return;};
if (vsUser.getWeapon() != 0 && !action.hasParam("c")) {response.sendRedirect("wait.jsp");return;}
String[] weaps = vsGame.weapons;
int start = 1;
int end = 4;
//if (vsUser.getSide() == 1)  {start = 4; end = 7;}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="丛林混战"><p align="left"><%=BaseAction.getTop(request, response)%>
请记住这些武器,战场上这些武器代表着自己的队友!<br/>请选择要使用的武器:<br/><%
for (int i = start;i < end; i++) {
	%><a href="wait.jsp?weap=<%=i%>"><%=weaps[i]%></a><br/><%
}
%><a href="/Column.do?columnId=12226">武器说明</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>