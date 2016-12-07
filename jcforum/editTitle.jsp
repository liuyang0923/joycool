<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String contentId=request.getParameter("contentId");
session.setAttribute("forumtitle","true");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="更改主题">
<p align="left">
<%=BaseAction.getTop(request, response)%>
主题:<input name="title"  maxlength="30" value=""/><br/>
 <anchor title="确定">更改主题
    <go href="viewContent.jsp?contentId=<%=contentId%>" method="post">
    <postfield name="title" value="$title"/>
    </go>
    </anchor><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>