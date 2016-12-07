<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
action.linkMan(request);
LinkedList linkManList  = (LinkedList)request.getAttribute("linkManList");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="最近联系人">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(linkManList!=null && linkManList.size()>0){%>
最近联系人：<br/>
<%
Iterator iter = linkManList.iterator();
while(iter.hasNext()){
	int userId = ((Integer)iter.next()).intValue();
	UserBean user=UserInfoUtil.getUser(userId);
	if(user==null)continue;%>
<a href="/chat/post.jsp?toUserId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
<%}}else{%>您的最近联系人记录为空<br/><%}%><br/>
<a href="/chat/hall.jsp">返回聊天</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>