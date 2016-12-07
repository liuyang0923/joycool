<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.action.wgame.WGameDataAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
LgjAction action = new LgjAction(request);
action.pkRightDeal1(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean lgj = (WGamePKBean) request.getAttribute("lgj");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(lgj != null){    
	if(lgj.getLeftUserId() == loginUser.getId()){
		toUserId = lgj.getRightUserId();
	} else {
		toUserId = lgj.getLeftUserId();
	}
	chatUrl = ("/chat/post.jsp?toUserId=" + toUserId );
	viewUrl = ("/user/ViewUserInfo.do?userId=" + toUserId );
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="应战">
<p align="center">应战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<%}else{%>
<%--
<%
if(lgj.getWinUserId() == lgj.getLeftUserId()) {  //输了
%>
<%=ResultPicture.getPicture(1)%>
<%
}else if(lgj.getWinUserId() == lgj.getRightUserId()) { //赢了 %>
<%=ResultPicture.getPicture(2)%>
<%}else {  //平局%> 
<%=ResultPicture.getPicture(0)%>
<%}%>
--%>
<%


//庄家的手式
	String a=lgj.getLeftCardsStr();
    // 战者的手式
	String b=lgj.getRightCardsStr();
%>
比赛结果是:<br/>
<%if(lgj.getWinUserId() == lgj.getLeftUserId() && lgj.getFlag()==1){%>*赌神的骰子*生效，使用次数减一,您不用付输掉的钱！<%}%>
庄家的手式是:<br/>
<img src="/wgame/lgj/img/<%=a%>.gif" alt="<%=action.getName(a)%>"/><br/>
您的手式是:<br/>
<img src="/wgame/lgj/img/<%=b%>.gif" alt="<%=action.getName(b)%>"/><br/>
<%
	//赢了
    if(lgj.getWinUserId() == lgj.getLeftUserId()){
		//加入谣言
//		WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/lgj/index.jsp") + "\" title=\"go\">剪刀石头布</a>]输给" + lgj.getLeftNickname() + "*" + lgj.getWager() + "个乐币");
		UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(lgj.getLeftUserId());
%>
您的手式小,您输给<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(lgj.getLeftNickname())%><%=lgj.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else if(lgj.getWinUserId() == lgj.getRightUserId()){
	    //加入谣言
	    UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(lgj.getLeftUserId());
//	    WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/lgj/index.jsp") + "\" title=\"go\">剪刀石头布</a>]赢了" + lgj.getLeftNickname() + "*" + lgj.getWager() + "个乐币");
%>
您的手式大,您赢了<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(lgj.getLeftNickname())%><%=lgj.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else {
		UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(lgj.getLeftUserId());
%>
您和庄家<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(lgj.getLeftNickname())%>打平了!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	}
%>
<a href="<%=chatUrl%>">跟她说话</a>|<a href="<%=viewUrl%>">查探底细</a><br/>
<a href="/wgamepk/lgj/pkStart.jsp?userId=<%=lgj.getLeftUserId()%>">邀请对方再PK一局</a><br/>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/><%
}
%>
<a href="/wgamepk/lgj/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>