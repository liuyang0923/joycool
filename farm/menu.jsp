<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean user = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="user/bag.jsp">我的行囊</a>|
<a href="user/quests.jsp">我的任务(<%=user.getQuests().size()%>)</a><br/>
<a href="user/mypro.jsp">技能</a>|
<a href="user/equips.jsp">装备</a>|
<a href="user/info.jsp">属性</a>|
<a href="user/pros.jsp">专业分配</a>
<br/><br/>
<a href="map.jsp">场景</a>|
<a href="fields.jsp">农场</a>|
<a href="feeds.jsp">畜牧</a>|
<a href="lands.jsp">采集场</a><br/>
<a href="bank.jsp">桃花钱庄</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>