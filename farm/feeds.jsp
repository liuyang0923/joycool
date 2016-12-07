<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.feeds();
List feeds = (List)request.getAttribute("feeds");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源农场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
for(int i=0;i<feeds.size();i++){
FarmFeedBean feed = (FarmFeedBean)feeds.get(i);
if(feed.getCropId()>0) {
FarmCropBean crop = action.world.getCrop(feed.getCropId());
%>
<a href="feed.jsp?id=<%=feed.getId()%>"><%=crop.getName()%></a><br/>
<%}else{%>
<a href="feed.jsp?id=<%=feed.getId()%>">空地</a><br/>
<%}%>
<%}%>
<br/>
<a href="afeed.jsp">开辟一块养殖场</a><br/><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>