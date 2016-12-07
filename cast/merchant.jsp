<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	
	MerchantAction action = new MerchantAction(request);
	int x = action.getParameterIntS("x");
	int y = action.getParameterIntS("y");
	CastleBean castleBean = null;
	if(x<0||y<0||x>=CastleUtil.mapSize||y>=CastleUtil.mapSize)
		request.setAttribute("msg","请输入正确的坐标!");
	else {
		action.send();
		int castleId = CastleUtil.getMapCastleId(x, y);
		if(castleId != 0) {
			castleBean = CastleUtil.getCastleById(castleId);
		}
	}
	CastleBean castle = action.getCastle();
	int buildGrade = action.getUserResBean().getBuildingGrade(ResNeed.MARKET_BUILD);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="派出商人"><p>
<%@include file="top.jsp"%>
<%if(request.getAttribute("msg") != null) {%><%=request.getAttribute("msg") %><br/>
<%}
if(castleBean != null) {
	if(buildGrade > 0) {
%>【派商人至】<%=request.getParameter("x")%>|<%=request.getParameter("y")%><br/>
剩余商人<%=buildGrade - action.getUserResBean().getMerchant() %>
可携带<%=(buildGrade - action.getUserResBean().getMerchant())*action.getMerchantCarry() %>资源<br/>
距离:<%=StringUtil.numberFormat(CastleUtil.calcDistance((castle.getX()-x),(castle.getY()-y)))%>格<br/>
输入资源数量:<br/>
木:<input name="w" format="*N"/><br/>
石:<input name="s" format="*N"/><br/>
铁:<input name="f" format="*N"/><br/>
粮:<input name="g" format="*N"/><br/>
<anchor>确认派出商人<go href="merchant.jsp?a=go&amp;x=<%=x%>&amp;y=<%=y%>" method="post">
<postfield name="w" value="$w"/>
<postfield name="s" value="$s"/>
<postfield name="f" value="$f"/>
<postfield name="g" value="$g"/>
</go></anchor><br/>
<a href="fun.jsp?t=17">返回市场</a>
<%} else {%>您还没有建造市场<%}%>
<%} else {%>该坐标没有城堡<br/><a href="fun.jsp?t=17">返回市场</a><%}%><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>