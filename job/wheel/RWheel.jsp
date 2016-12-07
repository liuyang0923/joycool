<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="--------俄罗斯轮盘游戏---------">
<%=BaseAction.getTop(request, response)%>
游戏规则:<br/>
轮盘中有大小不等十个格子，放置0-9十个数字。轮盘转动停止后，大格子被指中的几率大，但奖金少，小格子正相反。如果没有指中所选数字，将输掉所下注的金额。<br/>
<a href="/job/wheel/StartWheel.jsp" >开始游戏</a><br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp" title="返回上一级">返回导航中心</a>
<br/>
<%=BaseAction.getBottom(request, response)%>

</card>

</wml>