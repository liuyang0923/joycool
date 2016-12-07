<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgame.BasketBallAction"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
session.setAttribute("playingBasketBall", "playing");
BasketBallAction basketball = new BasketBallAction();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = basketball.getUserStatus(request);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
GirlBean girl = null; 
WGameBean wins = (WGameBean) session.getAttribute("basketballWins");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="篮球飞人">
<p align="left">
<%=BaseAction.getTop(request, response)%>
篮球飞人<br/>
<%
//已经选择美女
if(wins != null){
	girl = (GirlBean) Girls.getGirl(wins.getGirlId());
%>
对手:<%=girl.getName()%><br/>
您已经赢了<%=wins.getWins()%>次了!<br/>
<%
} else {
if(wins == null){
	Vector girls = Girls.getGirls();
    int r = RandomUtil.nextInt(8) + 1;
    switch (r) {
    case 1:
	  girl=(GirlBean)girls.get(0);
	  break;
    case 2:
	  girl=(GirlBean)girls.get(1);
	  break;
    case 3:
	  girl=(GirlBean)girls.get(2);
	  break;
	case 4:
	  girl=(GirlBean)girls.get(3);
	  break;
	case 5:
	  girl=(GirlBean)girls.get(4);
	  break;
	case 6:
	  girl=(GirlBean)girls.get(5);
	  break;
	case 7:
	  girl=(GirlBean)girls.get(6);
	  break;
    case 8:
	  girl=(GirlBean)girls.get(7);
	  break;
	case 9:
	  girl=(GirlBean)girls.get(8);
	  break;
    }
%>

<%=girl.getName()%>陪您玩<br/>
<%
}
}
%>
-------------------<br/>
下注(您现在有<%=us.getGamePoint()%>个乐币):<br/>
<input type="text" name="wager" format="*N" maxlength="10" value="100" title="下注"/><br/>

请选择进攻方式:<br/>
<img src="/wgamepk/basketball/img/shoumen.gif" alt="球门"/><br/>

<anchor title="左">突破上篮
    <go href="deal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="l"/>
<%
if(wins == null) {
%>
	<postfield name="girlId" value="<%=girl.getId()%>"/>
<%
}
%>
</go>
</anchor>
<anchor title="中"><br/>三分远投
    <go href="deal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="m"/>
<%
if(wins == null) {
%>
	<postfield name="girlId" value="<%=girl.getId()%>"/>
<%
}
%>
</go>
</anchor>
<anchor title="右"><br/>飞身扣篮
    <go href="deal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="r"/>
<%
if(wins == null) {
%>
	<postfield name="girlId" value="<%=girl.getId()%>"/>
<%
}
%>
    </go>
</anchor><br/>
<br/>

<a href="index.jsp">返回上一级</a><br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>