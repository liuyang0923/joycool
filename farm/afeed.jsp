<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.afeed();
FarmFeedBean feed = (FarmFeedBean)request.getAttribute("feed");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源农场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<%if(feed!=null){%>
<a href="feed.jsp?id=<%=feed.getId()%>">返回</a><br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>