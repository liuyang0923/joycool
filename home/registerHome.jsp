<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.registerHome(request);
String result=(String) request.getAttribute("result");
String url = ("/home/home.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("failure".equals(result)){
url = ("/home/inputRegisterInfo.jsp");
%>
<card title="注册结果" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>,3秒后自动返回!<br/>
<a href="/home/inputRegisterInfo.jsp">返回注册家园资料</a><br/>
<a href="/user/userInfo.jsp">修改基本资料</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
%>
<card title="注册结果" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
成功开通！（３秒后跳转家园首页） <br/>
<a href="/user/userInfo.jsp">修改基本资料</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>