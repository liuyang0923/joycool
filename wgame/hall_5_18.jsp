<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.WGameAction"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%
response.setHeader("Cache-Control","no-cache");
WGameAction action = new WGameAction();
action.hall(request);
//用户乐币数
UserStatusBean us = (UserStatusBean) request.getAttribute("us");
//在线人数
int onlineCount = ((Integer) request.getAttribute("onlineCount")).intValue();
//当前战况数
int rumorCount = ((Integer) request.getAttribute("rumorCount")).intValue();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="通吃岛赌场">
<p align="center">通吃岛赌场</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您有<%=us.getGamePoint()%>乐币<br/>
+美女脱衣陪玩区+<br/>
<a href="/wgame/football/index.jsp">射门</a>|<a href="/wgame/jsb/index.jsp">剪刀石头布</a><br/>
<a href="/wgame/dice/index.jsp">猜大小</a>|<a href="/wgame/g21/index.jsp">二十一点</a><br/>
<a href="/wgame/tiger/index.jsp">老虎机</a>|<a href="/wgame/showhand/index.jsp">乐酷梭哈</a><br/>
<a href="/wgame/3gong/index.jsp">三公</a>|<a href="/wgame/dicedx/index.jsp">骰子比大小</a><br/>
+真人激烈对战区+<br/>
在线玩家(<%=onlineCount%>人)<br/>
+<a href="/user/ViewFriends.do?backTo=http://wap.joycool.net/wgame/hall.jsp">邀请好友来玩</a>+<br/>
<!--
<a href="/wgame/rumorList.jsp">当前战况(<%=rumorCount%>条)</a><br/>
-->
**单回合游戏**<br/>
<a href="/wgamepk/football/index.jsp">射门</a>|<a href="/wgamepk/dice/index.jsp">骰子比大小</a><br/>
<a href="/wgamepk/3gong/index.jsp">三公</a>|<a href="/wgamepk/jsb/index.jsp">剪刀石头布</a><br/>
**多回合游戏**<br/>
<a href="/wgamehall/gobang/index.jsp">五子棋</a>|<a href="/wgamehall/othello/index.jsp">黑白棋</a><br/>
<a href="/wgamehall/football/index.jsp">点球决战</a><br/>
<!--
+个人相关+<br/>
<a href="/wgame/myStatus.jsp">我的战绩</a>|<a href="/wgame/myStatus.jsp">当前赌局</a><br/>
-->
<br/>
<%=BaseAction.getBottom(request, response)%><br/>
</p>

</card>
</wml>