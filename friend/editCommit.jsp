<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
action.editCommit(request);
String result=(String) request.getAttribute("result");
String url = ("/friend/friendCenter.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("failure".equals(result)){
url = ("/friend/editFriend.jsp");
%>
<card title="修改结果" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>,3秒后自动返回!<br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

<%
} else if("success".equals(result)){
%>
<card title="修改结果" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
修改成功!3秒后自动跳转<br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>