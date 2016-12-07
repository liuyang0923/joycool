<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,jc.family.game.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int sure=snowAction.sureJoinGame();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="打雪仗统计"><p align="left">
<% if(sure==0){ %>
没有雪仗赛事!<br/>
<a href="snowBallFight.jsp">返回打雪仗</a><br/>
<%}else if(sure==2){ %>
您不是该家族用户!<br/>
<%}else if(sure==3){ %>
 比赛尚无结果!<br/>
<%}else{if(sure==4){ if(request.getAttribute("StaticsBean")!=null){SnowBean bean=(SnowBean)request.getAttribute("StaticsBean");%>
<%=bean.getHoldTime() %><br/>
<%} %>
您的家族没有参加该次家族打雪仗比赛.<br/>
对阵双方<br/>
<%
int mid=snowAction.getAttributeInt("mid");
List list=snowAction.getFightList("statistics.jsp",false,mid);
if(list!=null){
if(list.size()>0){
for(int i=0;i<list.size();i++){
SnowBean bean=(SnowBean)list.get(i);
%>
<a href="/fm/myfamily.jsp?id=<%=bean.getFid1() %>"><%if(bean.getRank()==1){ %> 【赢】<%}else if(bean.getRank()==3){%>【平】<%} %>  <%=bean.getFmName1() %>家族</a>|<a  href="../../myfamily.jsp?id=<%=bean.getFid2() %>"><%=bean.getFmName2() %><%if(bean.getFid2()!=0){%>家族<%}%></a><br/>
 <%} }}%><%if(request.getAttribute("pageFight")!=null&&!"".equals(request.getAttribute("pageFight"))){%>
<%=request.getAttribute("pageFight") %>
<%}%> 
<a href="snowBallFight.jsp">返回打雪仗</a><br/>
<%}else if(sure==5){ if(request.getAttribute("StaticsBean")!=null){
	SnowBean bean=(SnowBean)request.getAttribute("StaticsBean");
	String win="输";
	long time=bean.getSpendTime();
	String s=GameAction.getFormatDifferTime(time);
if(bean.getRank()==1){win="赢";}
if(bean.getRank()==3||bean.getRank()==4){win="平";}
%>
本轮统计<%=bean.getHoldTime() %><br/>
本家族:<%=win %><br/>
参与人数:<%=bean.getNumTotal() %><br/>
用时:<%=s %><br/>
&gt;&gt;家族获得:<br/>
排名积分:<%=bean.getScore() %> <br/>
奖金:<%=bean.getPrize() %> <br/>
游戏经验值:<%=bean.getGamePoint() %> <br/>
&gt;&gt;个人获得:<br/>
个人功勋:<%=bean.getContribution() %> <br/>
<%}%> 
对阵双方<br/>
<%
int mid=snowAction.getAttributeInt("mid");
List list=snowAction.getFightList("statistics.jsp",false,mid);
if(list!=null){
if(list.size()>0){
for(int i=0;i<list.size();i++){
SnowBean bean=(SnowBean)list.get(i);
%>
<a href="/fm/myfamily.jsp?id=<%=bean.getFid1() %>"><%if(bean.getRank()==1){ %> 【赢】<%}else if(bean.getRank()==3){%>【平】<%} %>  <%=bean.getFmName1() %>家族</a>|<a  href="../../myfamily.jsp?id=<%=bean.getFid2() %>"><%=bean.getFmName2() %>家族</a><br/>
 <%} }}%><%if(request.getAttribute("pageFight")!=null&&!"".equals(request.getAttribute("pageFight"))){%>
<%=request.getAttribute("pageFight") %>
<%}%> 
<a href="snowBallFight.jsp">返回打雪仗</a><br/>
<%}%>
<%}%>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>