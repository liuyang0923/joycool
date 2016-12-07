<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%>
<%
FightBoxAction action = new FightBoxAction(request,response);
int weapon = action.getParameterInt("w");
if (weapon < 1 || weapon > 3) {
	weapon = 1;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="丛林混战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if (weapon == 2){%>
<a href="weapinfo.jsp?w=1">剑</a>|枪|<a href="weapinfo.jsp?w=3">弓</a><br/>
<%} else if (weapon == 3){%>
<a href="weapinfo.jsp?w=1">剑</a>|<a href="weapinfo.jsp?w=2">枪</a>|弓<br/>
<%} else {%>
剑|<a href="weapinfo.jsp?w=2">枪</a>|<a href="weapinfo.jsp?w=3">弓</a><br/>
<%}%>
武器伤害：2<br/>攻击范围：<br/>
<%if (weapon == 2) {%>
>正向：<br/>
口口攻口口<br/>
口攻攻攻口<br/>
口口口口口<br/>
口口我口口<br/>
>斜向：<br/>
攻口口口口<br/>
口攻攻口口<br/>
口攻口口口<br/>	
口口口我口<br/>
<%} else if (weapon == 3) {%>
>正向：<br/>
口口攻攻攻口<br/>
口口口攻口口<br/>
口口口口口口<br/>
口口口口口口<br/>
口口口我口口<br/>
>斜向：<br/>
口攻口口口口<br/>
攻攻口口口口<br/>
口口攻口口口<br/>	
口口口口口口<br/>
口口口口口口<br/>
口口口口口我<br/>
<%} else {%>
>正向：<br/>
口口攻口口<br/>
口攻攻攻口<br/>
口口我口口<br/>
>斜向：<br/>
攻口口口口<br/>
口攻攻口口<br/>
口攻我口口<br/>	
<%}%>
*“攻”为该武器攻击范围<br/><a href="chweap.jsp">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>