<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.forum.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
ForumBean forum = (ForumBean) request.getAttribute("forum");
Vector forumMessageList = (Vector) request.getAttribute("forumMessageList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int cpage = ((Integer) request.getAttribute("page")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");

int i, count;
ForumMessageBean fm = null;
IForumMessageService service = ServiceFactory.createForumMessageService();

IChatService chatService = ServiceFactory.createChatService();
Vector chatList = chatService.getMessageList("is_private = 0 order by id desc limit 0, 3");
Iterator itr = chatList.iterator();
JCRoomChatAction chatAction = new JCRoomChatAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=forum.getName()%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(forum.getId() == 13){
%>
<img src="logo.gif" alt="文斗世界杯"/><br/>
本论坛承办体坛周报世界杯论战！当日人气最高的3个贴子将发表在次日的体坛周报上，详情参见<a href="http://wap.joycool.net/Column.do?columnId=4593">活动规则</a>！<br/>
<%
}
else {
%>
<%=StringUtil.toWml(forum.getName())%><br/>
--------------------<br/>
<%
}
%>
<%
if(loginUser != null){
%>
<a href="post.jsp?forumId=<%=forum.getId() %>&amp;parent=0">发文</a>|<a href="postAttach.jsp?forumId=<%=forum.getId() %>&amp;parent=0">贴图</a><br/>
<%
}
%>
<a href="ForumIndex.do?id=<%=forum.getId() %>&amp;ob=id">按时间</a>|<a href="ForumIndex.do?id=<%=forum.getId() %>&amp;ob=hits">按人气</a><br/>
<%
count = forumMessageList.size();
boolean hasAttach;
for(i = 0; i < count; i ++){
	fm = (ForumMessageBean) forumMessageList.get(i);
	hasAttach = false;
	if(fm.getAttachment() != null && !fm.getAttachment().equals("")){
		hasAttach = true;
	}
%>
<a href="ForumMessage.do?id=<%=fm.getId()%>"><%=(cpage * 5 + i + 1)%>.<%if(hasAttach){%>[图]<%}%><%=StringUtil.toWml(fm.getTitle())%>(点击<%=fm.getHits()%>|回复<%=service.getForumMessageCount("parent_id = " + fm.getId())%>|<%=StringUtil.toWml(fm.getUserNickname())%>)</a><br/>
<%
}
%>
<%=PageUtil.shuzifenye(totalPageCount, cpage, prefixUrl.replace("&", "&amp;"), true, "|", response)%><br/>
第<%=(totalPageCount == 0? cpage : (cpage + 1))%>页|共<%=totalPageCount%>页<br/>
<a href="http://wap.joycool.net/Column.do?columnId=4597">上期登报用户名单</a><br/>
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