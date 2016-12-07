<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestAction action = new GuestAction(request);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游客聊天室" ontimer="<%=response.encodeURL("/guest/index.jsp")%>"><timer value="10"/><p>
<%=BaseAction.getTop(request, response)%>
您已经退出游客聊天室,不会再被其他游客骚扰!(1秒钟后跳到首页)<br/>
<a href="/guest/index.jsp">直接跳转</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>