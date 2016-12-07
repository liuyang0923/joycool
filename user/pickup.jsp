<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.LuckyAction" %><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.auction.AuctionBean"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%
response.setHeader("Cache-Control","no-cache");
LuckyAction action = new LuckyAction(request);
action.pickup();
String backTo = request.getParameter("back");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="幸运">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="userBag.jsp">查看行囊</a><br/>
<%if(backTo!=null){%><a href="<%=(backTo.replace("&","&amp;"))%>">返回</a><br/><%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>