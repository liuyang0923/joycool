<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file="/iwapAlly.jsp"%>
<%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="姚明直播">
<p align="left">

<%--马长青_2006-6-22_增加顶部信息_start--%>
<%=BaseAction.getTop(request, response)%>
<%--马长青_2006-6-22_增加顶部信息_end--%>
<%=title%>NBA赛事直播<br/>
--------<br/>
<logic:present name="list" scope="request"><logic:iterate id="element" name="list">
<bean:write name="element" property="broadcaster"/>：<bean:write name="element" property="msg" filter="false"/>(<bean:write name="element" property="time"/>)<br/>
</logic:iterate></logic:present>
<%
int total = ((Integer)request.getAttribute("total")).intValue();
int curPage = ((Integer)request.getAttribute("page")).intValue();
%>
<%if(curPage > 0){%>
	<a href="AllBroadcast.do?page=<%=Integer.toString(curPage-1)%>">上一页</a>
<%}%>
<%if((curPage + 1) * 10 < total){%>
	<a href="AllBroadcast.do?page=<%=Integer.toString(curPage+1)%>">下一页</a>
<%}%>
<a href="Broadcast.do">返回直播间</a>
<br/>
<%@ include file="/iwapAllyFooter.jsp"%>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>