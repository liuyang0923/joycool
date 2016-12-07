<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
String exist=(String)request.getAttribute("exist");
String myself=(String)request.getAttribute("myself");
String max=(String)request.getAttribute("max");
String goTo = (String) request.getAttribute("goTo");
String returnUrl = (String)session.getAttribute("pagebeforeclick"); 
if(returnUrl!=null){
	goTo = (returnUrl.replace("&", "&amp;"));
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(exist!=null){%>您已经添加过该好友<br/><%}else if(myself!=null){%>不能加自己为好友!<br/><%}else if(max!=null){%>好友系统正在修改,好友数量暂时加以限制,你的好友数量已经超过300的上限!系统修改完毕后会解除限制.<br/><%}else{%>
我的好友<br/>
--------<br/>
操作成功！<br/>
<br/>
<%}
String toUserId = (String) request.getAttribute("friendId");
%>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%
if(returnUrl!=null){
%>
<a href="/chat/post.jsp?toUserId=<%=toUserId%>">返回</a><br/>
<%
}
%>
<a href="/user/ViewUserInfo.do?userId=<%=toUserId%>">返回用户信息</a><br/>
<a href="/user/ViewFriends.do">返回好友列表</a><br/>
<%
String chatRoomId = (String)session.getAttribute("chatroomid");
if(chatRoomId==null || chatRoomId.equals("")){
	chatRoomId = "0";
}
%>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>