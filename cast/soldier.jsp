<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	

	CastleAction action = new CastleAction(request);
	
	int type = action.getParameterInt("t");
	int race = action.getCastleUser().getRace();
	SoldierResBean soldier = ResNeed.getSoldierRes(race,type);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="兵种说明"><p><%@include file="top.jsp"%>
【<%=soldier.getSoldierName()%>】<br/>
<%=soldier.getInfo()%><br/>
训练所需资源:木<%=soldier.getWood()%>|石<%=soldier.getStone()%>|铁<%=soldier.getFe()%>|粮<%=soldier.getGrain()%><br/>
所需时间<%=DateUtil.formatTimeInterval2(soldier.getTime())%><br/>
粮食消耗:<%=soldier.getPeople()%>/小时<br/>
速度:<%=soldier.getSpeed()%>格/小时<br/>
运载量:<%=soldier.getStore()%><br/>
近战攻击:<%=soldier.getAttack()%>|近战防御:<%=soldier.getDefense()%><br/>
远程攻击:<%=soldier.getAttack2()%>|远程防御:<%=soldier.getDefense2()%><br/>
<%=ResNeed.getResearchPreString(race,type)%><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>