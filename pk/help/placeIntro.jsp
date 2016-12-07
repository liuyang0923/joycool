<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
地点介绍:<br/>
地点相当于游戏地图，是敌人出没、大家战斗的地方，越往后敌人越强。<br/>
游戏里开始的地方叫入口，这里有商业区和任务堂，买卖游戏物品、接受和提交游戏任务，这里的开始冒险里可以选择去任何地点。<br/>
在后面的战斗地点中可以在前进后退中选择进入下一地点或返回上一地点，也可以选择回到入口。<br/>
<br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<a href="/pk/readme.jsp">返回详细游戏指南</a><br/>
<a href="/pk/help.jsp">返回侠客秘境首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>