<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="拍卖系统">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(1,request,response)%>
1、拍卖大厅是乐酷宝物流通地，消费、赚钱，大家各取所需。<br/>
2、物品价格分起价（当前价）和一口价。起价（当前价）是卖家设定的起拍价格，买家在此价格基础上可每次加价10％提高自己的竞价，拍卖结束时竞价最高者获得物品。一口价是卖家设定的一次性买下物品的价格，只要有人选择一口价，物品立即属于买家，卖家同时获得乐币，不用等待拍卖结束。<br/>
3、每件物品的拍卖时间是8小时，剩余时间分别以长、中、短表示。如截止时间无任何人竞价（在起拍价格上加10％），或者一口价购买，物品将被拍卖大厅低价回收。<br/>
4、拍卖大厅里买卖双方均是乐酷用户，拍卖成交后大厅将按低比例收取手续费(最高不会超过1亿)。<br/>
5、卖家的物品是卖给其他用户的，并不是设一个价格就一定会有人买。价低容易卖，价高利润高、卖不掉的风险也高，各位卖家要自己权衡。小窍门：随时关注自己拍卖的物品，如果时间“短”了还无人问津，可以自己竞价回购，降低价格或等待有利行情重新拍卖，避免被系统低价回收。<br/>
<br/>
<a href="auctionHall.jsp">返回拍卖大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>