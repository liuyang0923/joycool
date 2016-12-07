<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	CastleAction action = new CastleAction(request);
	
	int id = action.getParameterIntS("id");
	if(id <= 0 || id > CastleAction.affect.length){
		response.sendRedirect("shop.jsp");
		return;
	}
	action.shopBuy();
	CastleUserBean user = CastleUtil.getCastleUser(action.getLoginUser().getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p><%@include file="top.jsp"%>
<%if(request.getAttribute("msg") != null) {%><%=request.getAttribute("msg") %><br/><%} else { %>
拥有<%=user.getGold()%>个金币<br/>
<%if(request.getAttribute("sp") != null) {%><%=request.getAttribute("sp") %><br/><%} %>
价格:<%=CastleAction.shopPrice[id]%>金币<a href="buy.jsp?id=<%=id%>&amp;a=buy">购买</a><br/>
<%=(CastleAction.affect[id]).replace("立即完成正在建造的建筑","立即完成当前城堡正在建造的建筑<br/>注意:请选择正确的城堡,本功能只对当前单个城堡有效.<br/>本功能对皇宫/行宫以及奇迹城的所有建筑无效.") %><%if(CastleAction.shopTime[id] != 0) {%>(<%=CastleAction.shopTime[id]%>天)<%} %><br/>
<%} %>
<a href="shop.jsp?id=<%=id%>">返回商城</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>