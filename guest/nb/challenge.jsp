<%@page contentType="text/vnd.wap.wml;charset=utf-8"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%response.setHeader("Cache-Control", "no-cache");%>
<%@page import="jc.guest.*,net.joycool.wap.bean.*,jc.guest.battle.*,java.util.LinkedList,net.joycool.wap.util.*,java.util.ArrayList"%><%@page import="net.joycool.wap.framework.BaseAction"%>
<%GamepageAction action=new GamepageAction(request);
GuestHallAction action2 = new GuestHallAction(request,response);
GuestUserInfo guestUser = action2.getGuestUser();
if(StringUtil.toInt(request.getParameter("name"))<0 || GamepageAction.topbean==null){
	response.sendRedirect("game.jsp");
	return;
}
int winlose=StringUtil.toInt(request.getParameter("winlose"));
int h=StringUtil.toInt(request.getParameter("name"));
Topbean[] topbean=null;
String name="";
if(winlose==0){
	 topbean=GamepageAction.topbean;
		if(h>=topbean.length){
			if(h!=0)
			h=topbean.length-1;
			}
	 name=topbean[h].getWinname().get(0).toString();
}else{
	topbean=GamepageAction.topbeanlose;
	if(h>=topbean.length){
		if(h!=0)
		h=topbean.length-1;
		}
	 name=topbean[h].getLosename().get(0).toString();
}
%>
<wml>
<card title="名字大作战"><p>
<%=BaseAction.getTop(request,response)%>
请输入名字和TA进行战斗!(最多可输入10个字)<br/>
<%//取错误信息
if(session.getAttribute("stored")!=null){
	Stored stored=(Stored)session.getAttribute("stored");
if(!stored.isError()){%>
<%=stored.getErrormessage()%><br/>
<%}}session.removeAttribute("stored");%>
<input type="text" name="username1" format="M,A,a,N" />
vs<%=StringUtil.toWml(name)%><br/>
<anchor title="确定">确定
<go href="battle.jsp?challenge=1" method="post"><!-- challenge=1时，出现错误，返回challenge.jsp -->
<postfield name="username1" value="$username1" />
<postfield name="username2" value="<%=StringUtil.toWml(name)%>"/>
<postfield name="name" value="<%=h%>"/><!-- 返回当前页面时传递被挑战的名字 -->
<postfield name="winlose" value="<%=winlose%>"/><!-- 判断失败还是胜利的数组 -->
<postfield name="first" value="0" />
</go>
</anchor><%=guestUser != null ? "需要花费1游币." : "" %><br/>
<a href="top.jsp?winlose=<%=winlose%>">返回昨日挑战榜</a><br/>
<a href="game.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p>
</card>
</wml>