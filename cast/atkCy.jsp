<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	

	CastleAction action = new CastleAction(request);
	if(action.getParameterInt("t")!=4||action.getCastleUser().getCastleCount()<ResNeed.maxCastleCount){
	if(action.hasParam("oasis"))
		action.attack2();
	else
		action.attack();
	}else{
		request.setAttribute("msg","已经达到本阶段城堡数上限,请等待下一阶段的开放.");
	}
	String msg = (String)request.getAttribute("msg");
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="攻击"><p>
<%@include file="top.jsp"%>
<%if(msg!=null){%><%=msg%><br/><%}
AttackThreadBean attack = (AttackThreadBean)request.getAttribute("attack");
if(attack!=null){

if(action.hasParam("oasis")){
	%>对绿洲<a href="search.jsp?x=<%=attack.getX() %>&amp;y=<%=attack.getY() %>"><%=attack.getX() %>|<%=attack.getY() %></a>的<a href="army2.jsp?id=<%=attack.getId()%>"><%=attack.getTypeName()%></a>已发出<br/><%
}else{
	if(attack.getCid()!=0){
		%>对<a href="search.jsp?x=<%=attack.getX() %>&amp;y=<%=attack.getY() %>"><%=attack.getX() %>|<%=attack.getY() %></a>的<a href="army2.jsp?id=<%=attack.getId()%>"><%=attack.getTypeName()%></a>已发出<br/><%
	}else{
		%>向<a href="search.jsp?x=<%=attack.getX() %>&amp;y=<%=attack.getY() %>"><%=attack.getX() %>|<%=attack.getY() %></a>的空地的进军已发出<br/><%
	}
}
int dx = attack.getX()-action.getCastle().getX();
int dy = attack.getY()-action.getCastle().getY();
if(attack.getOpt()!=0){
%>投石车目标:<%=ResNeed.getTypeName(attack.getOpt())%><br/><%}
%>距离:<%=StringUtil.numberFormat(CastleUtil.calcDistance(dx,dy))%>格<br/>
剩余<%=DateUtil.formatTimeInterval2(attack.getEndTime())%>-<a href="cancelA.jsp?id=<%=attack.getId()%>">取消</a><br/><%
}%><a href="fun.jsp?t=22">返回集结点</a><br/><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>