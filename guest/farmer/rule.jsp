<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="jc.guest.*,jc.guest.farmer.*,net.joycool.wap.framework.*"%><%
FarmAction action=new FarmAction(request,response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="完美农夫"><p align="left"><%=BaseAction.getTop(request, response)%>
游戏目标：在地图中尽可能多的种植麦田。<br/>
移动：玩家可以在6x6地图中上下左右移动，每移动至一格，该格即变为麦田，玩家不可再次移动至现有麦田。<br/>
锤子：地图中会随机分布9块石头，玩家每次移动到石头格，都需要消耗1把锤子来砸碎石头，才能将其转化为麦田。每关会有3把锤子，用光后将不能再移动至石头格。<br/>
游戏结束：当玩家无法继续移动时，该关游戏结束。根据玩家所种麦田数量进行评分及相应奖励。<br/>
<a href="index.jsp">返回完美农夫首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>