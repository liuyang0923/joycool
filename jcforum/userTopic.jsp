<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumUserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
//action.userTopic(); 	// 写jsp吧

	int userId = action.getParameterInt("u");			// 如果是0则查询自己的？
	UserBean user = null;
	if(userId > 0)
		user = UserInfoUtil.getUser(userId);
	PagingBean paging = null;
	if (user == null) {
		action.tip(null, "该用户不存在！");
	} else {
		ForumUserBean fu = ForumCacheUtil.getForumUser(userId);
		int totalCount = 0;
		if(fu!=null)
			totalCount=fu.getThreadCount();
		if(totalCount>100)
			totalCount=100;
		paging = new PagingBean(action, totalCount, 10, "p");
		String sql = "select id from jc_forum_content where user_id=" + userId
				+ " and del_mark=0 order by id desc limit "+paging.getStartIndex()+",10";
		List forumContentIdList = SqlUtil
				.getIntList(sql, 2);
	
		request.setAttribute("forumContentIdList", forumContentIdList);
		action.tip("success");
	}

UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/jcforum/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(!action.isResult("success")){%>
<card title="用户主题" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%>(3秒后跳转论坛首页)<br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
List forumContentIdList=(List)request.getAttribute("forumContentIdList");
%>
<card title="用户主题">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>的帖子<br/>
<%if(forumContentIdList!=null){
   for(int i=0;i<forumContentIdList.size();i++){
  	Integer contentId =(Integer)forumContentIdList.get(i);
    ForumContentBean forumContent = ForumCacheUtil.getForumContent(contentId.intValue());
    if(forumContent==null){continue;}
    if(forumContent.isPeak()){%>[顶]<%}else if(forumContent.getMark1()==1){%>[精]<%}else{%><%=i+1%>.<%}%><a href="viewContent.jsp?contentId=<%=forumContent.getId()%>&amp;forumId=<%=forumContent.getForumId()%>"><%=StringUtil.toWml(forumContent.getTitle())%></a>(<%=forumContent.getReply()%>|<%=forumContent.getCount()%>)<br/>
<%}}%>
<%=paging.shuzifenye("userTopic.jsp?u=" + user.getId(), true, "|", response)%>
<%if(user.getHome()==1){%><a href="/home/home.jsp?userId=<%=user.getId()%>">返回<%=user.getGenderText()%>的家园</a><br/><%}%>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>