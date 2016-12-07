<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.wgame.big.WGamePKBigBean" %><%@ page import="net.joycool.wap.action.wgamepk.big.YlhykAction" %><%
response.setHeader("Cache-Control","no-cache");

YlhykAction action = new YlhykAction(request);
action.chlStart(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBigBean ylhyk = (WGamePKBigBean) request.getAttribute("ylhyk");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(ylhyk != null){    
	if(ylhyk.getLeftUserId() == loginUser.getId()){
		toUserId = ylhyk.getRightUserId();
	} else {
		toUserId = ylhyk.getLeftUserId();
	}
	viewUrl = ("/user/ViewUserInfo.do?userId=" + toUserId );
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="挑战">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<%
} else if("success".equals(result)){
%>
<%
int leftCard = StringUtil.toInt(ylhyk.getLeftCardsStr());
int rightCard = StringUtil.toInt(ylhyk.getRightCardsStr());
if(ylhyk.getFlag()==1){
	if(ylhyk.getWinUserId() == ylhyk.getLeftUserId()){%>
		恭喜，你的大富豪色子起作用了，本次赌博算打平。<br/>
	<%}
	if(ylhyk.getWinUserId() == ylhyk.getRightUserId()){%>
		很遗憾，对方的大富豪色子发挥了作用，双方打平。<br/>
	<%}
}%>
<%if(ylhyk.getLeftUserId()==ylhyk.getWinUserId()){%>
很遗憾，您挑战失败！
<%}else{%>
恭喜，您挑战成功！
<%}%><br/>
赌注金额：<%=action.bigNumberFormat(ylhyk.getWager())%><br/>
挑战者选择：<%=action.getTitle()[rightCard]%><br/>
庄家选择：<%=action.getTitle()[leftCard]%><br/>
庄家：<a href="<%=viewUrl%>"><%=StringUtil.toWml(ylhyk.getLeftNickname())%></a><br/>
挑战者：您自己<br/>
<%}%><br/>
<a href="/wgamepkbig/ylhyk/index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛</a><br/>
<%--=BaseAction.getAdver(21,response)--%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>