<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,jc.family.game.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int sure=snowAction.sureJoinGame();
int mid=snowAction.getParameterInt("mid");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="打雪仗"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(sure==2){ %>
您不是该家族用户!<br/>
<%}else if(sure==3){ %>
 比赛尚无结果!<br/>
<%}else{if(sure==4){ %>
您的家族没有参加该次家族打雪仗比赛.<br/>
<%}else if(sure==5){ if(request.getAttribute("StaticsBean")!=null){
	SnowBean bean=(SnowBean)request.getAttribute("StaticsBean");
	String win="输";
	long time=bean.getSpendTime();
	String s=GameAction.getFormatDifferTime(time);
if(bean.getRank()==1){win="赢";}
if(bean.getRank()==3||bean.getRank()==4){win="平";}
%>
<%=bean.getHoldTime() %><br/>
本家族:<%=win %><br/>
参与人数:<%=bean.getNumTotal() %><br/>
用时:<%=s %><br/>
&gt;&gt;家族获得:<br/>
排名积分:<%=bean.getScore() %> <br/>
奖金:<%=bean.getPrize() %> <br/>
游戏经验值:<%=bean.getGamePoint() %> <br/>
&gt;&gt;个人获得:<br/>
个人功勋:<%=bean.getContribution() %> <br/>
对阵双方<br/>
<a href="/fm/myfamily.jsp?id=<%=bean.getFid1() %>"><%=bean.getFmName1() %>家族</a>VS<a  href="../../myfamily.jsp?id=<%=bean.getFid2() %>"><%=bean.getFmName2() %>家族</a><br/>
<%}%>
<a href="fightLists.jsp?cmd=m&amp;mid=<%=mid %>">做雪球列表</a><br/>
<a href="fightLists.jsp?cmd=c&amp;mid=<%=mid %>">扫雪列表</a><br/>
<a href="fightLists.jsp?cmd=f&amp;mid=<%=mid %>">攻击列表</a><br/>
<a href="/fm/game/historygame.jsp">返回家族活动记录</a><br/>
<%}%>
<%} %>

<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>