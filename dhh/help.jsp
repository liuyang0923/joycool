<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(4,request,response)%>
详细游戏指南<br/>
游戏介绍：操作自己的一条船，来回沿海港口间进行特产贸易，想结束游戏时可以用海币换乐币!<br/>
港口：港口有交易所、银行、造船厂、出航所，还能保存游戏和兑换乐币。<br/>
交易所：买卖货物的地方，价格根据买卖情况浮动,买的人多价格上涨,卖的人多价格下跌。每种货物都有库存量，买完之后需要时间恢复才能继续购买。<br/>
银行：存取海币的地方，多存钱可以避免航行损失。<br/>
造船厂：出售各种船只的地方。<br/>
出航所：向其他港口航行出发的地方，可以向附近的港口出发。<br/>
航行介绍：航行过程中水手需要消耗补给费，另外还可能发生一些事件，有吉有凶。<br/>
船只介绍：每人只能拥有一条船，船只种类不同，速度、装载量和水手数都不同。速度快的航程消耗天数少，装载量就是装货物的数量，水手越多航程消耗的补给钱就越多。<br/>
保存游戏：在港口中大家可以保存自己的游戏，下次上线可以继续。注意:需要船上没有货物。<br/>
游戏币换乐币：选择游戏结束时，航海资金会自动兑换成乐币。同时会将游戏成绩计入游戏的最近排行榜和总排行榜。<br/>
<a href="play.jsp">马上开始航海！</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>