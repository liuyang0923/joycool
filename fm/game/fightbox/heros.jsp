<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
List list = vsGame.getBoxUserList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="本场英雄榜"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (list.size() > 0) {
	Collections.sort(list);
	PagingBean paging = new PagingBean(action,list.size(),10,"p");
	%>玩家|击杀|误杀<br/><%
	for (int i = paging.getStartIndex();i<paging.getEndIndex();i++){
	BoxUserBean bub = (BoxUserBean) list.get(i);
	%><%=i+1%>.<%=vsGame.position[bub.getSide()]%><a href="hero.jsp?uid=<%=bub.getUserId()%>"><%=bub.getNickNameWml()%></a>|<%=bub.getHitEnemyTime()%>|<%=bub.getHitFriendTime()%><br/><%
	}
%><%=paging.shuzifenye("heros.jsp?jcfr=1",true,"|",response)%><%
}
%><a href="over.jsp">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>