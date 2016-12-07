<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction"%><%@ page import="net.joycool.wap.bean.auction.AuctionBean" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
AuctionAction action = new AuctionAction(request);
action.buy(request);
String result =(String)request.getAttribute("result");
String url=("auctionHall.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="购买物品" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转拍卖大厅!)<br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
AuctionBean auction=(AuctionBean)request.getAttribute("auction");
DummyProductBean dummyProduct =(DummyProductBean)request.getAttribute("dummyProduct");
UserBean leftUser = UserInfoUtil.getUser(auction.getLeftUserId());
UserBean rightUser = UserInfoUtil.getUser(auction.getRightUserId());
%>
<card title="购买物品" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=dummyProduct.getName()%>(<%=auction.getTime()%><%if(dummyProduct.getTime() > 1){%>/<%=dummyProduct.getTime()%><%}%>)<br/>
<%=dummyProduct.getDescription()%><br/>
<%if(rightUser!=null){%>竞价人：<a href="/user/ViewUserInfo.do?userId=<%=rightUser.getId()%>"><%=StringUtil.toWml(rightUser.getNickName())%></a><br/><%}%>
现价:<%=StringUtil.bigNumberFormat2(auction.getCurrentPrice())%>乐币-
<a href="buyResult.jsp?type=1&amp;auctionId=<%=auction.getId()%>">确认竞价</a><br/>
<a href="buy.jsp?auctionId=<%=auction.getId()%>">返回</a><br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<a href="auctionHall.jsp">拍卖大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>