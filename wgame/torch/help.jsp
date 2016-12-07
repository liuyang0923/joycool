<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="奥运火炬手">
<p align="left">
<%=BaseAction.getTop(request, response)%>
=奥运火炬活动规则=<br/>
每天不定时发放五色火炬(周一除外),拿到火炬的玩家可以将火炬随意传递给自己在线的好友.凡是传递到的玩家都将获得（火炬手）称号,该称号显示在用户信息页.得到的不同火炬数量越多,火炬传递的玩家越多,火炬指数越高.<br/>
活动每周一举行进行发奖.活动结束后,抽取火炬指数最高的火炬手100名,并从中随机选择20名发奖.<br/>
一等奖1名奖品论坛卡一张和永久性乐酷奥运星一个<br/>
二等奖4名奖品金色荣誉卡一个和乐酷火炬手标志一个<br/>
三等奖15名奖品欢乐渔场卡和普通乐酷奥运卡各一张<br/>
*火炬指数越高的火炬手,将有越高的机会获得越高的奖励!<br/>
*火炬经过越多的玩家,指数越高(相同玩家传递多次不算).火炬指数增加的同时之前该火炬传递到过的所有玩家的火炬指数也会增加.<br/>
*每周一发奖后将清除火炬指数重新计算<br/>
<br/><a href="index.jsp">返回奥运火炬首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>