<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.action.wgame.WGameDataAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
JsbAction action = new JsbAction(request);
// 判断用户的输入action是否正确，只能是j s b中的一个
String ac = (String) request.getParameter("action");
if(ac==null||!ac.equals("j")&&!ac.equals("s")&&!ac.equals("b")){
	response.sendRedirect("index.jsp");
	return;
}
action.pkRightDeal1(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean jsb = (WGamePKBean) request.getAttribute("jsb");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(jsb != null){    
	if(jsb.getLeftUserId() == loginUser.getId()){
		toUserId = jsb.getRightUserId();
	} else {
		toUserId = jsb.getLeftUserId();
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
if(jsb.getWinUserId() == jsb.getLeftUserId()) {  //输了
%>
<%=ResultPicture.getPicture(1)%>
<%
}else if(jsb.getWinUserId() == jsb.getRightUserId()) { //赢了 %>
<%=ResultPicture.getPicture(2)%>
<%}else {  //平局%> 
<%=ResultPicture.getPicture(0)%>
<%}%>
--%>
<%


//庄家的手式
	String a=jsb.getLeftCardsStr();
    // 战者的手式
	String b=jsb.getRightCardsStr();
%>
比赛结果是:<br/>
<%if(jsb.getWinUserId() == jsb.getLeftUserId() && jsb.getFlag()==1){%>*赌神的骰子*生效，使用次数减一,您不用付输掉的钱！<%}%>
庄家的手式是:<br/>
<img src="/wgame/jsb/img/<%=a%>.gif" alt="<%=action.getName(a)%>"/><br/>
您的手式是:<br/>
<img src="/wgame/jsb/img/<%=b%>.gif" alt="<%=action.getName(b)%>"/><br/>
<%
	//赢了
    if(jsb.getWinUserId() == jsb.getLeftUserId()){
		//加入谣言
//		WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/jsb/index.jsp") + "\" title=\"go\">剪刀石头布</a>]输给" + jsb.getLeftNickname() + "*" + jsb.getWager() + "个乐币");
		UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(jsb.getLeftUserId());
%>
您的手式小,您输给<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(jsb.getLeftNickname())%><%=jsb.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else if(jsb.getWinUserId() == jsb.getRightUserId()){
	    //加入谣言
	    UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(jsb.getLeftUserId());
//	    WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgamepk/jsb/index.jsp") + "\" title=\"go\">剪刀石头布</a>]赢了" + jsb.getLeftNickname() + "*" + jsb.getWager() + "个乐币");
%>
您的手式大,您赢了<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(jsb.getLeftNickname())%><%=jsb.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else {
		UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(jsb.getLeftUserId());
%>
您和庄家<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(jsb.getLeftNickname())%>打平了!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	}
%>
<a href="<%=chatUrl%>">跟她说话</a>|<a href="<%=viewUrl%>">查探底细</a><br/>
<a href="/wgamepk/jsb/pkStart.jsp?userId=<%=jsb.getLeftUserId()%>">邀请对方再PK一局</a><br/>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/><%
}
%>
<a href="/wgamepk/jsb/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>