<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.LuckyAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%@ page import="java.util.Vector"%><%
response.setHeader("Cache-Control","no-cache");
LuckyAction action = new LuckyAction(request);
UserBean loginUser = action.getLoginUser();
action.play();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="幸运转盘">
<p align="left">
<%=BaseAction.getTop(request, response)%>
幸运大转盘，好礼随心拿！<br/>
-------------<br/>
<%if(!UserInfoUtil.isUserInterval(0, loginUser.getId())){%>
<a href="playRes.jsp">免费转幸运转盘</a><br/>
<%}else{%>
本周您已经参加过一次幸运转盘，如要再转则需支付3亿乐币！<br/>
<a href="playRes.jsp?f=1">付费转幸运转盘</a><br/>
<%}%>
-------------<br/>
<%=action.log.toString()%>
-------------<br/>
1、乐酷的所有友友，每周有1次免费参加幸运大转盘抽奖的机会。 <br/>
2、免费机会使用完以后，活动也允许用乐币换取抽奖，每次抽奖花费3亿币。<br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>