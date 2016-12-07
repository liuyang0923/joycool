<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.wgame.TigerAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
jc.guest.GuestHallAction guestAction = new jc.guest.GuestHallAction(request);
jc.guest.GuestUserInfo guestUser = guestAction.getGuestUser();
if (guestUser == null){
	response.sendRedirect("/guest/nick.jsp");
	return;
}
TigerBean tigerss = (TigerBean)session.getAttribute("tiger");
if(session.getAttribute("playingTiger2") == null || tigerss == null || tigerss.getWager() > guestUser.getMoney()){
	BaseAction.sendRedirect("/guest/tiger/start.jsp", response);
	return;
}
session.removeAttribute("playingTiger2");
TigerAction action = new TigerAction();
action.deal2(request,guestUser);
//UserStatusBean us = action.getUserStatus(request);
String result = (String) request.getAttribute("result");
TigerBean tiger = (TigerBean) request.getAttribute("tiger");
int[] results = tiger.getResults();
//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("tigerWins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
//马长青_2006-6-21_显示系统用户名_end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="老虎机">
<p align="left">
<%=BaseAction.getTop(request, response)%>
老虎机<br/>
-------------------<br/>
<img src="/wgame/tiger/img/<%=results[0]%>.gif" alt="<%=results[0]%>"/><img src="/wgame/tiger/img/<%=results[1]%>.gif" alt="<%=results[1]%>"/><img src="/wgame/tiger/img/<%=results[2]%>.gif" alt="<%=results[2]%>"/><br/>
<%
//赢了
if("win".equals(result)){
	//加入谣言
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您赢了<%=girl.getName()%>!<br/>
赔率是1:<%=tiger.getResult()%><br/>
您赢了<%=(tiger.getWager() * tiger.getResult())%>个游币<br/>
您还有<%=guestUser.getMoney()%>个游币<br/>
您已经赢了<%=wins.getWins()%>次!<br/>
<%
//输了
} else {
session.removeAttribute("tigerWins");
	//加入谣言
%>
呜呜呜,您输给<%=girl.getName()%>!<br/>
您输了<%=tiger.getWager()%>个游币<br/>
您还有<%=guestUser.getMoney()%>个游币<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续玩</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>