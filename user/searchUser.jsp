<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="搜索注册用户">
<p align="left">
<%=BaseAction.getTop(request, response)%>
搜索注册用户<br/>
-----------<br/>
<input name="nickname"  maxlength="20" value=""/><br/>
<anchor title="确定">搜索昵称
<go href="searchUserResult.jsp" method="post">
    <postfield name="nickname" value="$nickname"/>
</go>
</anchor><br/>
<br/>
<input name="id"  maxlength="10" format="*N" value=""/><br/>
<anchor title="确定">搜索ID
<go href="searchUserResult.jsp" method="post">
    <postfield name="id" value="$id"/>
</go>
</anchor><br/>
<br/>
<a href="userMoneyLog.jsp">查询我的送币记录</a><br/>
<a href="ViewFriends.do">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>