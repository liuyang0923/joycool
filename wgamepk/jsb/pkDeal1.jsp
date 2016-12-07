<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
JsbAction action = new JsbAction(request);
// 判断用户的输入action是否正确，只能是j s b中的一个
String ac = (String) request.getParameter("action");
if(ac==null||!ac.equals("j")&&!ac.equals("s")&&!ac.equals("b")){
	response.sendRedirect("index.jsp");
	return;
}
action.pkDeal1(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean jsb = (WGamePKBean) request.getAttribute("jsb");
UserStatusBean enemyUs = (UserStatusBean) request.getAttribute("enemyUs");
UserBean enemy = (UserBean) request.getAttribute("enemy");
int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(jsb != null){    
	if(jsb.getLeftUserId() == loginUser.getId()){
		toUserId = jsb.getRightUserId();
	} else {
		toUserId = jsb.getLeftUserId();
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
	String name=jsb.getLeftCardsStr();
%>
强行PK成功!<br/>
您下注<%=jsb.getWager()%>个乐币<br/>
您的手式是:<br/>
<img src="/wgame/jsb/img/<%=name%>.gif" alt="<%=action.getName(name)%>"/><br/>
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
<a href="/wgamepk/jsb/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>