<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.ShowHandAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingShowHand") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgame/showhand/start.jsp", response);
	return;
}
session.removeAttribute("playingShowHand");
ShowHandAction action = new ShowHandAction();
action.quit(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus(request);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
ShowHandBean sh = (ShowHandBean) request.getAttribute("showhand");
int count, i;
CardBean card = null;
//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("showhandWins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
session.removeAttribute("showhandWins");
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
	if(!card.isOpen()){
%><img src="../cardImg/back.gif" alt="x"/><%
    }else{%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
	}
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
呜呜呜,您输给<%=girlName%>!<br/>
您输了<%=sh.getTotalWager()%>个乐币<br/>
您还有<%=us.getGamePoint()%>个乐币!<br/>
<br/>
<a href="start.jsp">继续梭哈</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>