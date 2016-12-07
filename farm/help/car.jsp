<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
有些npc提供驿站远行,不过在帮他们完成了一些任务之前,他们是不会乐意的:)<br/>
远行线路是固定的,不同等级和区域的,距离不同,价格也不同.远行的方式可能是船,也可能是马车.速度大约是普通移动的四倍,而且中途不会被途经的怪物和玩家击落.<br/>
远行途中,可以选择中途下车/船.除非你非常熟悉线路和地形,否则不建议这么做,很容易迷失方向.<br/>
初级的远行路线为:潮州城-龙王镇,灵川码头-海冕岛码头,无埃城-荒城.<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>