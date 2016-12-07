<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request, response);
if(action.getDelay() <= 0) {	// 需要等待，未冷却
action.innerRedirect("go.jsp");
return; }
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
等待<%=action.getDelay()%>毫秒后才能操作<br/>
<a href="go.jsp?a=1">GO</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>