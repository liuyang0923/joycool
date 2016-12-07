<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
String c="";
if(request.getParameter("c")!=null){c="&amp;c="+request.getParameter("c");}
int mid=snowAction.getParameterInt("mid");
request.setAttribute("mid",Integer.valueOf(mid));
int isGameOver=snowAction.isGameOver(mid);// 比赛结束跳到别的页面
if(isGameOver==2||isGameOver==0){
	out.clearBuffer();
	response.sendRedirect("fight.jsp?mid="+mid);return;
}else if(isGameOver==3||isGameOver==4){
	out.clearBuffer();
	response.sendRedirect("/fm/game/fmgame.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="兑换雪币"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(request.getParameter("c")!=null&&request.getParameter("cmd")==null){
int money = snowAction.getParameterInt("change");
if(money == 0 || money < 0 || money > 10000){
%>
输入格式不正确,请输入兑换数量的数字!<br/>
<a href="snowMoney.jsp?mid=<%=mid %><%=c %>">兑换雪币</a><br/>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%}else{%>
确定花费1酷币的手续费,用<%=100000*money %>乐币兑换<%=money %>雪币吗?<br/>
<a href="reSnowMoney.jsp?mid=<%=mid %>&amp;cmd=y&amp;change=<%=money %><%=c %>">确定</a><br/>
<a href="fight.jsp?mid=<%=mid %>">返回游戏</a><br/>
<%}}else if(request.getParameter("c")==null ||(request.getParameter("c")!=null&&request.getParameter("cmd")!=null)){
if(request.getParameter("c")==null&&snowAction.isInGame()){
out.clearBuffer();
response.sendRedirect("/fm/game/snow/snowMoney.jsp?mid="+mid+"&c=2");return;	
}
int change=snowAction.getChange();
if(change==5){ %>
恭喜你成功兑换了<%=snowAction.getParameterInt("change") %>个雪币!<br/>
<a href="fight.jsp?mid=<%=request.getParameter("mid") %>">进入游戏</a><br/>
<%}else{if(change==4||change==3){%>
您不是家族成员,不能进行操作!<br/>
<%}else if(change==2){ %>
输入格式不正确,请输入兑换数量的数字!<br/>
<%}else if(change==6){ %>
只准在当前赛事中兑换,且必须在比赛中!<br/>
<%}else if(change==1){ %>
您输入的雪币金额超过了您可以兑换的范围,请重新兑换!<br/>
<%}else if(change==0){%>
您的乐币不够!您可以减少兑换量!<br/>
<%}else if(change==10){ %>
您的酷币不够!<br/>
<%}else if(change==11){ %>
您未参加该赛事,不能进行兑换!<br/>
<%}else if(change==12){ %>
只能在雪仗游戏中,进行雪币兑换!<br/>
<%} %>
<a href="snowMoney.jsp?mid=<%=mid %><%=c %>">兑换雪币</a><br/>
<%} %>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%} %>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>