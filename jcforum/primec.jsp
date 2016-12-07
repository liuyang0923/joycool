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

Object[] adminObj = (Object[])session.getAttribute("primeAdmin"+forum.getId());
HashSet adminSelect = null;
if(adminObj!=null) {	// 管理精华区
	adminSelect = (HashSet)adminObj[0];
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="forum.jsp?forumId=<%=forum.getId()%>"><%=StringUtil.toWml(forum.getTitle())%></a>&gt;<%if(catBean.getParentId()==0){%>分类精华<%}else{%><a href="prime.jsp?cat=<%=forum.getPrimeCat()%>">分类精华</a>&gt;<%=StringUtil.toWml(catBean.getName())%><%}%><br/>
修改分类:<br/>
分类名:<input name="catname"  maxlength="10" value=""/><br/>
<anchor title="确定">确认修改分类
<go href="prime.jsp?a=8&amp;cat=<%=catId%>" method="post">
	<postfield name="name" value="$catname"/>
</go>
</anchor><br/>
<br/>
<a href="prime.jsp?a=7&amp;cat=<%=catId%>">删除空分类</a><br/>

<a href="prime.jsp?cat=<%=catId%>">返回分类</a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>