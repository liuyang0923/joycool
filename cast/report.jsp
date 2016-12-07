<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="java.util.*,net.joycool.wap.spec.castle.*,net.joycool.wap.bean.*,net.joycool.wap.framework.*,net.joycool.wap.util.DateUtil"%><%@ page import="java.util.List"%><%
	
	
	CastleAction action = new CastleAction(request);
	CastleUserBean user = action.getCastleUser();
	int id = action.getParameterInt("id");
	CastleMessage message = action.getService().getCastleMessage("id=" + id);
	if(message.getUid()!=user.getUid()&&(message.getTongId()==0||message.getTongId()!=user.getTong())){
		response.sendRedirect("amsg.jsp");
		return;
	}
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="信息">
<p><%@include file="top.jsp"%>
【<%=message.getContent()%>】(<%=DateUtil.sformatTime(message.getTime())%>)<br/>
<%=message.getDetail()%><br/><br/>
<a href="his.jsp">返回信息列表</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>