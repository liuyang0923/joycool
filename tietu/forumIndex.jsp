<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.forum.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
        response.setHeader("Cache-Control","no-cache");
if(true){
response.sendRedirect(("/jcforum/forum.jsp?forumId=671"));
return;
}
		String backTo = request.getParameter("backTo");
		if(backTo == null){
			backTo = "/wapIndex.jsp";
		}
		backTo="/lswjs/index.jsp";
        UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
        int FORUM_MESSAGE_PER_PAGE = 10;
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
        		// macq_2006-10-12_获取聊天大厅3条最新的信息_start (注: 传入的值是聊天大厅id)
		Vector contentIdList = (Vector) RoomCacheUtil.getRoomContentCountMap(0);
		Vector chatList = new Vector();
		int contentCount = 0;
		JCRoomContentBean roomContent = null;
		int roomContentId = 0;
		if (contentIdList != null) {
			contentCount = contentIdList.size();
		   }
		int j=0;
		for (int k = 0; k < contentCount; k++) {
			roomContentId = ((Integer) contentIdList.get(k)).intValue();
			roomContent = RoomCacheUtil.getRoomContent(roomContentId);
			if(roomContent.getMark()==0){
				j++;
				chatList.add(roomContent);
				if(j==3){
					break;
				}
			}
		}
		// macq_2006-10-12_获取聊天大厅3条最新的信息_end
        // Vector chatList = chatService.getMessageList("is_private = 0 and mark=0 order by id desc limit 0, 3");
        Iterator itr = chatList.iterator();
        JCRoomChatAction chatAction = new JCRoomChatAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=forum.getName()%>">
<p align="left"><%=BaseAction.getTop(request, response)%><br/>
<%
if(forum.getId() == 13){
%>
<img src="logo.gif" alt="文斗世界杯"/><br/>
场上竞技"武斗"烽烟四起，场下论战"文斗"鏖战正酣！世界杯话题热议中，每日人气帖将刊登在次日体坛周报上！<a href="http://wap.joycool.net/Column.do?columnId=4593">活动规则</a><br/>
<%
}
else {
%>
<%=StringUtil.toWml(forum.getName())%>
<%
int totalTietuCount = 0;
if (chatService.getJCRoomContentCount(" room_id="+Constants.TIETU_TOTAL_COUNT_ID) != null){
JCRoomContentCountBean contentCountBean = chatService.getJCRoomContentCount(" room_id="+Constants.TIETU_TOTAL_COUNT_ID);
	totalTietuCount = contentCountBean.getCount();
%>
(<%=totalTietuCount%>帖)
<br/>
----------------<br/>
<%
}
}
if ("id".equals(ob)){
%>

<a href="forumIndex.jsp?id=<%=forum.getId() %>&amp;ob=hits">按人气</a>|
按时间<br/>
<%
}else if ("hits".equals(ob)){
%>
按人气|
<a href="forumIndex.jsp?id=<%=forum.getId() %>&amp;ob=id">按时间</a><br/>
<%
}
count = forumMessageList.size();

if (count == 0 ){
%>
你是第一个进入贴图中心的！暂时还没有贴图。
<%

}

boolean hasAttach;
for(i = 0; i < count; i ++){
	fm = (ForumMessageBean) forumMessageList.get(i);
	hasAttach = false;
	if(fm.getAttachment() != null && !fm.getAttachment().equals("")){
		hasAttach = true;
	}
%>
<a href="forumMessage.jsp?id=<%=fm.getId()%>&amp;order=<%=ob%>&amp;forumId=<%=id%>"><%=(pageIndex * FORUM_MESSAGE_PER_PAGE + i + 1)%>.<%=StringUtil.toWml(fm.getTitle())%>(点击<%=fm.getHits()%>|评论<%=service.getForumMessageCount("parent_id = " + fm.getId())%>)</a><br/>
<%--
<img src="<%=fm.getAttachmentURL()%>" alt="loading..."/><br/>
--%>
<%
}
%>
<%=PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "|", response)%><br/>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>

<%
if(loginUser != null){
%>
<a href="postAttach.jsp?forumId=<%=forum.getId() %>&amp;parent=0">我也要贴图</a><br/>
<%if(forum.getId()==14){%>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<%}%>
<a href="/wgame/hall.jsp">返回导航中心</a><br/>
<%
}
%>


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