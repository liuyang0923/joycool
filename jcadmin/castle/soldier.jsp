<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");

	CustomAction action = new CustomAction(request);
	
	int type = action.getParameterInt("t");
	int race = action.getParameterInt("r");
	SoldierResBean soldier = ResNeed.getSoldierRes(race,type);
	int imgId = soldier.getId();
	if(imgId%10==4||imgId%10==5) imgId++; else if(imgId%10==6) imgId -=2;
%><html>
	<head>
	</head>
<link href="common2.css" rel="stylesheet" type="text/css">
	<body>
<a href="soldiers.jsp">拜索斯</a>|<a href="soldiers.jsp?t=1">伊斯</a>|<a href="soldiers.jsp?t=2">杰彭</a>|<a href="soldiers.jsp?t=3">自然界</a>|<a href="soldiers.jsp?t=4">纳塔</a><br/>
【<img class="unit u<%=imgId%>" src="img/blank.gif">&emsp;<%=soldier.getSoldierName()%>】<br/>
<%=soldier.getInfo()%><br/>
<table class="tbg" cellpadding="2" cellspacing="1" style="width:450px;"><tr class="rbg"><td><img src="img/att.gif"></td>
<td><img src="img/att.gif"></td>
<td><img src="img/def1.gif"></td>
<td><img src="img/def2.gif"></td>
<td><img src="img/1.gif"></td>
<td><img src="img/2.gif"></td>
<td><img src="img/3.gif"></td>
<td><img src="img/4.gif"></td>
<td><img src="img/5.gif"></td>
<td><img src="img/clock.gif"></td>
</tr>
<tr>
<td><%=soldier.getAttack()%></td>
<td><%=soldier.getAttack2()%></td>
<td><%=soldier.getDefense()%></td>
<td><%=soldier.getDefense2()%></td>
<td><%=soldier.getWood()%></td><td><%=soldier.getStone()%></td><td><%=soldier.getFe()%></td><td><%=soldier.getGrain()%></td>
<td><%=soldier.getPeople()%></td>
<td><%=DateUtil.formatTimeInterval2(soldier.getTime())%></td>
</tr></table>
速度:<%=soldier.getSpeed()%>格/小时<br/>
运载量:<%=soldier.getStore()%><br/>
<%=ResNeed.getResearchPreString(race,type)%><br/>
<%if(!soldier.isFlagResearched()){%>
<table class="tbg" cellpadding="2" cellspacing="1" style="width:280px;"><tr class="rbg">
<td><img src="img/1.gif"></td>
<td><img src="img/2.gif"></td>
<td><img src="img/3.gif"></td>
<td><img src="img/4.gif"></td>
<td><img src="img/clock.gif"></td>
</tr>
<tr>
<td><%=soldier.getWood2()%></td><td><%=soldier.getStone2()%></td><td><%=soldier.getFe2()%></td><td><%=soldier.getGrain2()%></td>
<td><%=DateUtil.formatTimeInterval2(soldier.getTime2())%></td>
</tr></table><%}%>
<%if(!soldier.isFlagNoUpgrade()){%>
<table class="tbg" cellpadding="2" cellspacing="1" style="width:320px;"><tr class="rbg">
<td>lvl</td>
<td><img src="img/1.gif"></td>
<td><img src="img/2.gif"></td>
<td><img src="img/3.gif"></td>
<td><img src="img/4.gif"></td>
<td><img src="img/clock.gif"></td>
</tr>
<%for(int i=1;i<=20;i++){
ResTBean rest = ResNeed.getDefenceResource(race, type, i);
%><tr>
<td><%=i%></td><td><%=rest.getWood()%></td><td><%=rest.getStone()%></td><td><%=rest.getFe()%></td><td><%=rest.getGrain()%></td>
<td><%=DateUtil.formatTimeInterval2(ResNeed.getGradeTime(1,rest.getTime()))%></td>
</tr><%}%></table><%}%>
<br/>
<%@include file="bottom.jsp"%>
</html>