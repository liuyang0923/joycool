<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.SqlUtil"%><%@ page import="java.text.SimpleDateFormat"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%!
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.dd");
%><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
ForumUserBean forumUser = action.getForumUser();
int catId = action.getParameterInt("cat");
PrimeCatBean catBean = ForumAction.getForumService().getPrimeCat("id="+catId);
if(catBean==null){
	response.sendRedirect("index.jsp");
	return;
}
ForumBean forum = ForumCacheUtil.getForumCache(catBean.getForumId());
UserBean loginUser = action.getLoginUser();
if(!action.isMethodGet()){
	String name = action.getParameterNoEnter("name");
	PrimeCatBean cat = new PrimeCatBean();
	cat.setName(name);
	cat.setUserId(loginUser.getId());
	cat.setParentId(catId);
	ForumAction.addPrimeCat(forum, cat);
	response.sendRedirect("prime.jsp?cat="+catId);
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="forum.jsp?forumId=<%=forum.getId()%>"><%=StringUtil.toWml(forum.getTitle())%></a>&gt;<%if(catBean.getParentId()==0){%>分类精华<%}else{%><a href="prime.jsp?cat=<%=forum.getPrimeCat()%>">分类精华</a>&gt;<%=StringUtil.toWml(catBean.getName())%><%}%><br/>

分类名:<input name="catname"  maxlength="10" value=""/><br/>
<anchor title="确定">创建分类
<go href="primeAdd.jsp?cat=<%=catId%>" method="post">
	<postfield name="name" value="$catname"/>
</go>
</anchor><br/>
<%if(catBean.getParentId()!=0){%><a href="prime.jsp?cat=<%=catBean.getParentId()%>">返回上一级</a><br/><%}%>

<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>