<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%
response.setHeader("Cache-Control","no-cache");
// 添加帖子到精华
ForumAction action=new ForumAction(request);
	int contentId = action.getParameterInt("prime");
	ForumContentBean con = ForumCacheUtil.getForumContent(contentId);
	if(con == null){
		response.sendRedirect("index.jsp");
		return;
	}
	ForumBean forum = ForumCacheUtil.getForumCache(con.getForumId());
	if(forum == null||con.getMark1()==0){
		response.sendRedirect("viewContent.jsp?contentId=" + con.getId());
		return;
	}
	UserBean loginUser = action.getLoginUser();
	if (!ForumAction.isSuperAdmin(loginUser.getId()) && !forum.getUserIdSet().contains(new Integer(loginUser.getId()))){
		response.sendRedirect("forum.jsp?forumId="+forum.getId());
		return;
	}
	// 放入精华分类
	int catId = action.getParameterIntS("cat");
	PrimeCatBean catBean = ForumAction.getForumService().getPrimeCat("id="+catId);
	if(catBean == null||catBean.getForumId()!=forum.getId()){
		response.sendRedirect("viewContent.jsp?contentId=" + con.getId());
		return;
	}
	DbOperation db = new DbOperation(2);
	int contentCat = db.getIntResult("select cat_id from jc_forum_prime where id=" + con.getId());
	if(contentCat != catBean.getId()) {
		db.executeUpdate("update jc_forum_prime set cat_id=" + catId + " where id="+con.getId());
		if(contentCat != 0){
			db.executeUpdate("update jc_forum_prime_cat set thread_count=thread_count-1 where id="+contentCat);
		}
		db.executeUpdate("update jc_forum_prime_cat set thread_count=thread_count+1 where id=" + catId);
	}
	db.release();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
帖子【<a href="viewContent.jsp?contentId=<%=contentId%>"><%=StringUtil.toWml(con.getTitle())%></a>】已加入精华,所属分类<a href="prime.jsp?cat=<%=catId%>"><%=StringUtil.toWml(catBean.getName())%></a><br/>

<a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>