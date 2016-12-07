<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestAction action = new GuestAction(request);
   GuestHallAction action2 = new GuestHallAction(request,response);
   GuestUserInfo guestUser = action2.getGuestUser();
   String tip = "";
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游客聊天室"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>==游客聊天室说明==<br/>
本聊天室是为没有注册成正式用户的游客,提供的交流场所.<br/>
游客可以在这里进行彼此间交流.<br/>
正式用户也可以在这里和游客交流.<br/>
一旦点击"退出",则不会被别人再找到.<br/>
欢迎各位游客有良好的体验,并<a href="../register.jsp">升级注册成乐酷正式用户</a>,一切免费哦!<br/>
<a href="chat.jsp">返回聊天室</a><br/><%if(guestUser != null){%>您的临时昵称:<%=guestUser.getUserNameWml()%><br/><%}%>
<%
} else {
	%><%=tip%><br/><a href="chat.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>