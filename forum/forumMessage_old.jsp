<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.forum.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
ForumBean forum = (ForumBean) request.getAttribute("forum");
ForumMessageBean message = (ForumMessageBean) request.getAttribute("message");
Vector forumMessageList = (Vector) request.getAttribute("forumMessageList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int cpage = ((Integer) request.getAttribute("page")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");

int i, count;
ForumMessageBean fm = null;

IChatService chatService = ServiceFactory.createChatService();
Vector chatList = chatService.getMessageList("is_private = 0 order by id desc limit 0, 3");
Iterator itr = chatList.iterator();
JCRoomChatAction chatAction = new JCRoomChatAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(message.getTitle())%>">
<p align="left"><%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(message.getTitle())%><br/>
<%=StringUtil.toWml(message.getUserNickname())%>|<%=message.getCreateDatetime()%><br/>
<%
if(loginUser != null){
%>
<a href="post.jsp?forumId=<%=message.getForumId() %>&amp;parent=<%=message.getId()%>">回复</a>|<a href="postAttach.jsp?forumId=<%=message.getForumId() %>&amp;parent=<%=message.getId()%>">贴图</a><br/>
<%
}
%>
--------------------<br/>
<%
//第一页
if(cpage == 0){
%>
<%=StringUtil.toWml(message.getContent())%><br/>
<%
	if(message.getAttachment() != null && !message.getAttachment().equals("")){
%>
<a href="<%=message.getAttachmentURL()%>"><img src="<%=message.getAttachmentURL()%>" alt="loading..."/></a><br/>
<%
    }
}
%>
评论列表：<br/>
<%
count = forumMessageList.size();
for(i = 0; i < count; i ++){
	fm = (ForumMessageBean) forumMessageList.get(i);
%>
<%=(cpage * 5 + i + 1)%>.<%=StringUtil.toWml(fm.getTitle())%>(<%=StringUtil.toWml(fm.getUserNickname())%>|<%=fm.getCreateDatetime()%>):<%=StringUtil.toWml(fm.getContent())%><br/>
<%
	if(fm.getAttachment() != null && !fm.getAttachment().equals("")){
%>
<a href="<%=fm.getAttachmentURL()%>"><img src="<%=fm.getAttachmentURL()%>" alt="loading..."/></a><br/>
<%
    }
}
%>
<%=PageUtil.shangxiafenye(totalPageCount, cpage, prefixUrl.replace("&", "&amp;"), true, "<br/>", response)%><br/>
第<%=(totalPageCount == 0? cpage : (cpage + 1))%>页|共<%=totalPageCount%>页<br/>
<a href="ForumIndex.do?id=<%=forum.getId()%>">返回<%=forum.getName()%></a><br/>
==聊天室动态==<br/>
<%
while(itr.hasNext()){
    out.print(chatAction.getMessageDisplay((JCRoomContentBean)itr.next(), request, response));
	out.print("<br/>");
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>