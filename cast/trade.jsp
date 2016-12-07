<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	
	MerchantAction action = new MerchantAction(request);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="接受交易"><p><%
if(action.hasParam("c")||action.getParameterInt("s")==2){
action.trade();
%><%@include file="top.jsp"%>
<%if(action.isResult("tip")) {%><%=action.getTip()%><br/><%}%>
<%}else{
int id = action.getParameterInt("id");
TradeBean trade = action.getService().getTradeBean("id=" + id);
if(trade==null){%>交易已经过期<br/><%}else{
int needTime = (int)(action.getCastle().calcDistance(trade.getX(),trade.getY()) * 3600 / 16);
%>对方:<a href="search.jsp?cid=<%=trade.getCid()%>"><%=trade.getX()%>|<%=trade.getY()%></a><br/>
对方想用<%=trade.getSupplyName()%><%=trade.getSupplyRes()%>换取我方<%=trade.getNeedName()%><%=trade.getNeedRes()%><br/>
需时:<%=DateUtil.formatTimeInterval2(needTime)%><br/>
<a href="trade.jsp?c=1&amp;id=<%=id%>">确认接受交易</a><br/>
<%}}%>
<a href="fun.jsp?t=17&amp;s=<%=action.getParameterInt("s")%>">返回市场</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>