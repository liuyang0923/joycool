<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page errorPage=""%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.bean.forum.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
        response.setHeader("Cache-Control","no-cache");
        UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
        String condition = null;
		int FORUM_MESSAGE_PER_PAGE = 5;
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

		//zhul 2006-09-08 取得图片ID列表,求上一张，下一张 start
		String order = request.getParameter("order");
		String od=request.getParameter("forumId");
		int[] orderId=null;			//上下面序列
		int prevId=-1;				//上一张id
		int nextId=-1;				//下一张id
		if("14".equals(od)){//只有帖图显示此功能
		
		if(order.equals("id")){		//以ID排序将ORDERID放在SESSION上
		orderId=(int[])session.getAttribute(order);
		if(orderId==null){
		orderId=forumMessageService.getForumMessageId(order) ;
		session.setAttribute(order,orderId);
		}
		}else{						//以点击率排序，以点击时的排序为准
			String unique=request.getParameter("unique");
			if(unique!=null && !unique.equals((String)session.getAttribute("uniqueOrder"))){
			session.setAttribute("uniqueOrder",unique);
			orderId=forumMessageService.getForumMessageId(order) ;
			session.setAttribute("hitsOrder",orderId);
			}else{
			orderId=(int[])session.getAttribute("hitsOrder");
			}
		}

		for(int i=0;i<orderId.length;i++) 
		{
			if(orderId[i]==id)
			{
				if(i!=0) prevId=orderId[i-1];
				if(i!=orderId.length-1) nextId=orderId[i+1];
				break;
			}
		}
		}
		//zhul 2006-09-08 取得图片ID列表,求上一张，下一张 end
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
//		Vector chatList = chatService.getMessageList("is_private = 0 and mark=0 order by id desc limit 0, 3");
//		Iterator itr = chatList.iterator();
//		JCRoomChatAction chatAction = new JCRoomChatAction(request);
        UserStatusBean us =UserInfoUtil.getUserStatus(message.getUserId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(message.getTitle())%>">
<p align="left"><%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(message.getTitle())%><br/>
<%if(us!=null){%><%=us.getHatShow()%><%}%><a href="/user/ViewUserInfo.do?userId=<%=message.getUserId() %>&amp;backTo=<%=PageUtil.getBackTo(request)%>"><%=StringUtil.toWml(message.getUserNickname())%></a>|<%=message.getCreateDatetime()%><br/>
<%
//第一页
//if(pageIndex == 0){
	if(message.getAttachment() != null && !message.getAttachment().equals("")){
	
%>
<a href="<%=message.getAttachmentURL()%>"><img src="<%=message.getAttachmentURL()%>" alt="loading..."/></a><br/>
<%
    }
//}
%>
<%--
图片描述：
--%>
<%
if (!(StringUtil.toWml(message.getContent()).equals(""))){
%>
<%=StringUtil.toWml(message.getContent())%><br/>
<%
}
%>
<%if(prevId!=-1){%><a href="forumMessage.jsp?id=<%=prevId%>&amp;order=<%=order%>&amp;forumId=<%=od%>" >上一张</a><br/><%}%>
<%if(nextId!=-1){%><a href="forumMessage.jsp?id=<%=nextId%>&amp;order=<%=order%>&amp;forumId=<%=od%>" >下一张</a><br/><%}%>
<%--
贴图人：
<%=StringUtil.toWml(message.getUserNickname())%><br/>
--%>
<%
if(loginUser != null){
%>
<a href="post.jsp?forumId=<%=message.getForumId() %>&amp;parent=<%=message.getId()%>">发表评论</a><br/>
<%--
<a href="postAttach.jsp?forumId=<%=message.getForumId() %>&amp;parent=<%=message.getId()%>">回复贴图</a><br/>
--%>
<%
}
%>

聊友评论：<br/>
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
<%
if(loginUser != null){
%>
<a href="postAttach.jsp?forumId=<%=forum.getId() %>&amp;parent=0">我也要贴图</a><br/>
<%
}
%>
<a href="forumIndex.jsp?id=<%=forum.getId()%>">返回贴图中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>