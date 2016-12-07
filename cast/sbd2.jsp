<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	
	
	CastleBaseAction action = new CastleBaseAction(request);
	int type = action.getParameterInt("type");
	int pos = action.getParameterInt("pos");
	BuildingBean bean = action.getCastleService().getBuildingBeanByPos(action.getCastle().getId(),pos);
	CastleUserBean user = CastleUtil.getCastleUser(action.getLoginUser().getId());
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="选择建筑"><p><%@include file="top.jsp"%>
<%
	if(bean == null) {
		BuildingTBean tb = ResNeed.getBuildingT(type,1);
		BuildingTBean bt = ResNeed.getBuildingT(type);
%>
【<%=ResNeed.getTypeName(type)%>】等级0<br/>
<%=bt.getInfo()%><br/>
当前产量<%=ResNeed.RES_BASE%>每小时<br/>
升级后产量<%=ResNeed.RES_BASE/2*5%>每小时<br/>
<%if(action.getUserResBean().canBuild(bt)) {
	if(user.isSpAccount()) {
		if(CastleUtil.isResEnough(action.getUserResBean(), tb)) {%>
<a href="upgrade.jsp?type=<%=type%>&amp;pos=<%=pos%>">开始升级</a><br/>
<%} else { %>
资源不足<br/>
SP账号提示:将于<%=DateUtil.formatDate2(System.currentTimeMillis() + CastleUtil.getCanBuildTime(action.getUserResBean(), tb)) %>可以建造<br/>
<%} } else{%><a href="upgrade.jsp?type=<%=type%>&amp;pos=<%=pos%>">开始升级</a><br/><%}%><%}else{%>未达到建造条件<br/><%}%>

所需资源:木<%=tb.getWood()%>|石<%=tb.getStone()%>|铁<%=tb.getFe()%>|粮<%=tb.getGrain()%><br/>
粮食消耗:<%=tb.getPeople()%>/小时<br/>
所需时间<%=DateUtil.formatTimeInterval2(action.getUserResBean().calcBuildTime(tb.getTime()))%><%
} else {%>
该空地已经有建筑
<%} %>
<br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>