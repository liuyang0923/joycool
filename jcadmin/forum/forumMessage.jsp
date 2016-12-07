<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.forum.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end

response.setHeader("Cache-Control","no-cache");
        String condition = null;
		int FORUM_MESSAGE_PER_PAGE = 50;
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
		
		int i, count;
        ForumMessageBean fm = null;
%>
<p align="left">
<%=message.getTitle()%><br/>
<%=message.getUserNickname()%>|<%=message.getCreateDatetime()%><br/>
<a href="deleteMessage.jsp?id=<%=message.getId()%>">删除本主题</a><br/>
--------------------<br/>
<%
//第一页
if(pageIndex == 0){
%>
<%=message.getContent()%><br/>
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
<%=(pageIndex * 5 + i + 1)%>.<%=fm.getTitle()%>(<%=fm.getUserNickname()%>|<%=fm.getCreateDatetime()%>):<%=fm.getContent()%>|<a href="deleteMessage.jsp?id=<%=fm.getId()%>">删除</a><br/>
<%
	if(fm.getAttachment() != null && !fm.getAttachment().equals("")){
%>
<a href="<%=fm.getAttachmentURL()%>"><img src="<%=fm.getAttachmentURL()%>" alt="loading..."/></a><br/>
<%
    }
}
%>
<%=PageUtil.shangxiafenye(totalPageCount, pageIndex, prefixUrl, true, "<br/>", response)%><br/>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>
<a href="forumIndex.jsp?id=<%=forum.getId()%>">返回<%=forum.getName()%></a><br/>
</p>