<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游戏帮助"><p><%=BaseAction.getTop(request, response)%>
【游戏简介】<br/>
通过把好友当作“奴隶”进行买卖，赚取越来越多的钱。当你以某价格买入一个奴隶后，这个奴隶的身价即会自动提高；他的其他好友可以这个新的标价从你手中随时买走，中间的差价在扣除人口交易印花税、奴隶自己的收益后，大部分的获利归你所有。
如果你喜欢某位好友、或者“憎恨”某位好友，那就把他/她买为你的奴隶吧！奴隶被交易的次数越多，他的身价就越高。
在本游戏中，只有你加了对方的、并且对方也加了你为好友（双向好友）的人，才能被你买卖。<br/>
【如何买卖奴隶】<br/>
只要你有足够的现金，你可以将你的任一双向好友买入为你的奴隶。如果那位好友目前是别人的奴隶，你可以强行购入<br/>
【如何赚钱】<br/>
1.你买入的奴隶被别人购买后，中间的大部分差价即为你的盈利<br/>
2.让你的奴隶去打工，来赚取金钱<br/>
3.邀请朋友加入乐酷社区，每邀请1位奖励300元<br/>
【折磨奴隶】<br/>
1.购买奴隶时，可以给他起个你觉得爽的绰号<br/>
2.买入的奴隶，你可以整他一下！让他去给你打工赚钱、给你洗脚...<br/>
3.你如果惨无人道反复折磨你的奴隶，奴隶可能会跑掉<br/>
【自由、赎身】<br/>
1.赎身：花费一定的金钱从奴隶主的手里把自己买下来，赎身之后的1天内不能被买卖<br/>
2.自由：免费让你的奴隶获得自由，你将会损失一些金钱<br/>
<a href="myInfo.jsp">返回朋友买卖首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>