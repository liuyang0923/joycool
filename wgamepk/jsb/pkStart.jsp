<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
JsbAction action = new JsbAction(request);
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
//不够10个乐币
if("notEnoughMoney".equals(result)){
%>
<%=tip%><br/>
<%
}else if("hasPk".equals(result)){
%>
<%=tip%><br/> 
<%
}else if("failure".equals(result)){
%>
<%=tip%><br/>
<%
} else if("continue".equals(result)){
%>
下注(至少10个乐币,您还有<%=us.getGamePoint()%>个乐币,对手<%if(enemy.getUs2()!=null){%><%=enemy.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(enemy.getNickName())%>还有<%=enemyUs.getGamePoint()%>个乐币):<br/>
<input type="text" name="wager" format="*N" maxlength="10" value="100" title="下注"/><br/>
<anchor title="出剪刀">出剪刀
<go href="pkDeal1.jsp" method="post">
<postfield name="wager" value="$wager"/>
	<postfield name="action" value="j"/>
	<postfield name="userId" value="<%=enemy.getId()%>"/>
</go>
</anchor><br/>
<anchor title="扔石头">扔石头
<go href="pkDeal1.jsp" method="post">
<postfield name="wager" value="$wager"/>
	<postfield name="action" value="s"/>
	<postfield name="userId" value="<%=enemy.getId()%>"/>
</go>
</anchor><br/>
<anchor title="撒大布">撒大布
<go href="pkDeal1.jsp" method="post">
<postfield name="wager" value="$wager"/>
	<postfield name="action" value="b"/>
	<postfield name="userId" value="<%=enemy.getId()%>"/>
</go>
</anchor><br/>
<%
}
%>
<br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>