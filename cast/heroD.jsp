<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%!
	static CastleService castleService = CastleService.getInstance();
%><%
	
	
	SoldierAction action = new SoldierAction(request);
	int id = action.getParameterInt("id");
	HeroBean hero = castleService.getHero("id=" + id);
	if(hero == null || hero.getUid()!=action.getCastleUser().getUid()){
		response.sendRedirect("hero.jsp");
		return;
	}

%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="指挥官"><p>
【解散指挥官】<br/>
<%=StringUtil.toWml(hero.getName())%>(<%=hero.getHeroSoldier().getSoldierName()%>)等级<%=hero.getRank()%><br/>
解散后该名指挥官就会消失,等级和经验值不会保留.需要重新训练新的指挥官,确认要解散吗?<br/>
<a href="hero.jsp?a=3&amp;id=<%=id%>">确认解散</a><br/>
<br/>
<a href="hero.jsp">不解散了直接返回</a><br/><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>