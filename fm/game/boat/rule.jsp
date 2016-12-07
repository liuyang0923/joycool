<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游戏规则"><p align="left"><%=BaseAction.getTop(request, response)%><%
%>1、游戏开放日的规定比赛时间是正式游戏时间。<br/><%
%>2、赛龙舟最多11人参赛，多余人员可以在聊天室旁观助威，无法划船。<br/><%
%>3、比赛开始前，游戏管理员及家族族长可进入龙舟商店购买龙舟（依龙舟类型消耗购买人的乐币或者酷币），比赛开始后无法购买，重复购买，后买的覆盖之前购买的龙舟。<br/><%
%>4、比赛开始，家族龙舟的起始速度为0米/分钟。龙舟有最大速度。达到此速度后，按加速不会再提升速度。（依龙舟不同，最大速度不同）。<br/><%
%>5、参赛人员按上加速，左右调整龙舟航行角度。（单次点击依龙舟不同，效果不同）<br/><%
%>6、方向控制：龙舟左侧人员：上、右；龙舟右侧人员：上、左；掌舵人：左、右。<br/><%
%>7、赛道长为8000米，走完全程则比赛结束。到达比赛结束时间仍未完成比赛的家族按里程数计算排名。<br/><%
%>8、赛龙舟过程中会随机遇到系统事件发生。<br/><%
%>9、胜利奖励：第一名，获得全部报名费的50%。第二名获得30%。第三名获得15%。<br/><%
%>10、若游戏途中服务器事故导致游戏不能正常进行，则退回报名费用。<br/><%
%><a href="tpboat.jsp">龙舟类型</a><br/><a href="accident.jsp">龙舟随机事件</a><br/><a href="match.jsp">返回赛龙舟</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>