<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.NoticeUtil"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%!
static IUserService userService = ServiceFactory.createUserService();
%><%
response.setHeader("Cache-Control","no-cache");
int toId=StringUtil.toInt(request.getParameter("id"));
String action = request.getParameter("action");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
UserBean toUser=(UserBean)UserInfoUtil.getUser(toId);
if(toUser==null){
	out.clearBuffer();
	response.sendRedirect("/bottom.jsp");
	return;
}
%>
<card title="删除好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您选择删除好友<%=toUser.getNickNameWml()%>,你对他的友好度将变成零。
<br/>
<a href="/user/OperFriend.do?delete=1&amp;friendId=<%=toUser.getId()%>">确定删除好友</a><br/>
<a href="/chat/post.jsp?toUserId=<%=toId%>">取消</a><br/>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>