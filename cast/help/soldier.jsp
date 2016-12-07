<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	

	CustomAction action = new CustomAction(request);
	
	int type = action.getParameterInt("t");
	int race = action.getParameterInt("r");
	if(race>5||race<1) race=1;
	SoldierResBean soldier = ResNeed.getSoldierRes(race,type);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="兵种介绍"><p>
<a href="soldiers.jsp">拜索斯</a>|<a href="soldiers.jsp?t=1">伊斯</a>|<a href="soldiers.jsp?t=2">杰彭</a>|<a href="soldiers.jsp?t=3">自然界</a>|<a href="soldiers.jsp?t=4">纳塔</a><br/>
【<%=soldier.getSoldierName()%>】<br/>
<%=soldier.getInfo()%><br/>
<%if(race==5){

%>训练所需资源:木?|石?|铁?|粮?<br/>
所需时间?<br/>
粮食消耗:?/小时<br/>
速度:?格/小时<br/>
运载量:?<br/>
近战攻击:<%=soldier.getAttack()%>|近战防御:<%=soldier.getDefense()%><br/>
远程攻击:<%=soldier.getAttack2()%>|远程防御:<%=soldier.getDefense2()%><br/>
研发条件:?<br/><%

}else{

%>训练所需资源:木<%=soldier.getWood()%>|石<%=soldier.getStone()%>|铁<%=soldier.getFe()%>|粮<%=soldier.getGrain()%><br/>
所需时间<%=DateUtil.formatTimeInterval2(soldier.getTime())%><br/>
粮食消耗:<%=soldier.getPeople()%>/小时<br/>
速度:<%=soldier.getSpeed()%>格/小时<br/>
运载量:<%=soldier.getStore()%><br/>
近战攻击:<%=soldier.getAttack()%>|近战防御:<%=soldier.getDefense()%><br/>
远程攻击:<%=soldier.getAttack2()%>|远程防御:<%=soldier.getDefense2()%><br/>
<%=ResNeed.getResearchPreString(race,type)%><br/><%

}%>



<a href="soldiers.jsp">返回兵种列表</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>