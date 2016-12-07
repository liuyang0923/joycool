<%@ page language="java" import="net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="报名流程"><p align="left"><%=BaseAction.getTop(request, response)%><%
%>1.家族总人数至少为5人，才可进行报名。<br/><%
%>2.活动从当日4时起至活动结束前均可以进行报名，报名后自动进入审核名单。<br/><%
%>3.有游戏管理权限的家族官员可对该审核名单进行管理。游戏开始后，已进入的用户将从审核名单中去除。<br/><%
%>4.管理员选择通过的成员可在游戏正式开始后进入游戏，每进入一人将从家族基金中扣除1000万报名费，若家族基金不足则无法进入。<br/><%
%>5.若家族管理员在游戏开始前选择不参加本次活动或活动开始时没有人通过审核，则视为该家族未报名参加此次活动。<br/><%
%><a href="match.jsp">返回赛龙舟</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>