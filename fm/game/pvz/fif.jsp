<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
LinkedList list = vsGame.getFightInformationList();
PagingBean paging = new PagingBean(action,list.size(),10,"p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
for (int i=paging.getStartIndex();i<paging.getEndIndex();i++){
	%><%=i+1%>.<%=list.get(i)%><br/><%
}
%><%=paging.shuzifenye("fif.jsp",false,"|",response)%>
<a href="view.jsp">返回战场</a><br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>