<%@page contentType="text/vnd.wap.wml;charset=utf-8"%>
<%response.setHeader("Cache-Control", "no-cache");%>
<%@page import="net.joycool.wap.bean.*,jc.guest.battle.*,java.util.LinkedList,net.joycool.wap.util.*,java.util.*"%><%@page import="net.joycool.wap.framework.BaseAction"%>
<%GamepageAction action=new GamepageAction(request);
if(request.getParameter("winlose")==null){
	response.sendRedirect("game.jsp");
	return;
}
action.top();
int winlose=0;//用来选择排行榜 0是胜利  1是 失败
%>
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="昨日挑战榜"><p>
<%=BaseAction.getTop(request,response)%>
<%Topbean[] topbean=null;
if(StringUtil.toInt(request.getParameter("winlose"))==0){
	 topbean=GamepageAction.topbean;
%>
战神榜<%=StringUtil.toWml("<>") %><a href="top.jsp?winlose=1">炮灰榜</a><br/>
名字|胜利次数<br/>
<%}else if(StringUtil.toInt(request.getParameter("winlose"))==1){
	 topbean=GamepageAction.topbeanlose;	
	 winlose=1;
%>
<a href="top.jsp?winlose=0">战神榜</a><%=StringUtil.toWml("<>") %>炮灰榜<br/>
名字|失败次数<br/>
<%}%>
<%
if(topbean!=null){
for(int i=0;i<topbean.length;i++){
	String topname="";
	int h=i;
	if(winlose==1){
	 topname=topbean[i].getLosename().get(0).toString();
	 
	}else if(winlose==0){
	 topname=topbean[i].getWinname().get(0).toString();
	}
	if(i>19)
		break;
	String name=StringUtil.toWml(topname);
%>
<%=i+1%>.<%=name%>|<%=topbean[i].getTimes()%><a href="challenge.jsp?name=<%=i%>&amp;winlose=<%=winlose%>">挑战</a><br/>
<%
}}%>
<a href="game.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p>
</card>
</wml>