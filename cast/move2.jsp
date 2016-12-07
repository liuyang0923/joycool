<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.cache.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.List"%><%
	
	
	CastleAction action = new CastleAction(request);
	boolean flag = action.move2();
	int cid = action.getCastle().getId();
	
	CastleArmyBean army = (CastleArmyBean)request.getAttribute("army");
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="指令"><p>
<%@include file="top.jsp"%>
<% if(flag){ %><%=action.getTip()%><br/>
<%if(army!=null&&!army.isEmpty()){%><%if(request.getAttribute("all")==null){%><a href="move2.jsp?id=<%=army.getId()%>">返回</a><br/><%}%><%}%>
<%}else{%><%
if(army != null) {
	OasisBean oasis = CastleUtil.getOasisById(army.getAt());
	CastleBean castle = null;
	CastleBean castle2 = null;	// 所属城堡
	boolean mine = (army.getCid()==cid);
	if(mine) {
		castle = CastleUtil.getCastleById(oasis.getCid());
		castle2=action.getCastle();
	}else{
		castle = CastleUtil.getCastleById(army.getCid());
		castle2 = castle;
	}
	SoldierResBean[] so = ResNeed.getSoldierTs(castle2.getRace());
	boolean send = (army.getCid()!=0&&army.getCid()!=army.getAt());	// 允许回派
%>【军队详细数据】<br/>
<%if(mine){%>支援<a href="search.jsp?x=<%=oasis.getX()%>&amp;y=<%=oasis.getY()%>">绿洲(<%=oasis.getX()%>|<%=oasis.getY()%>)</a><%}else{%>来自<a href="search.jsp?x=<%=castle.getX()%>&amp;y=<%=castle.getY()%>"><%=castle.getCastleNameWml()%>(<%=castle.getX()%>|<%=castle.getY()%>)</a><%}%>的军队<br/><%
	for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
		if(count==0) continue;
%><%=soldier.getSoldierName()%>(现有:<%=count%>)<%if(send){%><input name="count<%=soldier.getType()%>" format="*N"/><%}%><br/><%}
%><%if(army.getHero()!=0){%>指挥官(现有:<%=army.getHero()%>)<%if(send){%><input name="countH" format="*N"/><%}%><br/><%}%>
<%if(send){		// 如果cid==0表示为自然界军队
%><anchor><%if(army.getCid()==cid){%>召回<%}else{%>回派<%}%>军队
<go href="move2.jsp?id=<%=army.getId()%>&amp;t=1" method="post">
<%
for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
		if(count==0) continue;
%><postfield name="count<%=soldier.getType()%>" value="$count<%=soldier.getType()%>"/><%}%>
<%if(army.getHero()!=0){%><postfield name="countH" value="$countH"/><%}%>
</go></anchor>|!<a href="move2.jsp?id=<%=army.getId()%>&amp;t=2">全部<%if(mine){%>召回<%}else{%>回派<%}%></a><br/><%}%>
总粮食消耗:<%=army.getGrainCost(so)%>/小时<br/>
<%} else {%>军队不存在<br/><%}}%><br/>
<a href="fun.jsp?t=22">返回集结点</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>