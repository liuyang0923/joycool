<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.castle.*"%><%@ page import="net.joycool.wap.util.ForbidUtil"%><%
	
	
	CastleAction action = new CastleAction(request);
	CastleBean bean = action.start();
	if(bean == null) {
		response.sendRedirect("msg.jsp");
		return;
	}
	action.curPage = 2;
	action.setAttribute2("casSwi","ad.jsp");
	int[] buildAdvancePos = action.getCastleService().getAdvanceBuild(bean.getId());
	int[] buildPos = action.getCastleService().getBuildPos(bean.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p>
<%@include file="top.jsp"%><%
if(bean.isNatar()){

%>【<%if(buildAdvancePos[19]==0){%><a href="sbd.jsp?type=38&amp;pos=19">世界奇迹</a><%}else{%><a href="fun.jsp?pos=19">世界奇迹<%=buildAdvancePos[19]%></a><%}%>】.<%
%><%if(buildAdvancePos[20]==0){%><a href="sbd.jsp?type=22&amp;pos=20">集结点</a><%}else{%><a href="fun.jsp?pos=20">集结点<%=buildAdvancePos[20]%></a><%}%><br/><%
%><%if(buildAdvancePos[21]==0){%><a href="sbd.jsp?type=4&amp;pos=21">城堡</a><%}else{%><a href="fun.jsp?pos=21">城堡<%=buildAdvancePos[21]%></a><%}%>.<%
	for(int i = 22; i <= 36; i ++) {
		if(buildPos[i] > 0){
%><a href="fun.jsp?pos=<%=i %>"><%=ResNeed.getTypeName(buildPos[i])%><%=buildAdvancePos[i]%></a><%
	} else {
%><a href="build.jsp?pos=<%=i %>">空地</a><%
	}if((i)%4 == 0) {%><br/><%}else{%>.<%}%><%}%><br/><%

}else{

	for(int i = 19; i <= 40; i ++) {
		if(buildPos[i] > 0){
%><a href="fun.jsp?pos=<%=i %>"><%=ResNeed.getTypeName(buildPos[i])%><%=buildAdvancePos[i]%></a><%
	} else {
%><a href="build.jsp?pos=<%=i %>">空地</a><%
	}if((i)%4 == 2) {%><br/><%}else{%>.<%}%><%}%><br/><%

}

%>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>