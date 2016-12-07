<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="猜名人专用火机">
<p align="left">
猜名人专用火机，赢取zippo千元大奖<br/>
---------------------<br/>
Zippo"芝宝"火机驰名全球，是众多时尚名流、成功人士的挚爱之选。也是当前时尚、成功男性随身必备的"装备"。<br/>
乐酷现选出8位大家熟悉的名人，只要你从40余款zippo精品火机中选对最"称"他的那款，你就有机会获得此款火机！即使你没有猜对也有乐酷的奖励哦（<a href="/wxsj/zippo/rule.jsp">具体奖励和答题规则</a>）<br/>
活动周五晚上9点结束，周五晚上10点将公布8位名人实际使用的zippo，还有他们使用zippo的签名照片。<br/>
<br/>
补充说明：为保证游戏的公正性，最终获奖用户将公示3日，若有人投诉其作弊并获得支持将取消它的权益。<br/>
<a href="/wxsj/zippo/game/index.jsp"><%=StringUtil.toWml("马上开始游戏>>")%></a><br/>
=zippo（芝宝）常识=<br/>
了解这些可以增加你的中奖机率哦~<br/>
<a href="/wxsj/zippo/intro.jsp">*zippo（芝宝）简介</a><br/>
zippo是世界知名的经典打火机，其历史悠久，设计独特...<br/>
<a href="/wxsj/zippo/history.jsp">*zippo（芝宝）历史</a><br/>
zippo 是美国人乔治·布雷斯代在1932年发明的，迄今有60多年的历史了...<br/>
<a href="/wxsj/zippo/story.jsp">*zippo（芝宝）故事</a><br/>
60多年来，Zippo时刻都在发生着让人难以忘怀的动人故事...<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>