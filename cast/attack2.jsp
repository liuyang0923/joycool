<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.cache.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.List"%><%
	
	
	CastleAction action = new CastleAction(request);
	CastleBean castle = action.getCastle();
	int cid = castle.getId();
	
	int x = action.getParameterIntS("x");
	int y = action.getParameterIntS("y");

	
	OasisBean oasisBean = null;	
	if(CastleUtil.isXY(x, y)) {
		int type = CastleUtil.getMapType(x, y);
		if(type > 6) {
			oasisBean = CastleUtil.getOasisByXY(x, y);
		}
	}
	CastleArmyBean army = CastleUtil.getCastleArmy(cid);
	SoldierResBean[] so = ResNeed.getSoldierTs(castle.getRace());
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="出兵"><p>
<%@include file="top.jsp"%>
<%
if(oasisBean != null) {
%>【出兵至绿洲】<%=request.getParameter("x")%>|<%=request.getParameter("y")%><br/>
距离:<%=StringUtil.numberFormat(CastleUtil.calcDistance((castle.getX()-x),(castle.getY()-y)))%>格<%if((oasisBean==null||oasisBean.getCid()!=cid)){if(CastleUtil.inSquare(castle.getX()-x, castle.getY()-y, 3)){%>(在可占领范围内)<%}else{%>(无法占领)<%}}%><br/>
<%
	for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
		if(count==0) continue;
%><%=soldier.getSoldierName()%>(现有:<%=count%>):<input name="count<%=soldier.getType()%>" format="*N"/><br/><%}%>
<%if(army.getHero()!=0){%>指挥官(现有:<%=army.getHero()%>)<input name="countH" format="*N"/><br/><%}%>
<select name="atype" value="8">
<%if(oasisBean.getCid()!=0){%><option value="10">支援</option>
<option value="7">攻击</option><%}%>
<option value="8">抢夺</option> 
<option value="9">侦察</option>
</select>
<anchor>出兵
<go href="atkCy.jsp?oasis=1&amp;x=<%=x%>&amp;y=<%=y%>" method="post">
<%
for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
		if(count==0) continue;
%><postfield name="count<%=soldier.getType()%>" value="$count<%=soldier.getType()%>"/><%}%>
<postfield name="t" value="$atype"/>
<%if(army.getCount(8)!=0){%><postfield name="opt" value="$opt"/><%}%>
<%if(army.getHero()!=0){%><postfield name="countH" value="$countH"/><%}%>
</go></anchor><br/>
<a href="search.jsp?x=<%=x%>&amp;y=<%=y%>">返回</a>
<%} else {%>该坐标没有绿洲<%}%><br/><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>