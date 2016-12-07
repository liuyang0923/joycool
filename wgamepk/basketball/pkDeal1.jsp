<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
BasketBallAction action = new BasketBallAction(request);
action.pkDeal1(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean basketball = (WGamePKBean) request.getAttribute("basketball");
UserStatusBean enemyUs = (UserStatusBean) request.getAttribute("enemyUs");
UserBean enemy = (UserBean) request.getAttribute("enemy");
int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(basketball != null){    
	if(basketball.getLeftUserId() == loginUser.getId()){
		toUserId = basketball.getRightUserId();
	} else {
		toUserId = basketball.getLeftUserId();
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
	String a=basketball.getLeftCardsStr();
%>
强行PK成功!<br/>
您下注<%=basketball.getWager()%>个乐币<br/>
您选择的进攻方式是:<br/>
<%if(a.equals("l")){%>
<img src="/wgamepk/basketball/img/l.gif" alt="<%=action.getAttackName(a)%>"/><br/>
<%}else if(a.equals("m")){%>
<img src="/wgamepk/basketball/img/m.gif" alt="<%=action.getAttackName(a)%>"/><br/>
<%}else {%>
<img src="/wgamepk/basketball/img/r.gif" alt="<%=action.getAttackName(a)%>"/><br/>
<%}%>
请等待<%if(enemy.getUs2()!=null){%><%=enemy.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(enemy.getNickName())%>应战<br/>
<%
}

if(chatUrl != null){
%>
<a href="<%=chatUrl%>">跟她说话</a>|<a href="<%=viewUrl%>">查探底细</a><br/>
<%
}
%>
<br/>
<a href="/wgamepk/basketball/index.jsp">返回上一级</a><br/>
<a href="/wgamepk/index.jsp">返回通吃岛</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>