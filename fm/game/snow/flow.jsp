<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="打雪仗流程"><p align="left">
 流程,报名和奖励.<br/>
1.活动从当日4时起至活动结束前均可以进行报名,报名后自动进入审核名单.<br/>
2.有游戏管理权限的家族官员可对该审核名单进行管理.<br/>
3.管理员选择通过的成员可在游戏正式开始后进入游戏,每进入一人将从家族基金中扣除1000万报名费,若家族基金不足则无法进入.<br/>
4.若家族管理员在游戏开始前选择不参加本次活动或活动开始时没有人通过审核,则视为该家族未报名参加此次活动.<br/>
5.若家族报名参加此次活动,且在游戏结束时没有人进入游戏,则视为该家族本次活动弃权.<br/>
6.到比赛规定的结束时间时,双方仍没有结果.结束时按积雪多少计算,积雪少的胜出.若积雪量一样算平局.<br/>
7.奖励<br/>
胜利:胜利的一方,获得双方全部报名费和过程花费的95%.5%作为乐酷税收.失败的一方损失全部.<br/>
平局:游戏如果超出游戏结束时间还未分出胜负,则强行终止,和局.双方退还报名费,损失游戏中使用的钱.<br/>
一方没有参加比赛:若有一方在比赛结束后一直没有进场人员得,那么参赛的一方在赢得比赛后退还全部的报名费以及比赛中花费.<br/>
<a href="snowBallFight.jsp">返回打雪仗</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>