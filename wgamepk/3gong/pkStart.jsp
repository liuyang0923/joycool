<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
Gong3Action action = new Gong3Action(request);
action.pkStart(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
UserStatusBean enemyUs = (UserStatusBean) request.getAttribute("enemyUs");
UserBean enemy = (UserBean) request.getAttribute("enemy");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="强行PK">
<p align="center">强行PK</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//不够100个乐币
if("notEnoughMoney".equals(result)){
%>
<%=tip%><br/>
<%
} else if("failure".equals(result)){
%>
<%=tip%><br/>
<%
} else if("hasPk".equals(result)){
%>
<%=tip%><br/>
<%
} else if("continue".equals(result)){
%>
下注(至少10乐币,您还有<%=us.getGamePoint()%>乐币,<%if(enemy.getUs2()!=null){%><%=enemy.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(enemy.getNickName())%>还有<%=enemyUs.getGamePoint()%>个乐币):<br/>
<input type="text" name="wager" format="*N" maxlength="10" value="100" title="下注"/><br/>
<anchor title="确定">确定
    <go href="/wgamepk/3gong/pkDeal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="userId" value="<%=enemy.getId()%>"/>
    </go>
</anchor><br/>
<%
}
%>
<br/>
<a href="/wgamepk/3gong/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>