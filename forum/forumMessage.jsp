<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.forum.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
        response.setHeader("Cache-Control","no-cache");
        UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
        String condition = null;
		int FORUM_MESSAGE_PER_PAGE = 8;
        IForumService forumService = ServiceFactory.createForumService();
        IForumMessageService forumMessageService = ServiceFactory
                .createForumMessageService();

        //取得参数
        //主题id
        int id = StringUtil.toInt(request.getParameter("id"));
        //页码
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        if (pageIndex == -1) {
            pageIndex = 0;
        }

        /**
         * 取得主题
         */
        condition = "id = " + id;
        ForumMessageBean message = forumMessageService
                .getForumMessage(condition);
        if (message == null) {
            return;
        }
        condition = "id = " + message.getForumId();
        ForumBean forum = forumService.getForum(condition);
        if (forum == null) {
            return;
        }

        /**
         * 分页相关
         */
        condition = "parent_id = " + id;
        int totalCount = forumMessageService.getForumMessageCount(condition);

        int totalPageCount = totalCount
                / FORUM_MESSAGE_PER_PAGE;
        if (totalCount %FORUM_MESSAGE_PER_PAGE != 0) {
            totalPageCount++;
        }
        if (totalPageCount == 0) {
            pageIndex = 0;
        } else if (totalPageCount != 0 && pageIndex >= totalPageCount) {
            pageIndex = totalPageCount - 1;
        }
        String prefixUrl = "forumMessage.jsp?id=" + id;

        /**
         * 取得主题列表
         */
        condition += " ORDER BY id DESC LIMIT " + pageIndex
                * FORUM_MESSAGE_PER_PAGE + ", "
                + FORUM_MESSAGE_PER_PAGE;
        Vector forumMessageList = forumMessageService
                .getForumMessageList(condition);    
                
        /**
         * 更新点击率
         */
        if (pageIndex == 0) {
            condition = "id = " + id;
            String set = "hits = (hits + 1)";
            forumMessageService.updateForumMessage(set, condition);
        } 
		
		int i, count;
        ForumMessageBean fm = null;

//		IChatService chatService = ServiceFactory.createChatService();
//		Vector chatList = chatService.getMessageList("is_private = 0 order by id desc limit 0, 3");
//		Iterator itr = chatList.iterator();
//		JCRoomChatAction chatAction = new JCRoomChatAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(message.getTitle().replace("\"", "'"))%>">
<p align="left"><%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(message.getTitle())%><br/>
<a href="/user/ViewUserInfo.do?userId=<%=message.getUserId() %>&amp;backTo=<%=PageUtil.getBackTo(request)%>"><%=StringUtil.toWml(message.getUserNickname())%></a>|<%=message.getCreateDatetime()%><br/>
<%
if(loginUser != null){
%>
<a href="post.jsp?forumId=<%=message.getForumId() %>&amp;parent=<%=message.getId()%>">回复</a><% if(message.getForumId()!=15){ %>|<a href="postAttach.jsp?forumId=<%=message.getForumId() %>&amp;parent=<%=message.getId()%>">贴图</a><% } %><br/>
<%
}
%>
--------------------<br/>
<%
//第一页
if(pageIndex == 0){
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
==回复列表==<br/>
<%
count = forumMessageList.size();
for(i = 0; i < count; i ++){
	fm = (ForumMessageBean) forumMessageList.get(i);
%>
<%=(pageIndex * FORUM_MESSAGE_PER_PAGE + i + 1)%>.<a href="/user/ViewUserInfo.do?userId=<%=fm.getUserId() %>&amp;backTo=<%=PageUtil.getBackTo(request)%>"><%=StringUtil.toWml(fm.getUserNickname())%></a>:<%=StringUtil.toWml(fm.getContent())%>(<%=fm.getCreateDatetime()%>)<br/>
<%
	if(fm.getAttachment() != null && !fm.getAttachment().equals("")){
%>
<a href="<%=fm.getAttachmentURL()%>"><img src="<%=fm.getAttachmentURL()%>" alt="loading..."/></a><br/>
<%
    }
}
if(totalPageCount > 1){
%>
<%=PageUtil.shangxiafenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "<br/>", response)%><br/>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>
<%
}
%>
<a href="forumIndex.jsp?id=<%=forum.getId()%>"><%= (forum.getId()!=15)?"返回论坛":"返回" + forum.getName() %></a><br/>
<% if(forum.getId()!=15){ %>
==聊天室动态==<br/>
<%--
while(itr.hasNext()){
    out.print(chatAction.getMessageDisplay((JCRoomContentBean)itr.next(), request, response));
	out.print("<br/>");
}
--%>
<% } %>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>