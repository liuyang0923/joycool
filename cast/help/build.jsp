<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	
	
	CustomAction action = new CustomAction(request);
	int type = action.getParameterInt("t");
	BuildingTBean bt = ResNeed.getBuildingT(type);
	BuildingTBean tb = ResNeed.getBuildingT(type,1);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="建筑"><p>
<a href="builds.jsp">军事</a>|<a href="builds.jsp?t=1">资源</a>|<a href="builds.jsp?t=2">基础</a><br/>
【<%=ResNeed.getTypeName(type)%>】<br/>
<img src="/cast/img/build/<%=type%>.gif" alt="pic"/>
<%=bt.getInfo()%><br/>
<%=ResNeed.getBuildingPreString(type)%><br/>
等级1的开发成本:<br/>
所需资源:木<%=tb.getWood()%>|石<%=tb.getStone()%>|铁<%=tb.getFe()%>|粮<%=tb.getGrain()%><br/>
粮食消耗:<%=tb.getPeople()%>/小时<br/>
所需时间<%=DateUtil.formatTimeInterval2(tb.getTime())%><br/>
<a href="builds.jsp">返回建筑列表</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>