<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
int toUserId=action.getParameterInt("toUserId");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="聊天记录">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
action.viewMessage(request,response,"viewMessages.jsp");
Vector vecMessages=(Vector)request.getAttribute("messages");
String pagination=(String)request.getAttribute("pagination");
JCRoomContentBean content=null;
%>
聊天记录:<br/>
<%
for(int i = 0; i < vecMessages.size(); i ++){
	content = (JCRoomContentBean)vecMessages.get(i);
%>
<%=(i + 1)%>.<%=action.getPrivateMessageDisplay(content, request, response)%><br/>
<%}%>
<%if(pagination!=null||pagination.equals("")){%><%=pagination%><br/><%}%>
<a href="/user/ViewUserInfo.do?userId=<%=toUserId%>" title="进入">返回用户信息</a><br/>
<a href="/chat/hall.jsp" title="进入">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>