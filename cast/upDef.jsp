<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static String[] msg = {"士兵武器开始升级","士兵防具开始升级","兵种开始研发"};
%><%
	

	CasernAction action = new CasernAction(request);
	int type = action.getParameterInt("t");
	if(type < 0 || type>2) type=2;
	boolean flag = action.defenceUpgrade();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="攻防升级"><p><%@include file="top.jsp"%>
<%if(flag) {%><%=msg[type]%><br/>
<%} else {%><%=request.getAttribute("msg")%><br/>
<%}%><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>