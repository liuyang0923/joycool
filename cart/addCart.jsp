<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的收藏夹">
<%=BaseAction.getTop(request, response)%>
<p align="left">
我的收藏夹<br/>
-------------------<br/>
加入成功！<br/>
进入<a href="http://wap.joycool.net/mycart.jsp">我的收藏夹</a><br/>
<br/>
<anchor title="back"><prev/>返回上一页</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>