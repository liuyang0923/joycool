<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.ResultPicture"%><%@ page import="net.joycool.wap.action.wgame.WGameDataAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FootBallAction action = new FootBallAction(request);
// 判断用户的输入action是否正确，只能是j s b中的一个
String ac = (String) request.getParameter("action");
if(ac==null||!ac.equals("l")&&!ac.equals("m")&&!ac.equals("r")){
	response.sendRedirect("index.jsp");
	return;
}
action.chlStart(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGamePKBean football = (WGamePKBean) request.getAttribute("football");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
int toUserId = 0;
String chatUrl = null;
String viewUrl = null;
//跟对方发言
if(football != null){    
	if(football.getLeftUserId() == loginUser.getId()){
		toUserId = football.getRightUserId();
	} else {
		toUserId = football.getLeftUserId();
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
	String a=football.getLeftCardsStr();
    // 战者的手式
	String b=football.getRightCardsStr();
%>
<%--
<%
if(football.getWinUserId() == football.getLeftUserId()) {  //输了
%>
<%=ResultPicture.getPicture(1)%>
<%
}else if(football.getWinUserId() == football.getRightUserId()) { //赢了 %>
<%=ResultPicture.getPicture(2)%>
<%}else {  //平局%> 
<%=ResultPicture.getPicture(0)%>
<%}%>
--%>
根据射门规则射门方失败输掉两倍乐币<br/>
比赛结果是:<br/>
<%--<%if(football.getWinUserId() == football.getLeftUserId() && football.getFlag()==1){%>*赌神的骰子*生效，使用次数减一,您不用付输掉的钱！<%}%>--%>
对方的射门方向是:<%=action.getName(a)%><br/>
您的守门方向是:<%=action.getName(b)%><br/>
<%
	if(a.equals("l")&&b.equals("l")){%>
<img src="img/l-l.gif" alt="射门左vs守门左"/><br/>
   <% }else if(a.equals("l")&&b.equals("m")){%>
<img src="img/l-m.gif" alt="射门左vs守门中"/><br/>
	<%}else if(a.equals("l")&&b.equals("r")){%>
<img src="img/l-r.gif" alt="射门左vs守门右"/><br/>
	<%}else if(a.equals("m")&&b.equals("l")){%>
<img src="img/m-l.gif" alt="射门中vs守门左"/><br/>
	<%}else if(a.equals("m")&&b.equals("m")){%>
<img src="img/m-m.gif" alt="射门中vs守门中"/><br/>
	<%}else if(a.equals("m")&&b.equals("r")){%>
<img src="img/m-r.gif" alt="射门中vs守门右"/><br/>
	<%}else if(a.equals("r")&&b.equals("l")){%>
<img src="img/r-l.gif" alt="射门右vs守门左"/><br/>
	<%}else if(a.equals("r")&&b.equals("m")){%>
<img src="img/r-m.gif" alt="射门右vs守门中"/><br/>
	<%}else{%>
<img src="img/r-r.gif" alt="射门右vs守门右"/><br/>
	<%}//赢了
  if(football.getWinUserId() == football.getLeftUserId()){
	    //加入谣言
//		WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("index.jsp") + "\" title=\"go\">射门</a>]输给" + football.getLeftNickname() + "*" + football.getWager() + "个乐币");
		UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(football.getLeftUserId());
%>
扑救失败,您输给<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(football.getLeftNickname())%><%=football.getWager()%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} else if(football.getWinUserId() == football.getRightUserId()){
	    //加入谣言
//	    WGameDataAction.addRumor("<a href=\"" + ("/wgame/hall.jsp") + "\" title=\"go\">赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("index.jsp") + "\" title=\"go\">射门</a>]赢了" + football.getLeftNickname() + "*" + football.getWager() + "个乐币");
	    UserStatusBean usTemp=null;
	    usTemp=UserInfoUtil.getUserStatus(football.getLeftUserId());
%>
扑救成功,您赢了<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(football.getLeftNickname())%><%=football.getWager()*2%>个乐币!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
	} 
}

if(chatUrl != null){
%><a href="<%=chatUrl%>">跟她说话</a>|<a href="<%=viewUrl%>">查探底细</a><br/>
<a href="pkStart.jsp?userId=<%=football.getLeftUserId()%>">邀请对方再PK一局</a><br/>
<%}%>
<br/>
<a href="index.jsp">返回上一级</a><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>