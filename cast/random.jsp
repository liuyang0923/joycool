<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	
	
	CastleAction action = new CastleAction(request);
	CastleBean bean = action.getCastle();
	int type = action.getParameterInt("type");
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="随机资源"><p>
<%@include file="top.jsp"%><%
		switch(type) {
			case 0:
%>树林<br/>您的城市附近有树林能增加你的小部分木头产量<br/>
<%		
			break;
			case 1:
%>田野<br/>您的城市附近有田野能增加你的小部分粮食产量<br/>
<%
			break;
			case 2:
%>矿藏<br/>您的城市附近有矿藏能增加你的小部分矿藏产量<br/>
<%		
			break;
			case 4:
%>山脉<br/>您的城市附近有山脉能增加你的小部分石头产量<br/>
<%
		}
%><br/>本城信息:<br/>坐标:<%=bean.getX()%>|<%=bean.getY()%><br/>城堡:<%=StringUtil.toWml(bean.getCastleName())%><br/>城主:<%=StringUtil.toWml(action.getCastleUser().getName())%><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>