<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.broadcast.BStatusBean"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ include file="/iwapAlly.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="姚明直播">
<p align="left">
<%--马长青_2006-6-22_增加顶部信息_start--%>
<%=BaseAction.getTop(request, response)%>
<%--马长青_2006-6-22_增加顶部信息_end--%>
NBA赛事直播<br/>
--------<br/>
<%=BStatusBean.line1%><br/>
<%=BStatusBean.line2%><br/>
<a href="Broadcast.do?r=<%=java.util.Calendar.getInstance().getTimeInMillis()%>">刷新</a> （请点击刷新，看最新进展）<br/>
<logic:present name="list" scope="request"><logic:iterate id="element" name="list">
<bean:write name="element" property="broadcaster"/>：<bean:write name="element" property="msg" filter="false"/>(<bean:write name="element" property="time"/>)<br/>
</logic:iterate></logic:present>
<a href="AllBroadcast.do">前面比赛内容》》</a>
<br/>
<%@ include file="/iwapAllyFooter.jsp"%>
<%--马长青_2006-6-22_增加底部信息_start
--%>
<%=BaseAction.getBottom(request, response)%>

<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>