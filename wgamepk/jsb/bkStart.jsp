<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%
response.setHeader("Cache-Control","no-cache");
JsbAction action = new JsbAction(request);
action.bkStart(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="坐庄">
<p align="center">坐庄</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//不够10个乐币
if("notEnoughMoney".equals(result)){
%>
<%=tip%><br/>
<%
} else if("hasBk".equals(result)){
%>
<%=tip%><br/>
<a href="index.jsp">我坐庄的赌局</a><br/>
<%
} else if("continue".equals(result)){
%>
提醒：坐庄后将无法取消，请慎重决定。如果无人挑庄，您的庄将在72小时后自动撤掉.<br/>
下注(至少100个乐币,您还有<%=us.getGamePoint()%>个乐币):<br/>
<input type="text" name="wager" format="*N" maxlength="10" value="100" title="下注"/><br/>
<anchor title="出剪刀">出剪刀
<go href="bkDeal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="j"/>
</go>
</anchor><br/>
<anchor title="扔石头">扔石头
<go href="bkDeal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="s"/>
</go>
</anchor><br/>
<anchor title="撒大布">撒大布
<go href="bkDeal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="b"/>
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