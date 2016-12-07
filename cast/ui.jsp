<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*"%><%@ page import="java.util.*,net.joycool.wap.util.*"%><%
	
	BuildingAction action = new BuildingAction(request);
	CastleBean bean = action.getCastle();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="建筑升级"><p><%@include file="top.jsp"%>
<%
		CacheService cacheService = CacheService.getInstance();
		List cacheList = cacheService.getCacheBuildingByUid(action.getUserBean().getId());
		if(cacheList == null || cacheList.size() == 0) {
%>暂无建造或正在升级的建筑<br/>
<%} else {Iterator iterator = cacheList.iterator();
	while(iterator.hasNext()){
		BuildingThreadBean cacheBuildingBean = (BuildingThreadBean)iterator.next();
%>建筑种类：<%=ResNeed.getTypeName(cacheBuildingBean.getType())%><br/>
建筑状态：<%=cacheBuildingBean.getState()%><br/>
剩余时间：<%=cacheBuildingBean.getTimeLeft()%><br/>
建筑等级：<%=cacheBuildingBean.getGrade()%><br/>
<a href="<%=("cancel.jsp?id="+cacheBuildingBean.getId())%>" >取消</a><br/>
-----------------<br/>
<%}}%><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>