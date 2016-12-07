<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
学习不同的专业可以得到不同的收获.<br/>
每个专业的学习需要专业点数,点击各个专业可以查看专业的说明,以及学习/升级该专业所需的点数.<br/>
学习了一个专业后才能学习这个专业下的<a href="mypro.jsp">技能</a>.<br/>
废弃一个专业后,该专业耗费的相应点数也会还原.<br/>

返回<a href="../user/pros.jsp">专业</a>功能<br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>