<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.action.wgame.WGameDataAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
BasketBallAction action = new BasketBallAction(request);
// 判断用户的输入action是否正确，只能是j s b中的一个
String ac = (String) request.getParameter("action");
if(ac==null||!ac.equals("l")&&!ac.equals("m")&&!ac.equals("r")){
	response.sendRedirect("index.jsp");
	return;
}
action.chlStart(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean basketball = (WGamePKBean) request.getAttribute("basketball");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(basketball != null){    
	if(basketball.getLeftUserId() == loginUser.getId()){
		toUserId = basketball.getRightUserId();
	} else {
		toUserId = basketball.getLeftUserId();
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
<a href="index.jsp">返回上级</a><br/>
<%
} else if("success".equals(result)){
	//庄家的手式
	String a=basketball.getLeftCardsStr();
    // 战者的手式
	String b=basketball.getRightCardsStr();
%>
<%--
<%
if(basketball.getWinUserId() == basketball.getLeftUserId()) {  //输了
%>
<%=ResultPicture.getPicture(1)%>
<%
}else if(basketball.getWinUserId() == basketball.getRightUserId()) { //赢了 %>
<%=ResultPicture.getPicture(2)%>
<%}else {  //平局%> 
<%=ResultPicture.getPicture(0)%>
<%}%>
--%>
根据篮球规则进攻方失败输掉两倍乐币<br/>
比赛结果是:<br/>
<%if(basketball.getWinUserId() == basketball.getLeftUserId() && basketball.getFlag()==1){%>*赌神的骰子*生效，使用次数减一,您不用付输掉的钱！<%}%>
对方的进攻方式是:<%=action.getAttackName(a)%><br/>
您的防守方式是:<%=action.getDefenseName(b)%><br/>
<%
	if(a.equals("l")&&b.equals("l")){%>
<img src="img/no.gif" alt="射门左vs守门左"/><br/>
   <% }else if(a.equals("l")&&b.equals("m")){%>
<img src="img/yes.gif" alt="射门左vs守门中"/><br/>
	<%}else if(a.equals("l")&&b.equals("r")){%>
<img src="img/yes.gif" alt="射门左vs守门右"/><br/>
	<%}else if(a.equals("m")&&b.equals("l")){%>
<img src="img/yes.gif" alt="射门中vs守门左"/><br/>
	<%}else if(a.equals("m")&&b.equals("m")){%>
<img src="img/no.gif" alt="射门中vs守门中"/><br/>
	<%}else if(a.equals("m")&&b.equals("r")){%>
<img src="img/yes.gif" alt="射门中vs守门右"/><br/>
	<%}else if(a.equals("r")&&b.equals("l")){%>
<img src="img/yes.gif" alt="射门右vs守门左"/><br/>
	<%}else if(a.equals("r")&&b.equals("m")){%>
<img src="img/yes.gif" alt="射门右vs守门中"/><br/>
	<%}else{%>
<img src="img/no.gif" alt="射门右vs守门右"/><br/>
	<%}//赢了
  if(basketball.getWinUserId() == basketball.getLeftUserId()){
	    //加入谣言
//		WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("index.jsp") + "\" title=\"go\">射门</a>]输给" + basketball.getLeftNickname() + "*" + basketball.getWager() + "个乐币");
		UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(basketball.getLeftUserId());
%>
防守失败,您输给<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(basketball.getLeftNickname())%><%=basketball.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else if(basketball.getWinUserId() == basketball.getRightUserId()){
	    //加入谣言
//	    WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("index.jsp") + "\" title=\"go\">射门</a>]赢了" + basketball.getLeftNickname() + "*" + basketball.getWager() + "个乐币");
	    UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(basketball.getLeftUserId());
%>
防守成功,您赢了<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(basketball.getLeftNickname())%><%=basketball.getWager()*2%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} 
}

if(chatUrl != null){
%><a href="<%=chatUrl%>">跟她说话</a>|<a href="<%=viewUrl%>">查探底细</a><br/>
<a href="pkStart.jsp?userId=<%=basketball.getLeftUserId()%>">邀请对方再PK一局</a><br/>
<%
}
%><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>