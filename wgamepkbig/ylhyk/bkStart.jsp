<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.action.wgamepk.big.YlhykAction" %><%
response.setHeader("Cache-Control","no-cache");
YlhykAction action = new YlhykAction(request);
action.bkStart(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
session.setAttribute("ylhykbk", "");
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="坐庄">
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
金额必须是1亿以上的整数亿。主题文字可以嚣张但必须文明。<br/>
主题(15字以下)<br/>
<input type="text" name="content" maxlength="15" value="要乐还要酷" title="下注"/><br/>
金额(亿)<br/>
<input type="text" name="wager" format="*N" maxlength="4" value="10" title="下注"/><br/>
选择:<anchor title="确定">乐
    <go href="bkDeal1.jsp?rs=0" method="post">
    <postfield name="wager" value="$wager"/>
    <postfield name="content" value="$content"/>
    </go>
</anchor><br/>
选择:<anchor title="确定">酷
    <go href="bkDeal1.jsp?rs=1" method="post">
    <postfield name="wager" value="$wager"/>
    <postfield name="content" value="$content"/>
    </go>
</anchor><br/>
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