<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	
	CustomAction action = new CustomAction(request);
	int type = action.getParameterInt("t")+1;
	if(type>5||type<1) type=1;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="种族介绍"><p>
<%if(type==1){%>拜索斯<%}else{%><a href="race.jsp">拜索斯</a><%}%>|
<%if(type==2){%>伊斯<%}else{%><a href="race.jsp?t=1">伊斯</a><%}%>|
<%if(type==3){%>杰彭<%}else{%><a href="race.jsp?t=2">杰彭</a><%}%>|
<%if(type==5){%>纳塔<%}else{%><a href="race.jsp?t=4">纳塔</a><%}%><br/>
【种族介绍-<%=ResNeed.raceNames[type]%>】<br/>
<%=ResNeed.raceInfos[type]%><br/>
【种族特色】<br/>
<%=ResNeed.raceAdvs[type]%><br/>
<a href="help.jsp">返回详细游戏指南</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>