<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.game.*"%><%@ page import="jc.family.*"%><%SnowGameAction snowAction=new SnowGameAction(request,response);
int mid=snowAction.getParameterInt("mid");
request.setAttribute("mid",Integer.valueOf(mid));
int snow=snowAction.getEnd(); 
SnowBean gameBean=(SnowBean)request.getAttribute("GameEnds");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="打雪仗"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(gameBean==null){%>
该赛事不存在!<br/>
<%}else{int rank=gameBean.getRank(); %>
<%if(rank==1){ %>
<img alt="win" src="/fm/game/img/sl.gif" /><br/>
游戏结束!恭喜<%=gameBean.getFmName1() %>家族获胜!获得<%=gameBean.getPrize() %>乐币奖励以及<%=jc.family.game.snow.Constants.SNOW_WIN_SCORE %>分雪仗排行榜积分!<br/>
<%}else if(rank==2){%>
<img alt="lose" src="/fm/game/img/shib.gif" /><br/>
游戏结束!恭喜<%=gameBean.getFmName2() %>家族获胜!获得<%=snowAction.getAttributeLong("vsFmPrize") %>乐币奖励以及<%=jc.family.game.snow.Constants.SNOW_WIN_SCORE %>分雪仗排行榜积分!<br/>
<%}else if(rank==3||rank==4){if(gameBean.getFid2()!=0){ %>
<img alt="deuce" src="/fm/game/img/pj.gif" /><br/>
游戏结束!双方战平!家族取回报名费用<%=gameBean.getNumTotal()*10000000 %>乐币.<br/>
<%}else {%>
游戏结束,家族配对失败,没有多余的家族和贵家族配对!您的家族在本轮活动中轮空!获得所有报名费和<%=jc.family.game.snow.Constants.SNOW_DEUCE_SCORE %>分雪仗排行榜积分!<br/>
<%}} %>
<%if(gameBean.getGamePoint() != 0){ %>
您的家族获得<%=gameBean.getGamePoint()%>游戏经验值!<br/>
<%} %>
<%} %>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>