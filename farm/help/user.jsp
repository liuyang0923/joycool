<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
首页的个人资料中可以查看人物属性,包括姓名,职业,帮派和级别,经验值等等.<br/>
注意:人物等级和人物的战斗等级是分离的.人物等级再高,如果战斗等级不足,也无法打败野兽.<br/>

返回<a href="../user/info.jsp">个人资料</a>功能<br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>