<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
当学习了一个<a href="pro.jsp">专业</a>后,如果这个专业有相应的技能,可以在某些NPC那里学习技能.<br/>
如果是制造技能,在材料足够的情况下,可以直接使用.如果是战斗技能,可以在战斗中选择使用.<br/>
返回<a href="../user/mypro.jsp">专业技能</a>功能<br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>