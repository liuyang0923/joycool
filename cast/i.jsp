<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	if(true){
		new CastleAction(request,response).innerRedirect("end.jsp");
		return;
	}
	CastleAction action = new CastleAction(request);
	CastleBean bean = action.start();
	if(bean == null) {
		response.sendRedirect("msg.jsp");
		return;
	}
	if(bean.getMap().length()==0){
		response.sendRedirect("ad.jsp");
		return;
	}
	String[] map = bean.getMap().split(",");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p><%@include file="top.jsp"%>
<%if(CastleUtil.isHrefLink(map[0])) {%>
<a href="<%=("random.jsp?type=" + map[0])%>"><%=CastleUtil.getMapType(map[0])%></a>
<%} else {%><%=CastleUtil.getMapType(map[0])%><%}%>|<%if(CastleUtil.isHrefLink(map[1])) {%><a href="<%=("random.jsp?type=" + map[1])%>"><%=CastleUtil.getMapType(map[1])%></a>
<%} else {%><%=CastleUtil.getMapType(map[1])%><%}%>|<%if(CastleUtil.isHrefLink(map[2])) {%><a href="<%=("random.jsp?type=" + map[2])%>"><%=CastleUtil.getMapType(map[2])%></a>
<%} else {%><%=CastleUtil.getMapType(map[2])%><%}%><br/><%if(CastleUtil.isHrefLink(map[3])) {%>
<a href="<%=("random.jsp?type=" + map[3])%>"><%=CastleUtil.getMapType(map[3])%></a>
<%} else {%><%=CastleUtil.getMapType(map[3])%><%}%>|<a href="<%=("fun.jsp?pos=19")%>">城堡</a>|<%if(CastleUtil.isHrefLink(map[4])) {%>
<a href="<%=("random.jsp?type=" + map[4])%>"><%=CastleUtil.getMapType(map[4])%></a><%} else {%><%=CastleUtil.getMapType(map[4])%><%}%><br/>
<%if(CastleUtil.isHrefLink(map[5])) {%><a href="<%=("random.jsp?type=" + map[5])%>"><%=CastleUtil.getMapType(map[5])%></a>
<%} else {%><%=CastleUtil.getMapType(map[5])%><%}%>|<%if(CastleUtil.isHrefLink(map[6])) {%><a href="<%=("random.jsp?type=" + map[6])%>"><%=CastleUtil.getMapType(map[6])%></a><%} else {%><%=CastleUtil.getMapType(map[6])%><%}%>|<%if(CastleUtil.isHrefLink(map[7])) {%><a href="<%=("random.jsp?type=" + map[7])%>"><%=CastleUtil.getMapType(map[7])%></a>
<%} else {%><%=CastleUtil.getMapType(map[7])%>
<%}%><br/><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>