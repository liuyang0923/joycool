<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	
	
	CastleBaseAction action = new CastleBaseAction(request);
	int pos = action.getParameterInt("pos");
	BuildingBean bean = action.getCastleService().getBuildingBeanByPos(action.getCastle().getId(),pos);
	if(bean==null){
		response.sendRedirect("ad.jsp");
		return;
	}
	
	//TODO 最高等级检查
	boolean flag = false;
	if(bean.getGrade() >= ResNeed.getBuildingT(bean.getBuildType()).getMaxGrade()) {
		flag = true;
	}
	
	CacheService cacheService = CacheService.getInstance();
	HashMap state = cacheService.getCacheBuildingType(action.getCastle().getId());
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="选择建筑升级"><p><%@include file="top.jsp"%><%
	if(pos < 19 && castleUser.getMain()!=action.getCastle().getId() && bean.getGrade()>=10)
		flag = true;
 if(!flag) {
	 BuildingTBean tb = ResNeed.getBuildingT(bean.getBuildType(),bean.getGrade()+1);
%>【<%=ResNeed.getTypeName(bean.getBuildType())%>】等级<%=bean.getGrade()%>
<%if(state.get(new Integer(pos)) == null) {%>
<%if(castleUser.isSpAccount()) {%>
<%if(CastleUtil.isResEnough(action.getUserResBean(), tb)) {%>
<a href="upgrade.jsp?pos=<%=pos%>">升级</a><br/>
<%} else { %>
资源不足<br/>
SP账号提示:将于<%=DateUtil.formatDate2(System.currentTimeMillis() + CastleUtil.getCanBuildTime(action.getUserResBean(), tb)) %>可以升级<br/>
<%} } else {%>
<a href="upgrade.jsp?pos=<%=pos%>">升级</a><br/>
<%}

} else { %><%=state.get(new Integer(pos)) %><br/><%} %>
<%=ResNeed.getBuildingT(bean.getBuildType()).getInfo()%><br/>
升级所需资源:
木<%=tb.getWood()%>|石<%=tb.getStone()%>|铁<%=tb.getFe()%>|粮<%=tb.getGrain()%><br/>
粮食消耗:<%=tb.getPeople()%>/小时<br/>
所需时间<%=DateUtil.formatTimeInterval2(action.getUserResBean().calcBuildTime(tb.getTime()))%><br/>
<%}else{
%><%=ResNeed.getTypeName(bean.getBuildType())%>已经建造完成<br/>
<%}%>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>
