<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.feed();
FarmUserBean user = action.getUser();
FarmFeedBean feed = (FarmFeedBean)request.getAttribute("feed");
FarmCropBean crop = feed.getCrop();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源农场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(crop!=null){%>
<%=crop.getName()%><br/>
<%if(feed.canHarvest(action.now)){%>
<a href="act2.jsp?a=2&amp;id=<%=feed.getId()%>">收获</a><br/>
<%}else{%>
<%if(feed.canAct(action.now)){%>
<a href="act2.jsp?a=3&amp;id=<%=feed.getId()%>">喂食</a><br/>
<%}else{%>
收获(还需<%=DateUtil.formatTimeInterval(feed.actTimeLeft(action.now))%>)<br/>
<%}%>
喂食(还需<%=DateUtil.formatTimeInterval(feed.harvestTimeLeft(action.now))%>)<br/>
<%}%>
<%}else{%>
<a href="plant2.jsp?id=<%=feed.getId()%>">养！</a><br/>
<%}%>
<a href="feeds.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>