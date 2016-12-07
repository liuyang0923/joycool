<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongChatPostResult(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
String url=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
url=("/tong/tongList.jsp");%>
<card title="城邦列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转城邦列表)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}if(result.equals("contentError")){
TongBean tong =(TongBean)request.getAttribute("tong");
url=("/tong/tongChat.jsp?tongId="+tong.getId());
%>
<card title="帮会聊天室" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转帮会聊天室)<br/>
<a href="/tong/tongChat.jsp?tongId=<%=tong.getId()%>">返回帮会聊天室</a><br/>   
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>  
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong =(TongBean)request.getAttribute("tong");
url=("/tong/tongChat.jsp?tongId="+tong.getId());
response.sendRedirect(url);
if(true)
return;
%>
<card title="帮会聊天室" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
发言成功(3秒后跳转帮会聊天室)<br/>
<a href="/tong/tongChat.jsp?tongId=<%=tong.getId()%>">返回帮会聊天室</a><br/>   
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>    
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>