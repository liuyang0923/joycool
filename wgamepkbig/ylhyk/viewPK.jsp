<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.wgame.big.WGamePKBigBean" %><%@ page import="net.joycool.wap.action.wgamepk.big.YlhykAction" %><%
response.setHeader("Cache-Control","no-cache");
YlhykAction action = new YlhykAction(request);
action.viewWGamePK(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
WGamePKBigBean ylhyk = (WGamePKBigBean) request.getAttribute("pk");
int toUserId=0;
String chatUrl = null;
String viewUrl = null;
if(ylhyk != null){    
	if(ylhyk.getLeftUserId() == loginUser.getId()){
		toUserId = ylhyk.getRightUserId();
	} else {
		toUserId = ylhyk.getLeftUserId();
	}
	chatUrl = ("/chat/post.jsp?toUserId=" + toUserId );
	viewUrl = ("/user/ViewUserInfo.do?userId=" + toUserId );
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赌局">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//已经被取消
if(ylhyk == null||ylhyk.getLeftUserId()!=loginUser.getId()){
%>
这局已被取消!<br/>
<%
} 
//坐庄中
else if(ylhyk.getMark() == WGamePKBigBean.PK_MARK_BKING){
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(ylhyk.getLeftUserId());
%>
庄家:<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(ylhyk.getLeftNickname())%><br/>
状态:等待挑战<br/>
赌注:<%=action.bigNumberFormat(ylhyk.getWager())%>乐币<br/>
<%
}
//结束
else if(ylhyk.getMark() == WGamePKBigBean.PK_MARK_END){
int leftCard = StringUtil.toInt(ylhyk.getLeftCardsStr());
int rightCard = StringUtil.toInt(ylhyk.getRightCardsStr());
if(ylhyk.getFlag()==1){%>
	<%if(ylhyk.getWinUserId() == ylhyk.getRightUserId()){%>
		恭喜，你的大富豪色子起作用了，本次赌博算打平。<br/>
	<%}%>
	<%if(ylhyk.getWinUserId() == ylhyk.getLeftUserId()){%>
		很遗憾，对方的大富豪色子发挥了作用，双方打平。<br/>
	<%}%>
<%}%>
<%if(ylhyk.getLeftUserId()==ylhyk.getWinUserId()){%>
恭喜，您坐庄赢了！
<%}else{%>
很遗憾，您坐庄输了！
<%}%><br/>
赌注金额：<%=action.bigNumberFormat(ylhyk.getWager())%><br/>
挑战者选择：<%=action.getTitle()[rightCard]%><br/>
庄家选择：<%=action.getTitle()[leftCard]%><br/>
庄家：您自己<br/>
挑战者：<a href="<%=viewUrl%>"><%=StringUtil.toWml(ylhyk.getRightNickname())%></a><br/>
<%}%>
<br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>