<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
个人属性:<br/>
侠客秘境中每个用户拥有一个单独的游戏角色，拥有自己的各种属性，进入"我的状态"就可以查看。<br/>
体力:前一个数字是你的当前体力值，后一个是体力上限。体力为零时角色宣告死亡。恢复体力可以通过吃药，增加体力上限可以通过升级、内功修炼和装备加成。角色死亡之后可以花乐币马上复活，也可以等待系统自动复活（免费，５分钟）<br/>
气力: 前一个数字是你的当前体力值，后一个是体力上限。每次攻击都需要一定的气力，气力不足时无法使用攻击技能。恢复气力可以通过吃药和休息一会，增加气力上限可以通过升级、内功修炼和装备加成。<br/>
战斗经验值:表示角色的战斗经验的数值，角色每战斗一次就会有所增加，增加到一定程度就会使角色升级，带来属性的全面增长。<br/>
攻击力:表示角色攻击力的数值，根据角色等级、装备和内外功综合计算，越高越好。<br/>
防御力: 表示角色防御力的数值，根据角色等级、装备和内外功综合计算，越高越好。<br/>
轻功:综合影响角色防御和命中的数值，越高越好。<br/>
吉运:决定角色发挥最高攻击力和防御力的数值，越高角色在战斗中越容易发挥最高攻击力和防御力。<br/>
<br/>
<a href="/pk/index.jsp">直接开始游戏</a><br/>
<a href="/pk/readme.jsp">返回详细游戏指南</a><br/>
<a href="/pk/help.jsp">返回侠客秘境首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>