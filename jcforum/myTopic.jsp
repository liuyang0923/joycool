<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
action.myTopic(request,response); 
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/jcforum/index.jsp");
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="我的主题" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后跳转论坛首页)<br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
List forumContentIdList=(List)request.getAttribute("forumContentIdList");
ForumBean forum = (ForumBean)request.getAttribute("forum");
%>
<card title="我的主题">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="editContent.jsp?forumId=<%=forum.getId()%>">发表主题</a>|<a href="forum.jsp?forumId=<%=forum.getId()%>">全部主题</a><br/>
<%if(forumContentIdList!=null){
   for(int i=0;i<forumContentIdList.size();i++){
  	Integer contentId =(Integer)forumContentIdList.get(i);
    ForumContentBean forumContent = ForumCacheUtil.getForumContent(contentId.intValue());
    if(forumContent==null){continue;}
    if(forumContent.isPeak()){%>[顶]<%}else if(forumContent.getMark1()==1){%>[精]<%}else{%><%=i+1%>.<%}%><a href="<%=("viewContent.jsp?contentId="+forumContent.getId()+"&amp;forumId="+forumContent.getForumId()+"&amp;myTopic=true")%>"><%=StringUtil.toWml(forumContent.getTitle())%></a>(<%=forumContent.getReply()%>|<%=forumContent.getCount()%>)<br/>
<%}}%>
<%=PageUtil.shuzifenye(pagingBean, "myTopic.jsp?f=" + forum.getId(), true, "|", response)%>
<a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>