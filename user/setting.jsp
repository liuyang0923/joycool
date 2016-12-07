<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");

UserAction action = new UserAction(request);
action.setting();

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="免骚扰功能">
<p align="left">
<%=BaseAction.getTop(request, response)%>
设置成功!<br/>
<a href="userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>