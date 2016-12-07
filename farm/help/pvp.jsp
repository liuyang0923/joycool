<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
pvp是指玩家与玩家的战斗,在桃花源中,只有晚间8点和9点这两个小时允许玩家之间的战斗(限部分区域).<br/>
在擂台区域,如果玩家被杀死,不会有死亡惩罚.在其他区域如果被玩家杀死,将如同被怪物杀死一样,损失部分经验和装备损坏.<br/>
在擂台区域内杀死玩家将获得荣誉.活动期间,每周的荣誉会有排行,获得名次的玩家会得到一定的奖励.<br/>
在非擂台区域杀死的玩家数量也会累计,杀玩家数最多的会被以恶人榜的形式公布在各大城市,以提醒其他玩家小心.<br/>
<a href="../map.jsp">返回场景</a><br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>