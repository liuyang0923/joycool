<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
FootBallBean footballss = (FootBallBean)session.getAttribute("football");
if(session.getAttribute("playingFootBall") == null || footballss == null || footballss.getWager() > us.getGamePoint()){
//	response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgame/football/start.jsp", response);
	return;
}
session.removeAttribute("playingFootBall");
FootBallAction action = new FootBallAction();
action.deal2(request);
//UserStatusBean us = action.getUserStatus(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
String systemAction = (String) request.getAttribute("systemAction");
FootBallBean football = (FootBallBean) request.getAttribute("football");
String a=football.getAction();
String b=systemAction;
//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("footballWins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
//马长青_2006-6-21_显示系统用户名_end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="射门">
<p align="left">
<%=BaseAction.getTop(request, response)%>
射门<br/>
-------------------<br/>
比赛结果是<br/>
您的射门方向是<%=action.getName(a)%><br/>
<%=girlName%>的守门方向是<%=action.getName(b)%><br/>
<%
	if(a.equals("l")&&b.equals("l")){%>
<img src="/wgamepk/football/img/l-l.gif" alt="射门左vs守门左"/><br/>
   <% }else if(a.equals("l")&&b.equals("m")){%>
<img src="/wgamepk/football/img/l-m.gif" alt="射门左vs守门中"/><br/>
	<%}else if(a.equals("l")&&b.equals("r")){%>
<img src="/wgamepk/football/img/l-r.gif" alt="射门左vs守门右"/><br/>
	<%}else if(a.equals("m")&&b.equals("l")){%>
<img src="/wgamepk/football/img/m-l.gif" alt="射门中vs守门左"/><br/>
	<%}else if(a.equals("m")&&b.equals("m")){%>
<img src="/wgamepk/football/img/m-m.gif" alt="射门中vs守门中"/><br/>
	<%}else if(a.equals("m")&&b.equals("r")){%>
<img src="/wgamepk/football/img/m-r.gif" alt="射门中vs守门右"/><br/>
	<%}else if(a.equals("r")&&b.equals("l")){%>
<img src="/wgamepk/football/img/r-l.gif" alt="射门右vs守门左"/><br/>
	<%}else if(a.equals("r")&&b.equals("m")){%>
<img src="/wgamepk/football/img/r-m.gif" alt="射门右vs守门中"/><br/>
	<%}else{%>
<img src="/wgamepk/football/img/r-r.gif" alt="射门右vs守门右"/><br/>
	<%}%>
<%
//赢了
if("win".equals(result)){
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/football/start.jsp") + "\" title=\"go\">射门</a>]赢了" + football.getWager() + "个乐币");
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您赢了<%=girl.getName()%>!<br/>
您赢了<%=football.getWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
您已经赢了<%=wins.getWins()%>次!<br/>
<%=loginUser.showImg(picList[picIndex])%>
<!--<img src="<%=picList[picIndex]%>" alt="<%=girl.getName()%>"/><br/>-->
<%
} 
//输了
else if("lose".equals(result)){
session.removeAttribute("footballWins");
	//加入谣言
//	WGameDataAction.addRumor("<a href=\"" + ("/wgame/index.jsp") + "\" title=\"go\">美女赌场</a>:<a href=\"" + ("/chat/post.jsp?to=" + loginUser.getUserName()) + "\" title=\"go\">" + StringUtil.toWml(loginUser.getNickName()) + "</a>[<a href=\"" + ("/wgame/football/start.jsp") + "\" title=\"go\">射门</a>]输了" + football.getWager() + "个乐币");
%>
呜呜呜,您输给<%=girl.getName()%>!<br/>
您输了<%=football.getWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续挑战</a><br/>
<%--mcq_2006-6-20_增加找真人PK链接_start--%>
<a href="/wgamepk/football/index.jsp">找真人PK</a><br/>
<%--mcq_2006-6-20_增加找真人PK链接_end--%>
<a href="index.jsp">返回上一级</a><br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>