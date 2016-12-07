<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.forum.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
        response.setHeader("Cache-Control","no-cache");
        UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
        int FORUM_MESSAGE_PER_PAGE = 8;
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
				
        IChatService chatService = ServiceFactory.createChatService();
        Vector chatList = chatService.getMessageList("is_private = 0 order by id desc limit 0, 3");
        Iterator itr = chatList.iterator();
        JCRoomChatAction chatAction = new JCRoomChatAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=forum.getName()%>">
<p align="left"><%=BaseAction.getTop(request, response)%>
<%
if(forum.getId() == 13){
%>
<img src="logo.gif" alt="文斗世界杯"/><br/>
场上竞技"武斗"烽烟四起，场下论战"文斗"鏖战正酣！世界杯话题热议中，每日人气帖将刊登在次日体坛周报上！<a href="http://wap.joycool.net/Column.do?columnId=4593">活动规则</a><br/>
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
    if(id!=15){ 
%>
<a href="post.jsp?forumId=<%=forum.getId() %>&amp;parent=0">发文</a>|<a href="postAttach.jsp?forumId=<%=forum.getId() %>&amp;parent=0">贴图</a><br/>
<%
    }
    else{
%>
如果您在浏览中遇到任何问题,或者有好的建议或意见请及时与我们联系。<br/>
联系QQ：491269032<br/>
联系电话:010－62059116－303或305或306<br/>
如方便可直接留言给我公司,非常感谢。<br/>
<a href="post.jsp?forumId=<%=forum.getId() %>&amp;parent=0">发表您的意见或建议</a><br/>
<%    	
    }
}

    if(id!=15){
%>
<a href="forumIndex.jsp?id=<%=forum.getId() %>&amp;ob=id">按时间</a>|<a href="forumIndex.jsp?id=<%=forum.getId() %>&amp;ob=hits">按人气</a><br/>
<%
    }
    else{
%>
<a href="forumIndex.jsp?id=<%=forum.getId() %>&amp;ob=id">按时间</a>|<a href="forumIndex.jsp?id=<%=forum.getId() %>&amp;ob=hits">按人数</a><br/>
<%    	
    }
count = forumMessageList.size();
boolean hasAttach;
for(i = 0; i < count; i ++){
	fm = (ForumMessageBean) forumMessageList.get(i);
	hasAttach = false;
	if(fm.getAttachment() != null && !fm.getAttachment().equals("")){
		hasAttach = true;
	}
%>
<a href="forumMessage.jsp?id=<%=fm.getId()%>"><%=(pageIndex * FORUM_MESSAGE_PER_PAGE + i + 1)%>.<%if(hasAttach){%>[图]<%}%><%=StringUtil.toWml(fm.getTitle())%>(点击<%=fm.getHits()%>|回复<%=service.getForumMessageCount("parent_id = " + fm.getId())%>|<%=StringUtil.toWml(fm.getUserNickname())%>)</a><br/>
<%
}
%>
<%=PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "|", response)%><br/>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>
<% if(id!=15){ %>
<a href="http://wap.joycool.net/Column.do?columnId=4597">上期登报用户名单</a><br/>
<a href="http://wap.joycool.net/wgamehall/football/index.jsp">点球决战</a>&nbsp;<a href="http://wap.joycool.net/wgamepk/football/index.jsp">射门</a>&nbsp;<a href="http://wap.joycool.net/worldcup/index.jsp">博彩</a><br/>
==聊天室动态==<br/>
<%
while(itr.hasNext()){
    out.print(chatAction.getMessageDisplay((JCRoomContentBean)itr.next(), request, response));
	out.print("<br/>");
}
%>
<% } %>
<a href="/user/onlineManager.jsp?forumId=355">返回警察局</a><br/>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>