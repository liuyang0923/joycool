<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.wgame.big.WGamePKBigBean" %><%@ page import="net.joycool.wap.action.wgamepk.big.YlhykAction" %><%
response.setHeader("Cache-Control","no-cache");
YlhykAction action = new YlhykAction(request);
action.cancelBk(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
WGamePKBigBean ylhyk = (WGamePKBigBean) request.getAttribute("ylhyk");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="取消坐庄">
<p align="center">取消坐庄</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//没有坐庄
if(ylhyk == null){
%>
这局已经结束!<br/>
<a href="bkStart.jsp">我要继续坐庄</a><br/>
<%
} else {
	long wager = ((Long) request.getAttribute("wager")).longValue();
%>
您坐庄的赌局已经被取消!交纳了场地费<%=wager%>个乐币!<br/>
<%
}
%>
<br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>