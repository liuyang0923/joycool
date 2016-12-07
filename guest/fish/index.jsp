<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.fish.FishAction,net.joycool.wap.action.job.fish.AreaBean" %><%
response.setHeader("Cache-Control","no-cache");
FishAction action = new FishAction(request);
action.index();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="钓鱼">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/job/fish/img/logo.gif" alt=""/><br/>
欢迎进入乐酷欢乐渔场。大家正玩得高兴呢，你也一起吧^_^（<a href="help.jsp">欢乐渔场游戏说明</a>）<br/>
小提示：12种鱼儿上钩方式对应12种拉竿方式，欢乐渔场公告会传达各地鱼讯，要好好观察总结哦！<br/>
<a href="areaList.jsp">选个好地方>></a><br/>
<a href="areaList.jsp">开始钓鱼</a><br/>
<%if(action.getLoginUser()!=null){%><%@include file="info.jsp"%><%}%>
<a href="<%=( "/guest/index.jsp")%>">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>