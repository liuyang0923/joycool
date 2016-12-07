<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.cache.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.List"%><%
	
	
	CastleAction action = new CastleAction(request);
	int cid = action.getCastle().getId();
	
	int x = action.getParameterIntS("x");
	int y = action.getParameterIntS("y");
	
	int castleId = CastleUtil.getMapCastleId(x, y);
	CastleBean castle = action.getCastle();
	CastleBean castleBean = null;
	if(castleId != 0) {
		castleBean = CastleUtil.getCastleById(castleId);
	}
	CastleArmyBean army = CastleUtil.getCastleArmy(cid);
	SoldierResBean[] so = ResNeed.getSoldierTs(castle.getRace());
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="出兵"><p><%@include file="top.jsp"%>
<%
if(castleBean != null) {
if(x < 0 || y < 0){%>
输入的坐标不正确<br/><%
	}else {
%>【出兵至坐标】<%=request.getParameter("x")%>|<%=request.getParameter("y")%><br/><%
if(castleBean!=null&&castleBean.getUid()!=castle.getUid()){
	CastleUserBean u = CastleUtil.getCastleUser(castleBean.getUid());
	if(u.getTong()!=0&&u.getTong()==action.getCastleUser().getTong()){
	%>(该坐标是你的同盟)<br/><%}}
%>距离:<%=StringUtil.numberFormat(CastleUtil.calcDistance((castle.getX()-x),(castle.getY()-y)))%>格<br/>
<%
	for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
		if(count==0) continue;
%><%=soldier.getSoldierName()%>(现有:<%=count%>)<input name="count<%=soldier.getType()%>" format="*N"/><%if(i==8){
int gatherGrade = action.getUserResBean().getBuildingGrade(ResNeed.GATHER_BUILD);
%><select name="opt" value="0"><option value="0">随意攻击</option>
<%	//  神器
for(int i2=1;i2<ResNeed.buildingTypeCount;i2++)if(ResNeed.gatherOpt[i2]<=gatherGrade){
%><option value="<%=i2%>"><%=ResNeed.getTypeName(i2)%></option><%
}%></select><%}%><br/><%}%>
<%if(army.getHero()!=0){%>指挥官(现有:<%=army.getHero()%>)<input name="countH" format="*N"/><br/><%}%>
<select name="atype" value="5">
<option value="5">支援</option>
<option value="0">攻击</option>
<option value="1">抢夺</option> 
<option value="2">侦察资源和军队</option>
<option value="3">侦察建筑和军队</option>
</select>
<anchor>出兵
<go href="atkCy.jsp?x=<%=x%>&amp;y=<%=y%>" method="post">
<%
for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
		if(count==0) continue;
%><postfield name="count<%=soldier.getType()%>" value="$count<%=soldier.getType()%>"/><%}%>
<postfield name="t" value="$atype"/>
<%if(army.getCount(8)!=0){%><postfield name="opt" value="$opt"/><%}%>
<%if(army.getHero()!=0){%><postfield name="countH" value="$countH"/><%}%>
</go></anchor><br/><%}%>
攻击|<a href="<%=("search.jsp?x="+request.getParameter("x")+"&amp;y="+request.getParameter("y"))%>">侦查</a>
<%} else {%>该坐标没有城堡<%}%><br/><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>