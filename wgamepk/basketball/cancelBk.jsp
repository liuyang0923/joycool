<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
BasketBallAction action = new BasketBallAction(request);
action.cancelBk(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
WGamePKBean basketball = (WGamePKBean) request.getAttribute("basketball");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="取消坐庄">
<p align="center">取消坐庄</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//没有坐庄
if(basketball == null){
%>
这局已经结束!<br/>
<a href="/wgamepk/basketball/bkStart.jsp">我要继续坐庄</a><br/>
<%
} else {
	int wager = ((Integer) request.getAttribute("wager")).intValue();
%>
您坐庄的赌局已经被取消!交纳了场地费<%=wager%>个乐币!您当前还有<%=us.getGamePoint()%>个乐币<br/>
<%
}
%>
<br/>
<a href="/wgamepk/basketball/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>