<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.framework.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/knife/start.jsp"));
	return;
}

if(session.getAttribute("correctCount") == null){
	response.sendRedirect(("/wxsj/knife/start.jsp"));
	return;
}

int correctCount = StringUtil.toInt((String) session.getAttribute("correctCount"));

session.removeAttribute("correctCount");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="勇士爱军刀">
<p align="left">
<%
int gp = 0;
int p = 0;
//0
if(correctCount == 0){
	gp = 0;
	p = 5;
%>
您总计有<%=correctCount%>题回答正确，将额外获得<%=gp%>乐币和<%=p%>点经验值的奖励！<br/>
号称男人必备的军刀，竟然没听说过…了解了解吧：<a href="/wxsj/knife/knife.jsp?id=1">服务员</a><br/>
<%
}
//1-5
else if(correctCount >= 1 && correctCount <= 5){
	gp = 200;
	p = 10;
%>
您总计有<%=correctCount%>题回答正确，将额外获得<%=gp%>乐币和<%=p%>点经验值的奖励！<br/>
号称男人必备的军刀，看来您是有听说没了解，认识一下吧：<a href="/wxsj/knife/knife.jsp?id=1">服务员</a><br/>
<%
}
//6-10
else if(correctCount >= 6 && correctCount <= 10){
	gp = 400;
	p = 15;
%>
您总计有<%=correctCount%>题回答正确，将额外获得<%=gp%>乐币和<%=p%>点经验值的奖励！<br/>
称得上博学多闻，<a href="/wxsj/knife/knife.jsp?id=2">经济型</a>军刀最适合您<br/>
<%
}
//11-15
else if(correctCount >= 11 && correctCount <= 15){
	gp = 800;
	p = 25;
%>
您总计有<%=correctCount%>题回答正确，将额外获得<%=gp%>乐币和<%=p%>点经验值的奖励！<br/>
精明的同志，心里有数的"<a href="/wxsj/knife/knife.jsp?id=3">逍遥派</a>"是您的最佳写照<br/>
<%
}
//16-20
else if(correctCount >= 16 && correctCount <= 20){
	gp = 1200;
	p = 35;
%>
您总计有<%=correctCount%>题回答正确，将额外获得<%=gp%>乐币和<%=p%>点经验值的奖励！<br/>
英雄！关注瑞士军刀很久了吧，一定知道"<a href="/wxsj/knife/knife.jsp?id=4">露营者</a>"！<br/>
<%
}
//21-25
else if(correctCount >= 21 && correctCount <= 25){
	gp = 2000;
	p = 50;
%>
您总计有<%=correctCount%>题回答正确，将额外获得<%=gp%>乐币和<%=p%>点经验值的奖励！<br/>
一定是瑞士军刀的fans！快来看看这款"<a href="/wxsj/knife/knife.jsp?id=5">迷你小冠军</a>"吧<br/>
<%
}
//25-30
else if(correctCount >= 25 && correctCount <= 30){
	gp = 2500;
	p = 68;
%>
您总计有<%=correctCount%>题回答正确，将额外获得<%=gp%>乐币和<%=p%>点经验值的奖励！<br/>
瑞士军刀骨灰级粉丝出现了！只有你才配得上经典的"<a href="/wxsj/knife/knife.jsp?id=6">前驱</a>"！<br/>
<%
}

//加上乐币
int [] sts = new int[2];
sts[0] = JoycoolInfc.GAME_POINT;
sts[1] = JoycoolInfc.POINT;
int [] svs = new int[2];
svs[0] = gp;
svs[1] = p;
JoycoolInfc.updateUserStatus(loginUser.getId(), sts, svs, request);

UserStatusBean us = JoycoolInfc.getUserStatus(loginUser.getId(), request);
%>
<br/>
您现有乐币<%=us.getGamePoint()%>，经验<%=us.getPoint()%><br/>
<a href="/lswjs/wagerHall.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>