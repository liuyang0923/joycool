<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.*" %><%
response.setHeader("Cache-Control","no-cache");
//response.sendRedirect(("index.jsp"));
ForumAction action=new ForumAction(request);
if(!action.isLogin()) {
	action.innerRedirect("needlogin.jsp", response);
	return;
}
action.searchUserRs(request,response); 
String result =(String)request.getAttribute("result");
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="查找结果" ontimer="<%=response.encodeURL("index.jsp")%>">
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
String prefixUrl = (String) request.getAttribute("prefixUrl");
%>
<card title="查找结果">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(forumContentIdList!=null){
   for(int i=0;i<forumContentIdList.size();i++){
  	Integer contentId =(Integer)forumContentIdList.get(i);
    ForumContentBean forumContent = ForumCacheUtil.getForumContent(contentId.intValue());
    if(forumContent==null){continue;}
    if(forumContent.isPeak()){%>[顶]<%}else if(forumContent.getMark1()==1){%>[精]<%}else{%><%=i+1%>.<%}%>
      <a href="viewContent.jsp?contentId=<%=forumContent.getId()%>&amp;forumId=<%=forumContent.getForumId()%>&amp;sUserId=<%=forumContent.getUserId()%>"><%=StringUtil.toWml(StringUtil.limitString(forumContent.getTitle(),30))%></a>(<%=forumContent.getReply()%>|<%=forumContent.getCount()%>)<br/>
    <%}
   }%>
<%=PageUtil.shuzifenye(pagingBean, prefixUrl, true, "|", response)%>
<a href="searchUser.jsp">继续搜索</a><br/>
<%if(forum!=null){%>
<a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%}%>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>