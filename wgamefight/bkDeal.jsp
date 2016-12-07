<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
FightAction action = new FightAction(request);
String result, tip;
action.bkDeal(request);
result = (String) request.getAttribute("result");
tip = (String) request.getAttribute("tip");
String url = "/wgamefight/index.jsp";
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
WGameFightBean fight = (WGameFightBean) request.getAttribute("fight");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="坐庄" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="center">坐庄</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<a href="/wgamefight/bkStart.jsp">返回下注</a><br/>
<%
}else if("success".equals(result)){
%>
坐庄成功!<br/>
您下注<%=fight.getWager()%>个乐币<br/>
请等其他用户挑战(3秒跳回首页)<br/>
<%
}
%>
<br/>
<a href="/wgamefight/index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛赌坊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>