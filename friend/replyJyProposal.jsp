<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.cache.NoticeCacheUtil,net.joycool.wap.util.NewNoticeCacheUtil"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
String reply=request.getParameter("reply");
String drinkId=request.getParameter("drinkId");
int fromId=StringUtil.toInt(request.getParameter("fromId"));
int noticeId=StringUtil.toInt(request.getParameter("noticeId"));
UserBean fromUser=(UserBean)UserInfoUtil.getUser(fromId);
UserBean loginUser=(UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

FriendUserBean friendUser = ServiceFactory.createFriendService().getFriendUser("mark=0 and user_id=" + fromId + " and friend_id=" + loginUser.getId());
if(friendUser==null){
	ServiceFactory.createNoticeService().updateNotice("status=" + NoticeBean.READED, "id=" + noticeId);
	NoticeCacheUtil.removeNoNoticeUserId(loginUser.getId());
	NewNoticeCacheUtil.addUserNoticeById(loginUser.getId());
%>
<card title="结义结果" ontimer="<%=response.encodeURL("/user/ViewUserInfo.do?userId="+fromId)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(fromUser.getNickName())%>的结义请求已经过期！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
else if("2".equals(reply))
{
	FriendAction action=new FriendAction(request);
	boolean flag = action.replyJy(friendUser, 2);
%>
<card title="结义结果" ontimer="<%=response.encodeURL("/user/ViewUserInfo.do?userId="+fromId)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<% if(flag){%>
您答应了<%=StringUtil.toWml(fromUser.getNickName())%>的结义请求！<br/>
<%}else{ %>
<%= (String)request.getAttribute("tip") %><br/>
<%	
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
else if ("1".equals(reply))
{
	FriendAction action=new FriendAction(request);
	boolean flag = action.replyJy(friendUser, 1);
%>
<card title="结义结果" ontimer="<%=response.encodeURL("/user/ViewUserInfo.do?userId="+fromId)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您拒绝了<%=StringUtil.toWml(fromUser.getNickName())%>的结义请求，哎……<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
else
{
	IFriendService friendService = ServiceFactory.createFriendService();
    FriendDrinkBean drink=friendService.getFriendDrink(StringUtil.toInt(drinkId));
%>
<card title="结义">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(fromUser.getNickName())%>用<%= drink.getName() %>向您请求义结金兰！<br/>
<a href="<%=("/friend/replyJyProposal.jsp?reply=2&amp;fromId="+fromId+"&amp;drinkId="+drinkId + (noticeId>0?"&amp;noticeId=" + noticeId:""))%>">答应</a><br/>
<a href="<%=("/friend/replyJyProposal.jsp?reply=1&amp;fromId="+fromId+"&amp;drinkId="+drinkId + (noticeId>0?"&amp;noticeId=" + noticeId:""))%>">拒绝</a><br/>
<a href="/user/ViewUserInfo.do?userId=<%=fromId%>">再考虑考虑</a><br/>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>