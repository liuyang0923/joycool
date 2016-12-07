<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wc.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
action.answer();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<logic:present name="result" scope="request">

    <logic:equal name="result" value="failure">
<card title="博彩下注">
<p align="left">
<%=BaseAction.getTop(request, response)%>
博彩下注<br/>
--------<br/>
    <bean:write name="tip" filter="false"/><br/>
	<anchor title="back"><prev/>返回下注页面</anchor><br/>
	<a href="http://wap.joycool.net/forum/forumIndex.jsp?id=13">返回世界杯论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>

    <logic:equal name="result" value="success">
<card title="博彩下注" ontimer="<%=response.encodeURL("index.jsp")%>">
    <timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
博彩下注<br/>
--------<br/>
下注成功！比赛结束时中奖用户将会收到系统通知！3秒后自动返回。<br/>
<a href="http://wap.joycool.net/forum/forumIndex.jsp?id=13">返回世界杯论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
</logic:present>