<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.chat.JCRoomContentCountBean"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%
response.setHeader("Cache-Control", "no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="论坛首页">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/jcforum/jcforum.gif" alt="论坛首页"/><br/>
<%if (request.getParameter("city") == null) {
%>
<a href="/jcforum/forumRule.jsp">乐酷论坛须知</a><br/>
<%
int startId = 250;
int i = startId;
	for (i = 0; i < 5; i++) {
		ForumBean forum = ForumCacheUtil.getForumCache(startId + i);
		if (forum != null) {%>
<%=i + 1%>.<a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>"><%=StringUtil.toWml(forum.getTitle())%></a>(<%=forum.getTotalCount()%>贴<%=forum.getTodayCount()%>新贴)<br/>
<%=StringUtil.toWml(forum.getDescription())%><br/>
<%                   
         }
	}
%>
<%IChatService chatService = ServiceFactory.createChatService();
JCRoomContentCountBean contentCountBean = chatService.getJCRoomContentCount(" room_id="+Constants.TIETU_TOTAL_COUNT_ID);
IForumMessageService forumService = ServiceFactory.createForumMessageService();
int totalTietuCount1 =  forumService.getNewForumMessageCount("forum_id=14");
int totalTietuCount = contentCountBean.getCount();

%>
<%-- 
<%=i + 1%>.<a href="/tietu/forumIndex.jsp?id=14">美图秀场</a>(<%=totalTietuCount%>贴<%=totalTietuCount1%>新贴)<br/>极品美图，尽在此处！<br/>
<%=i + 2%>.<a href="/jcforum/index.jsp?city=1">帮会论坛</a><br/>帮会大业，江湖恩怨！ <br/>
--%>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
