<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
	response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="有偿求助"><p><%=BaseAction.getTop(request, response)%>
规则与声明：<br/><%
%>1、女性用户点击“我要求助”后可进入编写求助贴页面，此功能暂且只针对女性用户开放，男用户可在求助帖中进行回复和给发帖者进行信誉投票。<br/><%
%>2、帖子标题最多输入20字、内容最多输入1000字、奖励最多输入50字、回复最多输入100字。<br/><%
%>3、每个女性用户每天只能发一个求助帖，有未结束帖时不能发新帖。<br/><%
%>4、每帖采用3个回复后自动结束，若想采用1或2个回复后结束帖子，需在帖子下方点击[结束此帖]，若想不采用回复而结束帖子，则直接选择帖子内容下方的[无满意答案,结束此帖]即可。<br/><%
%>5、每帖中都有“鲜花”与“臭鸡蛋”功能，如果对楼主支持，可以点击“鲜花”，如果被骗过就点击“臭鸡蛋”吧。每个用户在一个帖子里只能投一个：鸡蛋或者鲜花，且投后不能更改。<br/><%
%>6、如果发现有用户发表不符合规则或不适宜发在此处的帖子，管理员有权删除，请用户自觉遵守各项制度。<br/><%
%>7、乐酷只为各位网友提供求助平台，对于网友们在求助中是否兑现奖励的事情均不负责。<br/><%
%>8、最终解释权归乐酷所有。<br/><%
%>[<a href="hpindex.jsp">返回</a>]<br/><%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>