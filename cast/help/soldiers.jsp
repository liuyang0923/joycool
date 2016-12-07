<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	
	CustomAction action = new CustomAction(request);
	int type = action.getParameterInt("t")+1;
	if(type>5||type<1) type=1;
	SoldierResBean[] so = ResNeed.getSoldierTs(type);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="兵种介绍"><p>
【兵种介绍】<br/>
<%if(type==1){%>拜索斯<%}else{%><a href="soldiers.jsp">拜索斯</a><%}%>|
<%if(type==2){%>伊斯<%}else{%><a href="soldiers.jsp?t=1">伊斯</a><%}%>|
<%if(type==3){%>杰彭<%}else{%><a href="soldiers.jsp?t=2">杰彭</a><%}%>|
<%if(type==4){%>自然界<%}else{%><a href="soldiers.jsp?t=3">自然界</a><%}%>|
<%if(type==5){%>纳塔<%}else{%><a href="soldiers.jsp?t=4">纳塔</a><%}%><br/>
<%for(int i=1;i < so.length;i++){
	SoldierResBean s = so[i];
	if(s==null) continue;
%><%=i%>.<a href="soldier.jsp?t=<%=i%>&amp;r=<%=type%>"><%=s.getSoldierName()%></a><br/>
<%}%>
<a href="help.jsp">返回详细游戏指南</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>