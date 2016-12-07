<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction" %><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.auction.AuctionBean"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%
response.setHeader("Cache-Control","no-cache");
AuctionAction action = new AuctionAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="拍卖成交价">
<p align="left">
<%=BaseAction.getTop(request, response)%>
最近物品成交价<br/>
<%=action.toString(AuctionAction.log)%>
<a href="auctionHall.jsp">返回拍卖大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>