<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null) {
	response.sendRedirect("check.jsp");
	return;
}
List infoList = vsGame.getInformationList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="丛林混战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if (infoList.size() > 0) {
	Iterator iter = infoList.iterator();
	for (int i = 0; i < 3;i++){
		if (!iter.hasNext()) break;
		FightInformationBean fib = (FightInformationBean) iter.next();
		%><%=i+1%>.<%=fib.getInformation()%><br/><%
	}
} 
%>
个人战斗统计：<br/>
击杀:<%=vsUser.getHitEnemyTime()%>人|伤害量:<%=vsUser.getHitEnemyBlood()%><br/>
误杀:<%=vsUser.getHitFriendTime()%>人|伤害量:<%=vsUser.getHitFriendBlood()%><br/>
<a href="check.jsp">继续观看比赛</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>