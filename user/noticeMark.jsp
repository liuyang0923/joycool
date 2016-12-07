<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.user.SendAction" %><%
response.setHeader("Cache-Control","no-cache");
if(request.getParameter("mark")!=null){
SendAction action = new SendAction(request);
action.noticeMark(request);
}
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
String str= null;
boolean flag=loginUser.noticeMark();
str=flag==true?"免打扰":"公开";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="免打扰功能">
<p align="left">
<%=BaseAction.getTop(request, response)%>
(注:本功能只在用户每次登录设置以后生效,下线后即失效,再次登录后需重新设置)<br/>
您当前的状态为<%=str%>.<br/>
<%if(loginUser.noticeMark()){%>
<a href="/user/noticeMark.jsp?mark=0" title="进入">退出免打扰状态</a><br/>
<%}else{%>
<a href="/user/noticeMark.jsp?mark=1" title="进入">进入免打扰状态</a><br/>
<%}%><br/>
<a href="/user/userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>