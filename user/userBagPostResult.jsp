<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%
response.setHeader("Cache-Control","no-cache");
String roomId = (String) request.getParameter("roomId");
if(roomId==null){
	roomId="0";
}
//防止刷新
if(session.getAttribute("userBagPost")==null){
//response.sendRedirect(("/chat/hall.jsp?roomId="+roomId));
BaseAction.sendRedirect("/chat/hall.jsp?roomId="+roomId, response);
return;
}
UserBagAction action = new UserBagAction(request);
action.userBagPostResult(request);
session.removeAttribute("userBagPost");
String url=("/chat/hall.jsp?roomId="+roomId);
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="使用道具" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转聊天室.)<br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
String userId=(String)request.getAttribute("userId");
String content=(String)request.getAttribute("content");
%>
<card title="使用道具" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=content%>(3秒后跳转聊天室.)<br/>
<a href="/chat/post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=userId%>">返回发言页面</a><br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>