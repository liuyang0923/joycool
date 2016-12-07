<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgame.WGameDataAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
JsbAction action = new JsbAction(request);
// 判断用户的输入action是否正确，只能是j s b中的一个
String ac = (String) request.getParameter("action");
if(ac==null||!ac.equals("j")&&!ac.equals("s")&&!ac.equals("b")){
	response.sendRedirect("index.jsp");
	return;
}
action.bkDeal1(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean jsb = (WGamePKBean) request.getAttribute("jsb");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="坐庄">
<p align="center">坐庄</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<a href="/wgamepk/jsb/bkStart.jsp">返回下注</a><br/>
<%
} else if("success".equals(result)){
    //加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/jsb/index.jsp") + "\" title=\"go\">剪刀石头布</a>]坐庄" + jsb.getWager() + "个乐币");
	String name=jsb.getLeftCardsStr();
%>
坐庄成功!<br/>
您下注<%=jsb.getWager()%>个乐币<br/>
您的手式是:<br/>
<img src="/wgame/jsb/img/<%=name%>.gif" alt="<%=action.getName(name)%>"/><br/>
请等待其他用户挑战<br/>
<%
}
%>
<br/>
<a href="/wgamepk/jsb/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>