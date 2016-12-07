<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="java.util.*,net.joycool.wap.spec.castle.*,net.joycool.wap.bean.*,net.joycool.wap.framework.*,net.joycool.wap.util.DateUtil"%><%@ page import="java.util.List"%><%
	
	
	CastleAction action = new CastleAction(request);
	int uid = action.getUserBean().getId();
	// action.getCastleService().getCountMessageByUid(uid);
	PagingBean paging = new PagingBean(action, 1000, 10, "p");
	
	List list = action.getCastleService().getCastleMessageByUid(action.getUserBean().getId(), paging.getStartIndex(), paging.getCountPerPage());
	
	if(list.size()<10)
		paging.setTotalPageCount(paging.getCurrentPageIndex());
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="历史记录"><p>
<%@include file="top.jsp"%>
<%
	for(int i = 0; i < list.size(); i++) {
		CastleMessage message = (CastleMessage)list.get(i);
%><%=DateUtil.converDateToBefore(message.getTime())%>:<%if(!message.hasDetail()){%><%=message.getContent()%><%}else{
%><a href="report.jsp?id=<%=message.getId()%>"><%=message.getContent()%></a><%}%><br/>
<%}%><%=paging.shuzifenye("his.jsp", false, "|", response)%>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>