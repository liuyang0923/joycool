<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int type = action.getParameterInt("t");
	BuildingTBean bt = ResNeed.getBuildingT(type);
	BuildingTBean[] tb = ResNeed.getBuildingsT(type);
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
<a href="builds.jsp">军事</a>|<a href="builds.jsp?t=1">资源</a>|<a href="builds.jsp?t=2">基础</a><br/>
【<%=ResNeed.getTypeName(type)%>】<br/>
<img src="../../cast/img/build/<%=type%>.gif" alt=""/>
<%=bt.getInfo()%><br/>
<%=ResNeed.getBuildingPreString(type)%><br/>
<%
	switch(type){
	case ResNeed.GATHER_BUILD: {
List all = new ArrayList();
for(int i=1;i<=20;i++){
List list = new ArrayList();
list.add(new Integer(i));	// 第一个元素保存集结点级别
for(int j=0;j<=ResNeed.buildingTypeCount;j++)
if(ResNeed.gatherOpt[j]==i) list.add(new Integer(j));
if(list.size()>1) all.add(list);}

%><table class="tbg" cellpadding="2" cellspacing="1" width="900"><tr class="rbg">
<%for(int i=0;i<all.size();i++){
List list = (List)all.get(i);
%><td>等级<%=list.get(0)%></td><%}%>
</tr><tr><%
for(int i=0;i<all.size();i++){%><td><%
List list = (List)all.get(i);
for(int j=1;j<list.size();j++){
%><a href="build.jsp?t=<%=list.get(j)%>"><%=ResNeed.getBuildingT(((Integer)list.get(j)).intValue()).getName()%></a><br/>
<%}%></td>
<%}%></tr></table><br/><%
	} break;
	case ResNeed.MARKET_BUILD: {

%><table class="tbg" cellpadding="2" cellspacing="1" width="400"><tr class="rbg">
<td></td><%for(int i=1;i<4;i++){%><td><%=ResNeed.raceNames[i]%></td><%}%>
</tr><tr>
<td>移动格/小时</td><%for(int i=1;i<4;i++){%><td><%=ResNeed.merchantSpeed[i]%></td><%}%>
</tr><tr>
<td>商人运载量</td><%for(int i=1;i<4;i++){%><td><%=CastleBaseAction.merchantCarry[i]%></td><%}%>
</tr></table><br/><%
	} break;
}
%>
<table class="tbg" cellpadding="2" cellspacing="1" width="450"><tr class="rbg"><td>lvl</td><td><img src="img/1.gif"></td><td><img src="img/2.gif"></td><td><img src="img/3.gif"></td><td><img src="img/4.gif"></td><td><img src="img/5.gif"></td><td>CP</td><td>时间</td><td>V</td></tr>
<%for(int i=1;i<=bt.getMaxGrade();i++){
%><tr><td><%=i%></td>
<td><%=tb[i].getWood()%></td><td><%=tb[i].getStone()%></td><td><%=tb[i].getFe()%></td><td><%=tb[i].getGrain()%></td>
<td><%=tb[i].getPeople()%></td><td><%=tb[i].getCivil()%></td><td>
<%=DateUtil.formatTimeInterval2(tb[i].getTime())%></td><td><%=tb[i].getValue()%></td>
</tr><%}%></table>
<br/>
<%@include file="bottom.jsp"%><br/>
<table class="tbg" cellpadding="2" cellspacing="1" width="750">
<tr class="rbg"><td>lvl</td><%for(int i=1;i<=10;i++){%><td width="150">城堡<%=i%>级</td><%}%></tr>
<%for(int i=1;i<=bt.getMaxGrade();i++){
%><tr><td><%=i%></td>
<%for(int j=1;j<=10;j++){
%><td><%=DateUtil.formatTimeInterval2(ResNeed.getGradeTime(j,tb[i].getTime()))%></td><%}%>
</tr><%}%>
</table><br/>
<table class="tbg" cellpadding="2" cellspacing="1" width="750">
<tr class="rbg"><td>lvl</td><%for(int i=11;i<=20;i++){%><td width="150">城堡<%=i%>级</td><%}%></tr>
<%for(int i=1;i<=bt.getMaxGrade();i++){
%><tr><td><%=i%></td>
<%for(int j=11;j<=20;j++){
%><td><%=DateUtil.formatTimeInterval2(ResNeed.getGradeTime(j,tb[i].getTime()))%></td><%}%>
</tr><%}%>
</table>
</html>