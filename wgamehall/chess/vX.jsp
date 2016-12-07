<%@ page contentType="application/vnd.wap.xhtml+xml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page errorPage="../failure.jsp"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发表主题</title>
</head>
<body>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="#c">车</a><a href="#c">马</a><a href="#c">象</a><a href="#c">士</a><a href="#c">将</a><a href="#c">士</a><a href="#c">象</a><a href="#c">马</a><a href="#c">车</a><br/>
├┼┼┼┼┼┼┼┤<br/>
├<a href="#c">炮</a>┼┼┼┼┼<a href="#c">炮</a>┤<br/>
<a href="#c">卒</a>┼<a href="#c">卒</a>┼<a href="#c">卒</a>┼<a href="#c">卒</a>┼<a href="#c">卒</a><br/>
└┴┴┴┴┴┴┴┘<br/>
┌┬┬┬┬┬┬┬┐<br/>
<a href="<%=response.encodeURL("vX.jsp")%>">兵</a>┼<a href="<%=response.encodeURL("vX.jsp")%>">兵</a>┼<a href="<%=response.encodeURL("vX.jsp")%>">兵</a>┼<a href="<%=response.encodeURL("vX.jsp")%>">兵</a>┼<a href="<%=response.encodeURL("vX.jsp")%>">兵</a><br/>
├<a href="<%=response.encodeURL("vX.jsp")%>">炮</a>┼┼<a href="<%=response.encodeURL("vX.jsp")%>">炮</a>┼┼┼┤<br/>
├┼┼┼┼┼┼┼┤<br/>
<a href="<%=response.encodeURL("vX.jsp")%>">车</a><a href="<%=response.encodeURL("vX.jsp")%>">马</a><a href="<%=response.encodeURL("vX.jsp")%>">相</a><a href="<%=response.encodeURL("vX.jsp")%>">仕</a><a href="<%=response.encodeURL("vX.jsp")%>">帅</a><a href="<%=response.encodeURL("vX.jsp")%>">仕</a><a href="<%=response.encodeURL("vX.jsp")%>">相</a><a href="<%=response.encodeURL("vX.jsp")%>">马</a><a href="<%=response.encodeURL("vX.jsp")%>">车</a><br/>
</p>
</body>
</html>