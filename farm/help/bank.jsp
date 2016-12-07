<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
玩家进入桃花源将自动获得50铜板.<br/>
如果铜板不够用,可以到桃花钱庄将乐酷银行的乐币兑换成相应比例的铜板.一万乐币等价于1铜板.<br/>

返回<a href="../bank.jsp">桃花钱庄</a>功能<br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>