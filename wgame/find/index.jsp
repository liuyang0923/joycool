<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="一起找乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
一起找乐酷<br/>
-------------------<br/>
突破移动陷阱的趣味游戏！<br/>
中国移动即将推出“WAP首页推送”，会对访问免费WAP带来一定麻烦，今后如何正确访问乐酷社区呢？游戏将告诉你正确的方法，还有丰厚乐币和经验拿哦！<br/>
-------------------<br/>
游戏说明<br/>
进入游戏后，识破各种陷阱，选择正确的方法找到乐酷，如果选择正确，将会有经验和乐币的奖励。<br/>
--------------<br/>
<a href="play.jsp">开始游戏</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>