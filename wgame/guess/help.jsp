<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=GuessAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(7,request,response)%>
游戏规则<br/>
=================<br/>
您的目标是尽快得到全A成绩！<br/>
每次游戏系统随机给你一个四位数字，这四位数中没有重复数字（0-9都只出现一次），你来猜这个四位数字是什么。如果数字和位置都对得一个A，数字对位置不对得一个B。在10次机会内猜对的有乐币和经验的奖励，猜对用的次数越少奖励就越多。<br/>
例如，正确答案为1635，如果输入1234，由于其中的1和3与正确答案中位置和数字完全一致，所以提示“2A0B”，就是有两个全对；如果输入1324，由于其中1与正确答案中位置和数字完全一致，3与正确答案中数字一致位置不一致，所以提示“1A1B”，有一个全对，一个半对。<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>