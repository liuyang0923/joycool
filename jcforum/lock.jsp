<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.jcforum.*,net.joycool.wap.cache.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

String contentId = request.getParameter("contentId");

ForumContentBean con = ForumCacheUtil.getForumContent(StringUtil
		.toInt(contentId));

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="锁主题">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(con == null) {%>
不存在的主题<br/>
<%}else {
	ForumBean forum = ForumCacheUtil.getForumCache(con.getForumId());
	
	if(!con.isReadonly()){
	int userBagId = UserBagCacheUtil.getUserBagById(ForumContentBean.LOCK_ITEM, loginUser.getId());
	if(userBagId <= 0) {
%>没有论坛电子锁<br/>
<a href="/shop/index.jsp">&gt;&gt;去商城购买</a><br/>
<%} else {
	UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),userBagId);
	ForumCacheUtil.setReadonly(con);
%>锁帖成功<br/><%
	}}else { %>本帖已经被锁帖！<br/>
<%} %><a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%} %><a href="myTopic.jsp?f=<%=request.getParameter("f") %>">返回我的主题</a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>