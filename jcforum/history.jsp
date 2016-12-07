<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.LinkBean" %><%@ page import="net.joycool.wap.action.LinkAction" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
ForumAction action=new ForumAction(request);
if(!action.isLogin()) {
	action.innerRedirect("needlogin.jsp", response);
	return;
}
action.history(); 
String forumId = (String) request.getAttribute("forumId");
ForumBean forum =(ForumBean)request.getAttribute("forum"); 
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(forum==null){%>
<card title="论坛">
<%}else{%>
<card title="<%=StringUtil.toWml(forum.getTitle())%>历史主题">
<%}%>
<%
String result = (String)request.getAttribute("result");%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(request.getAttribute("tip")!=null){%>
<%=request.getAttribute("tip")%><br/>
<a href="forum.jsp?forumId=<%=forumId%>">返回论坛</a><br/>
<%}
else if(forum!=null){
List contentList = (List)request.getAttribute("contentList");
String popedom = (String) request.getAttribute("popedom");

%>
  <%if(contentList!=null){
   for(int i=0;i<contentList.size();i++){
   ForumContentBean forum1=(ForumContentBean)contentList.get(i);
   if(forum1!=null){
   	 	if(forum1.getAttach()!=null && !("").equals(forum1.getAttach())){%>@<%}%>
<%if(forum1.isPeak()){%>[顶]<%}else if(forum1.getMark1()==1){%>[精]<%}else{%><%=i+1%>.<%}%>
<a href="viewContentHis.jsp?contentId=<%=forum1.getId()%>&amp;forumId=<%=forum1.getForumId()%>"><%=StringUtil.toWml(StringUtil.limitString(forum1.getTitle(),30))%></a>(<%--回复--%><%=forum1.getReply()%>|<%--点击--%><%=forum1.getCount()%>)
<br/>
<%}}}%>
<%=PageUtil.shuzifenye(pagingBean, "history.jsp?forumId=" + forumId, true, "|", response)%>
    <%if(forum.getTongId()>0){%>
    <a href="/tong/tong.jsp?tongId=<%=forum.getTongId()%>">返回帮会首页</a><br/>
    <%}%>
    <%
}
else{%>
 该论坛不存在!<br/>
<%}%>
<a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>