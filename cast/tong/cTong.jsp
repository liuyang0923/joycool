<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	
	TongAction action = new TongAction(request);
	action.createTong();
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟"><p><%@include file="top.jsp"%>
<%if(request.getAttribute("msg") != null) {%><%=request.getAttribute("msg")%><br/><%} %>
<%if(action.getUserResBean().getBuildingGrade(ResNeed.EMMBASSY_BUILD) >= 3 
		&& CastleUtil.getCastleUser(action.getUserBean().getId()).getTong() == 0){
%>联盟名字：<input name="n"/><br/>
<anchor>创建联盟<go href="cTong.jsp?a=g&amp;pos=<%=request.getParameter("pos") %>" method="post">
<postfield name="n" value="$n"/>
</go></anchor><br/>
<%}else if(action.getUserResBean().getBuildingGrade(ResNeed.EMMBASSY_BUILD) < 3 && !action.hasParam("a")){ %>大使馆需要3级才能创建联盟<br/>
<%}else if(CastleUtil.getCastleUser(action.getUserBean().getId()).getTong() != 0 && !action.hasParam("a")){ %>你已经加入联盟<br/><%} %>
<%if(action.hasParam("pos")) {%>
<a href="tongM.jsp">返回联盟管理</a><br/>
<%} %>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>