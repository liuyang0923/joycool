<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.jcforum.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%
response.setHeader("Cache-Control","no-cache");
if(!true){
	request.getRequestDispatcher("/Column.do?columnId=10939").forward(request, response);
	return;
}
ForumxAction action=new ForumxAction(request);
ForumxAction.updateLatestNews();
ForumContentBean con = null;
List list = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="新闻首页">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
==【<a href="list.jsp?id=10">世博热报</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(10,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
   			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
   			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=9">乐酷热点</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(9,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
   			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
   			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=20">女性话题</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(20,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
   			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
   			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=21">情感</a>.<a href="list.jsp?id=25">星座</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(21,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
		con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
		if (con == null) continue;
		%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=23">八卦</a>.<a href="list.jsp?id=22">时尚</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(23,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=6">娱乐</a>.<a href="list.jsp?id=7">体育</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(6,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
   			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
   			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=5">军事</a>.<a href="list.jsp?id=8">科技</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(5,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
   			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
   			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=27">心理</a>.<a href="list.jsp?id=26">幽默</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(27,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=24">美体</a>.<a href="list.jsp?id=28">化妆</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(24,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=2">国内</a>.<a href="list.jsp?id=1">国际</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(2,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
   			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
   			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=3">社会</a>.<a href="list.jsp?id=4">财经</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(3,3);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
   			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
   			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>