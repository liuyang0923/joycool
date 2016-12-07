<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.user.SendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
SendAction action = new SendAction(request);
action.userMessageResult(request);
String result =(String)request.getAttribute("result");
String url=("/user/userMessageList.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="通知管理" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%--<%=BaseAction.getTop(request, response)%>--%>
<%=request.getAttribute("tip") %>(3秒后跳转通知列表)<br/>
<a href="/user/userMessageList.jsp">返回通知列表</a><br/>
<%=BaseAction.getBottom(request,response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
out.clearBuffer();
response.sendRedirect("userMessageList.jsp");
return;
}else{%>
<card title="通知管理" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%--<%=BaseAction.getTop(request, response)%>--%>
删除成功(3秒后跳转通知列表)<br/>
<a href="/user/userMessageList.jsp">返回通知列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>