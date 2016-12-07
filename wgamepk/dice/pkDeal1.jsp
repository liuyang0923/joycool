<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%
response.setHeader("Cache-Control","no-cache");
DiceAction action = new DiceAction(request);
action.pkDeal1(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean dice = (WGamePKBean) request.getAttribute("dice");
UserStatusBean enemyUs = (UserStatusBean) request.getAttribute("enemyUs");
UserBean enemy = (UserBean) request.getAttribute("enemy");
int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(dice != null){    
	if(dice.getLeftUserId() == loginUser.getId()){
		toUserId = dice.getRightUserId();
	} else {
		toUserId = dice.getLeftUserId();
	}
	chatUrl = ("/chat/post.jsp?toUserId=" + toUserId );
	viewUrl = ("/user/ViewUserInfo.do?userId=" + toUserId );
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="强行PK">
<p align="center">强行PK</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<anchor><prev/>返回下注</anchor><br/>
<%
} else if("success".equals(result)){
	int[] dices = dice.getLeftDices();
%>
强行PK成功!<br/>
您下注<%=dice.getWager()%>个乐币!<br/>
您的骰子是:<br/>
<img src="img/<%=dices[0]%>.gif" alt="<%=dices[0]%>"/><img src="img/<%=dices[1]%>.gif" alt="<%=dices[1]%>"/><img src="img/<%=dices[2]%>.gif" alt="<%=dices[2]%>"/><br/>
请等待<%if(enemy.getUs2()!=null){%><%=enemy.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(enemy.getNickName())%>应战.<br/>
<%
}

if(chatUrl != null){
%>
<a href="<%=chatUrl%>">跟她说话</a>|<a href="<%=viewUrl%>">查探底细</a><br/>
<%
}
%>
<br/>
<a href="/wgamepk/dice/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>