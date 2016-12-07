<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IJobService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
String name=(String)session.getAttribute("angername");
String angergender=(String)session.getAttribute("angergender");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="出气筒游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/img/job/anger/logo.gif" alt="出气筒"/><br/>

我是乐酷整人专家！受气了？别担心，我替你找回公道！<br/>
<%if(angergender!=null && name!=null){%>
<a href="/job/anger/ventAnger.jsp">继续拿<%=StringUtil.toWml(name)%>出气?</a><br/>
<%}%>
请输入出气对象的姓名：<br/>
<input name="name" value=""/><br/>
请选择出气对象性别：<br/>
<select name="gender" value="1">
    <option value="1">男</option>
    <option value="0">女</option>
	</select><br/>
请选择出气对象的和你的关系：<br/>
<select name="relation" value="1">
    <option value="1">父母</option>
    <option value="2">老师</option>
    <option value="3">恋人</option>
    <option value="4">上司</option>
    <option value="5">下属</option>
    <option value="6">朋友或兄弟姐妹</option>
    <option value="7">客户</option>
	</select><br/>
<anchor title="提交">提交
    <go href="/job/anger/ventAnger.jsp" method="post">
    <postfield name="gender" value="$gender"/>
    <postfield name="relation" value="$relation"/>
    <postfield name="name" value="$name"/>
      <postfield name="bleed" value="100"/>
    </go>
</anchor><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>