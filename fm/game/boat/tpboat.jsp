<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
BoatAction action=new BoatAction(request,response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="龙舟类型"><p align="left"><%=BaseAction.getTop(request, response)%><%
%>＝传统龙舟＝<br/><%
%>赛龙舟,吃粽子,念屈原.传统龙舟,传承千年的文化,历久而弥新,继续在乐酷家族赛龙舟中绽放光芒!!<br/><%
%>＝爆烈龙舟＝<br/><%
%>宽大的桨叶提供更强动力,爆烈系列基本龙舟,让你感受到别样激情.<br/><%
%>＝尖锋龙舟＝<br/><%
%>最小化空气阻力为设计重点,尖端的空气动力学研究成果的应用.这就是你想要的!<br/><%
%>＝凤凰龙舟＝<br/><%
%>九霄云外的凤凰龙舟,顷刻间俯冲而下,降临人间,洞察一切的艰难险阻.驾上这只"凤凰",你可以拨开迷雾看见晴空;拥有这只"凤凰",乘风破浪就在今朝.<br/><%
%><a href="rule.jsp">游戏规则</a><br/><a href="accident.jsp">龙舟随机事件</a><br/><a href="match.jsp">返回赛龙舟</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>