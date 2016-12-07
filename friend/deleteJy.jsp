<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.NoticeUtil"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
int toId=StringUtil.toInt(request.getParameter("toId"));
String action = request.getParameter("action");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
UserBean toUser=(UserBean)UserInfoUtil.getUser(toId);
UserBean loginUser=(UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

IUserService userService = ServiceFactory.createUserService();
if("1".equals(action)){
	FriendAction friendAction=new FriendAction(request);
	friendAction.deleteJy(loginUser.getId(), toId);
%>
<card title="割袍断义" ontimer="<%=response.encodeURL("/user/ViewUserInfo.do?userId="+toId)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您与<%=StringUtil.toWml(toUser.getNickName())%>已经恩断义绝，你们之间的友好度变成零。<br/>
<a href="/user/ViewUserInfo.do?userId=<%=toId%>">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
else{
%>
<card title="割袍断义">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您选择与<%=StringUtil.toWml(toUser.getNickName())%>恩断义绝，你们之间的友好度将变成零。
<br/>
<a href="/friend/deleteJy.jsp?action=1&amp;toId=<%=toId%>">确定</a><br/>
<a href="/user/ViewUserInfo.do?userId=<%=toId%>">取消</a><br/>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>