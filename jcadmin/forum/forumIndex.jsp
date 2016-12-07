<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.forum.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.util.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end

response.setHeader("Cache-Control","no-cache");
        int FORUM_MESSAGE_PER_PAGE = 50;
        IForumMessageService service = ServiceFactory
                .createForumMessageService();
        String condition = null;
        IForumService forumService = ServiceFactory.createForumService();

        //取得参数
        //论坛id
        int id = StringUtil.toInt(request.getParameter("id"));
        //页码
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        if (pageIndex == -1) {
            pageIndex = 0;
        }
        //order by
        String ob = request.getParameter("ob");
        if (ob == null || ob.equals("")) {
            ob = "id";
        }

        /**
         * 取得Forum
         */
        condition = "id = " + id;
        ForumBean forum = forumService.getForum(condition);
        if (forum == null) {
            return;
        }

        /**
         * 分页相关
         */
        condition = "forum_id = " + id + " and parent_id = 0";
        int totalCount = service.getForumMessageCount(condition);

        int totalPageCount = totalCount / FORUM_MESSAGE_PER_PAGE;
        if (totalCount % FORUM_MESSAGE_PER_PAGE != 0) {
            totalPageCount++;
        }
        if (totalPageCount == 0) {
            pageIndex = 0;
        } else if (totalPageCount != 0 && pageIndex >= totalPageCount) {
            pageIndex = totalPageCount - 1;
        }
        String prefixUrl = "forumIndex.jsp?id=" + id + "&ob=" + ob;

        /**
         * 取得主题列表
         */
        condition += " ORDER BY " + ob + " DESC LIMIT " + pageIndex
                * FORUM_MESSAGE_PER_PAGE + ", " + FORUM_MESSAGE_PER_PAGE;
        Vector forumMessageList = service.getForumMessageList(condition);

		int count, i;
		ForumMessageBean fm = null;
%>
<p align="left">
<%=forum.getName()%><br/>
--------------------<br/>
本论坛承办体坛周报世界杯论战！谁会是新的名嘴？你来选！<br/>
<a href="forumIndex.jsp?id=<%=forum.getId() %>&ob=id">按时间</a>|<a href="forumIndex.jsp?id=<%=forum.getId() %>&ob=hits">按人气</a><br/>
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
<a href="forumMessage.jsp?id=<%=fm.getId()%>"><%=(pageIndex * 5 + i + 1)%>.<%if(hasAttach){%>[图]<%}%><%=fm.getTitle()%>(<%=fm.getHits()%>|<%=fm.getUserNickname()%>|<%=fm.getCreateDatetime()%>)</a>|<a href="deleteMessage.jsp?id=<%=fm.getId()%>">删除</a><br/>
<%
}
%>
<%=PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response)%><br/>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>
</p>