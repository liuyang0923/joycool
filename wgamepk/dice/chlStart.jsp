<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.wgame.WGameDataAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
DiceAction action = new DiceAction(request);
action.chlStart(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean dice = (WGamePKBean) request.getAttribute("dice");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(dice != null){    
	if(dice.getLeftUserId() == loginUser.getId()){
		toUserId = dice.getRightUserId();
	} else {
		toUserId = dice.getLeftUserId();
	}
	chatUrl = ("/chat/post.jsp?toUserId=" + toUserId );
	viewUrl = ("/user/ViewUserInfo.do?userId=" + toUserId );
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="挑战">
<p align="center">挑战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<%
} else if("success".equals(result)){
	int[] leftDices = dice.getLeftDices();
	int[] rightDices = dice.getRightDices();
%>
<%--
<%
if(dice.getWinUserId() == dice.getLeftUserId()) {  //输了
%>
<%=ResultPicture.getPicture(1)%>
<%
}else if(dice.getWinUserId() == dice.getRightUserId()) { //赢了 %>
<%=ResultPicture.getPicture(2)%>
<%}else {  //平局%> 
<%=ResultPicture.getPicture(0)%>
<%}%>
--%>
比赛结果是:<br/>
<%if(dice.getWinUserId() == dice.getLeftUserId() && dice.getFlag()==1){%>*赌神的骰子*生效，使用次数减一,您不用付输掉的钱！<%}%>
庄家的骰子是:<br/>
<img src="img/<%=leftDices[0]%>.gif" alt="<%=leftDices[0]%>"/><img src="img/<%=leftDices[1]%>.gif" alt="<%=leftDices[1]%>"/><img src="img/<%=leftDices[2]%>.gif" alt="<%=leftDices[2]%>"/><br/>
您的骰子是:<br/>
<img src="img/<%=rightDices[0]%>.gif" alt="<%=rightDices[0]%>"/><img src="img/<%=rightDices[1]%>.gif" alt="<%=rightDices[1]%>"/><img src="img/<%=rightDices[2]%>.gif" alt="<%=rightDices[2]%>"/><br/>
<%
	//赢了
    if(dice.getWinUserId() == dice.getLeftUserId()){
		 //加入谣言
//	    WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/dice/index.jsp") + "\" title=\"go\">掷骰子</a>]输给" + dice.getLeftNickname() + "*" + dice.getWager() + "个乐币");
	    UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(dice.getLeftUserId());
%>
您的骰子点数小,您输给<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(dice.getLeftNickname())%><%=dice.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else if(dice.getWinUserId() == dice.getRightUserId()){
	     //加入谣言
//	    WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/dice/index.jsp") + "\" title=\"go\">掷骰子</a>]赢了" + dice.getLeftNickname() + "*" + dice.getWager() + "个乐币");
	    UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(dice.getLeftUserId());
%>
您的骰子点数大,您赢了<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(dice.getLeftNickname())%><%=dice.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else {
		 UserStatusBean usTemp=null;
		    usTemp=UserInfoUtil.getUserStatus(dice.getLeftUserId());
%>
您和庄家<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(dice.getLeftNickname())%>打平了!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	}
}

if(chatUrl != null){
%>
<a href="<%=chatUrl%>">跟她说话</a>|<a href="<%=viewUrl%>">查探底细</a><br/>
<a href="/wgamepk/dice/pkStart.jsp?userId=<%=dice.getLeftUserId()%>">邀请对方再PK一局</a><br/>
<br/>
<%
}
%>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamepk/dice/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>