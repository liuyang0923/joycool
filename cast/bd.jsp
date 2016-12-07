<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*"%><%@ page import="java.util.*"%><%!
	static CacheService cacheService = CacheService.getInstance();
%><%
	
	
	CastleBaseAction action = new CastleBaseAction(request);
	List list = action.getCastleService().getAllBuilding(action.getUserBean().getId());
	
	HashMap state = cacheService.getCacheBuildingType(action.getUserBean().getId());
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="建筑建造"><p><%@include file="top.jsp"%><%
		for(int i = 0; i < list.size(); i++) {
			BuildingBean bean = (BuildingBean)list.get(i);
			if(bean.getGrade() > 0) {
%><%=ResNeed.getTypeName(bean.getBuildType())%>(<%=bean.getGrade()%>):<%if(state.get(new Integer(bean.getBuildType())) == null) {%>
<a href="<%=("sUp.jsp?type=" + bean.getBuildType())%>">升级</a><%} else { %><%=state.get(new Integer(bean.getBuildType())) %><%} %><br/>
<%} else {%><%=ResNeed.getTypeName(bean.getBuildType())%>:<%if(state.get(new Integer(bean.getBuildType())) == null) {%>
<a href="<%=("sbd.jsp?type=" + bean.getBuildType())%>">建造</a><%} else { %><%=state.get(new Integer(bean.getBuildType())) %><%} %><br/>
<%}}%>升级中的建筑<a href="ui.jsp">查看</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>