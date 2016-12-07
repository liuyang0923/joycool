<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
//action
HomeAction home=new HomeAction(request); 
home.searchHome(request);
//得到搜索到的用户及其家园信息
UserBean user=(UserBean)request.getAttribute("user");
HomeUserBean homeUser=(HomeUserBean)request.getAttribute("homeUser");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="用户家园搜索">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
<%if(homeUser == null){%>
    没有搜索到您要的家园!<br/>
    <%}else{%>
搜到家园：<br/>
----------<br/>  
<a href="/home/home.jsp?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())+"的家园"%></a><br/>
    <%}%>
    <br/>
<a href="home.jsp" title="返回">返回我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>