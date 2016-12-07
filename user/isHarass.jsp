<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
action.isHarass(request);
String roomId = (String) request.getParameter("roomId");
if (roomId == null) {
	roomId = "0";
}
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
String flag=null;
int harass=loginUser.getHarass();
flag=harass!=0?"免骚扰":"公开";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="免骚扰功能">
<p align="left">
<%=BaseAction.getTop(request, response)%>
(注:本功能只在用户每次登录设置以后生效,下线后即失效,再次登录后需重新设置)<br/>
您当前的状态为<%=flag%>.<br/>
<%if(loginUser.getHarass()==0){%>
<a href="/user/isHarass.jsp?flag=1">进入免骚扰状态</a><br/>
<%}else{%>
<a href="/user/isHarass.jsp?flag=0">退出免骚扰状态</a><br/>
<%}%>
<a href="/user/userMessageList.jsp">返回通知管理</a>
<a href="/chat/hall.jsp?roomId=<%=roomId%>">返回聊天室</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>