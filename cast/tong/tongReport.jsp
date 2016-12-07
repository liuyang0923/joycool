<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="java.util.*,net.joycool.wap.spec.castle.*,net.joycool.wap.bean.*,net.joycool.wap.framework.*,net.joycool.wap.util.DateUtil"%><%@ page import="java.util.List"%><%
	
	
	CastleAction action = new CastleAction(request);
	int tongId = action.getCastleUser().getTong();
	TongBean tong = CastleUtil.getTong(tongId);
	if(tong==null){
		response.sendRedirect("tong.jsp");
		return;
	}
	List list = tong.getReports();
	PagingBean paging = new PagingBean(action, list.size(), 6, "p");

%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟战报"><p><%@include file="top.jsp"%>
<%
	int startIndex = paging.getStartIndex();
	int endIndex = paging.getEndIndex();
	for(int i = startIndex; i < endIndex; i++) {
		CastleMessage message = (CastleMessage)list.get(i);
%><%=i+1%>.<%if(!message.hasDetail()){%><%=message.getContent()%><%}else{
%><a href="../report.jsp?id=<%=message.getId()%>"><%=message.getContent()%></a><%}%><br/>
<%}%><%=paging.shuzifenye("tongReport.jsp", false, "|", response)%>
<a href="tong.jsp">返回联盟</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>