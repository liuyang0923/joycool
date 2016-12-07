<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);

//如果不是管理员，则返回聊天大厅
UserBean loginUser = action.getLoginUser();
int roomId = 0;
if (request.getParameter("roomId") != null)
	roomId = StringUtil.toId(request.getParameter("roomId"));
if (!action.isManager(roomId, loginUser.getId())) {
	BaseAction.sendRedirect("/chat/hall.jsp", response);
	return;
}

String url="/chat/kick.jsp?roomId="+roomId+"&amp;kickIndex="+request.getParameter("kickIndex")
+"&amp;onlineIndex="+request.getParameter("onlineIndex")+"&amp;backTo="+request.getParameter("backTo");
//踢人失败直接返回踢人页面（踢管理员时会失败）
if(action.kickUser(request)==false){
	//response.sendRedirect(url);
	BaseAction.sendRedirect(url, response);
}

int status=Integer.parseInt(request.getParameter("status"));
int userId = StringUtil.toInt(request.getParameter("userId"));
String userName="未知";
UserBean user = UserInfoUtil.getUser(userId);
if(user!=null){
	userName = user.getNickName();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if (status==0){ %>
<card title="踢出" ontimer="<%=response.encodeURL(url)%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<timer value="30"/>
<%= StringUtil.toWml(userName) %><br/>
已经被踢出！以后再也进不来了！（3秒钟跳转）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{ %>
<card title="取消踢出" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=userName%><br/>
已经被允许进入！（3秒钟跳转）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%} %>
</wml>