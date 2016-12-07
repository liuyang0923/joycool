<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgame.WGameDataAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.wgame.big.WGamePKBigBean" %><%@ page import="net.joycool.wap.action.wgamepk.big.YlhykAction" %><%
response.setHeader("Cache-Control","no-cache");
YlhykAction action = new YlhykAction(request);
boolean redeal = (session.getAttribute("ylhykbk") == null);	// 连庄了
if(!redeal) session.removeAttribute("ylhykbk");
String result, tip;
if(StringUtil.toInt(request.getParameter("wager")) > 200){
result="failure";
tip="近期整改，最多只能下注200亿乐币。";
//tip="为降低风险，现在起最多只能下注100亿乐币，稍后将做进一步调整。";
} else if(redeal){
result="failure";
tip="为了避免网络延迟导致的连庄,请重新坐庄";
} else {
action.bkDeal1(request);
result = (String) request.getAttribute("result");
tip = (String) request.getAttribute("tip");
}
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());

WGamePKBigBean ylhyk = (WGamePKBigBean) request.getAttribute("ylhyk");
//UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
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
<%
}else if("success".equals(result)){
%>
坐庄成功!<br/>
您下注<%=action.bigNumberFormat(ylhyk.getWager())%>个乐币<br/>
请等其他用户挑战<br/>
<%
}
%>
<br/>
<a href="bkStart.jsp">继续坐庄</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>