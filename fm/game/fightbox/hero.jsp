<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
int uid = action.getParameterInt("uid");
String [] weaps = vsGame.weapons;
// 根据uid 获得相应人物bean
BoxUserBean bub = (BoxUserBean) vsGame.getVsUser(uid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="本场英雄榜"><p align="left"><%=BaseAction.getTop(request, response)%><% 
if (bub != null) {
	int weap = bub.getWeapon();
	if (vsUser != null && vsUser.getSide() != bub.getSide()) {
		weap += 3;
	}
%><a href="/user/ViewUserInfo.do?userId=<%=bub.getUserId()%>"><%=bub.getNickNameWml()%></a><br/>
武器:<%=weaps[weap]%><br/>
剩余血量:<%=bub.getBlood()%><br/>
击杀:<%=bub.getHitEnemyTime()%>人|伤害量:<%=bub.getHitEnemyBlood()%><br/>
误杀:<%=bub.getHitFriendTime()%>人|伤害量:<%=bub.getHitFriendBlood()%><br/>
<%
} else {
	%>查无此人<br/><%
}
%><a href="heros.jsp">返回本场英雄榜</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>