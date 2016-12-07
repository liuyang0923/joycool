<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.LinkBean" %><%@ page import="net.joycool.wap.action.LinkAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
action.top10(request); 
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷十大热贴">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
List contentList = (List)request.getAttribute("top10");
if(contentList!=null){
   for(int i=0;i<contentList.size();i++){
		Integer iid = (Integer)contentList.get(i);
		ForumContentBean forum1=ForumCacheUtil.getForumContent(iid.intValue());
		if(forum1!=null){%>
<%=i+1%>.<a href="/jcforum/viewContent.jsp?contentId=<%=forum1.getId()%>&amp;forumId=<%=forum1.getForumId()%>"><%=StringUtil.toWml(StringUtil.limitString(forum1.getTitle(),30))%></a>(<%=forum1.getReply()%>|<%--点击--%><%=forum1.getCount()%>)<br/>
<%		}
	}
}%>
<a href="index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>