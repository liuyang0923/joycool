<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.SqlUtil"%><%@ page import="java.text.SimpleDateFormat"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%!
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.dd");
%><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
int forumId = action.getParameterIntS("forumId");
ForumBean forum = ForumCacheUtil.getForumCache(forumId);
UserBean loginUser = action.getLoginUser();
if(forum==null){
	response.sendRedirect("index.jsp");
	return;
}
int act = action.getParameterInt("a");
if(act==1) {
	if(forum.getPrimeCat()==0){
		// 如果有现成分类，关联上。否则创建新的
		PrimeCatBean cat = ForumAction.getForumService().getPrimeCat("forum_id="+forumId+" and parent_id=0 limit 1");
		if(cat==null){
			cat = new PrimeCatBean();
			cat.setName(forum.getTitle()+"精华区");
			cat.setUserId(loginUser.getId());
			ForumAction.addPrimeCat(forum, cat);
		}
		forum.setPrimeCat(cat.getId());
		SqlUtil.executeUpdate("update jc_forum set prime_cat=" + cat.getId() + " where id=" + forum.getId(), 2);
	}
} else if(act==2)
	if(forum.getPrimeCat()!=0){
		ForumAction.removePrimeCat(forum);
	}

if(forum.getPrimeCat()!=0) {	// 如果有分类精华，进入管理
	String adminKey = "primeAdmin"+forumId;
	if(session.getAttribute(adminKey)==null && ForumAction.isAdmin(forum, loginUser.getId())){
		Object[] adminObj = {new HashSet(4)};	// 目录用负数保存到hashset，帖子用正数
		session.setAttribute(adminKey, adminObj);
	}
	response.sendRedirect("prime.jsp?cat="+forum.getPrimeCat());
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<a href="forum.jsp?forumId=<%=forum.getId()%>">返回论坛</a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>