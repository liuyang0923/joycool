<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgame.JsbAction"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
session.setAttribute("playingJsb", "playing");
JsbAction action = new JsbAction();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus(request);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
GirlBean girl = null; 
WGameBean wins = (WGameBean) session.getAttribute("jsbWins");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="剪刀石头布">
<p align="left">
<%=BaseAction.getTop(request, response)%>
剪刀石头布<br/>
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
<anchor title="出剪刀">出剪刀
    <go href="deal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="j"/>
<%
if(wins == null) {
%>
	<postfield name="girlId" value="<%=girl.getId()%>"/>
<%
}
%>
    </go>
</anchor><br/>
<anchor title="扔石头">扔石头
    <go href="deal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="s"/>
<%
if(wins == null) {
%>
	<postfield name="girlId" value="<%=girl.getId()%>"/>
<%
}
%>
    </go>
</anchor><br/>
<anchor title="撒大布">撒大布
    <go href="deal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="b"/>
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