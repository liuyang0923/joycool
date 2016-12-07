<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%
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
	if(forum == null){
		response.sendRedirect("index.jsp");
		return;
	}
	UserBean loginUser = action.getLoginUser();
	if (!ForumAction.isSuperAdmin(loginUser.getId()) && !forum.getUserIdSet().contains(new Integer(loginUser.getId()))){
		response.sendRedirect("forum.jsp?forumId="+forum.getId());
		return;
	}
	
	if (con.getMark1() == 0)	// 加入精华
		ForumCacheUtil.addPrime(con, forum);

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
帖子【<%=StringUtil.toWml(con.getTitle())%>】已加入精华<br/>
<%
if(forum.getPrimeCat()!=0){
	int catId = action.getParameterIntS("cat");
	if(catId==-1)
		catId=forum.getPrimeCat();
	PrimeCatBean catBean = ForumAction.getForumService().getPrimeCat("id="+catId);
	if(catBean!=null){
	List primeCatList = ForumAction.getForumService().getPrimeCatList("parent_id="+catId+" order by id");
%>
【请放入适合的分类】<br/>
<a href="primea2.jsp?prime=<%=con.getId()%>&amp;cat=<%=catBean.getId()%>">放入当前分类[<%=StringUtil.toWml(catBean.getName())%>]</a><br/>
<%		for(int i = 0;i<primeCatList.size();i++){
PrimeCatBean bean = (PrimeCatBean)primeCatList.get(i);

%><%=i+1%>.<%if(bean.getCatCount()>0){%><a href="primea.jsp?prime=<%=con.getId()%>&amp;cat=<%=bean.getId()%>"><%=StringUtil.toWml(bean.getName())%></a><%}else{%><%=StringUtil.toWml(bean.getName())%><%}%>-<a href="primea2.jsp?prime=<%=con.getId()%>&amp;cat=<%=bean.getId()%>">放入</a><br/><%
		}
	if(catBean.getParentId()!=0){%><a href="primea.jsp?prime=<%=con.getId()%>&amp;cat=<%=catBean.getParentId()%>">返回上级分类</a><br/><%}
	}%>
<%}%>
<a href="viewContent.jsp?contentId=<%=contentId%>">返回帖子</a><br/>
<a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>