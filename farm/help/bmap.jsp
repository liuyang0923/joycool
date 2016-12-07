<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
桃源村因村东的桃花溪而得名,风景秀丽的恬静小村,人民勤劳淳朴而友善.<br/>
<a href="/farm/img/map/0.gif"><img src="../img/map/0.gif" alt="map"/><br/>下载</a>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>