<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="搜索家园">
<p align="left">
搜索家园<br/>
用户ID：<input name="userId" format="*N"  maxlength="10" value=""/> <anchor title ="search Image">Go
         <go href="searchHome.jsp" method="post">
             <postfield name="userId" value="$userId"/>
         </go>
         </anchor><br/>
<br/>
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<a href="home.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>