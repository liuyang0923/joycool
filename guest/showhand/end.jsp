<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.guest.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingShowHand2") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/guest/showhand/start.jsp", response);
	return;
}
session.removeAttribute("playingShowHand2");
jc.guest.wgame.ShowHandAction action = new jc.guest.wgame.ShowHandAction();
GuestHallAction guestAction = new GuestHallAction(request);
GuestUserInfo guestUser = guestAction.getGuestUser();
if (guestUser == null){
	response.sendRedirect("/guest/nick.jsp");
	return;
}
action.end(request,guestUser);
String result = (String) request.getAttribute("result");
//UserStatusBean us = action.getUserStatus(request);
jc.guest.wgame.ShowHandBean sh = (jc.guest.wgame.ShowHandBean) request.getAttribute("showhand");
int count, i;
CardBean card = null;
//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("showhandWins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
//马长青_2006-6-21_显示系统用户名_end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="梭哈">
<p align="left">
<%=BaseAction.getTop(request, response)%>
梭哈<br/>
-------------------<br/>
<%=girlName%>的牌:<br/>
<%
//显示系统牌
count = sh.getSystemCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) sh.getSystemCards().get(i);
%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
}
%>
<br/>
您的牌:<br/>
<%
//显示用户牌
count = sh.getUserCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) sh.getUserCards().get(i);
%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
}
%>
<br/>
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
您赢了<%=sh.getTotalWager()%>个游币<br/>
您还有<%=guestUser.getMoney()%>个游币<br/>
您已经赢了<%=wins.getWins()%>次!<br/>
<%
//输了
} else {
session.removeAttribute("showhandWins");
	//加入谣言
%>
呜呜呜,您输给<%=girl.getName()%>!<br/>
您输了<%=sh.getTotalWager()%>个游币<br/>
您还有<%=guestUser.getMoney()%>个游币<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续梭哈</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>