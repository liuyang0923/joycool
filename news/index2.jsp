<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.jcforum.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%
response.setHeader("Cache-Control","no-cache");
//ForumxAction action=new ForumxAction(request);
ForumxAction.updateLatestNews();
ForumContentBean con = null;
List list = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="八卦首页">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/bagua.gif" alt="bagua"/><br/>
==【<a href="list.jsp?id=20">女性话题</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(20,2);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
   			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
   			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>&amp;tid=20"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=21">情感</a>.<a href="list.jsp?id=25">星座</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(21,2);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
		con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
		if (con == null) continue;
		%><a href="news.jsp?id=<%=list.get(i)%>&amp;tid=21"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=23">八卦</a>.<a href="list.jsp?id=22">时尚</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(23,2);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>&amp;tid=23"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=27">心理</a>.<a href="list.jsp?id=26">幽默</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(27,2);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>&amp;tid=27"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
==【<a href="list.jsp?id=24">美体</a>.<a href="list.jsp?id=28">化妆</a>】==<br/>
<% list = ForumxAction.getRandomLatestNews(24,2);
   if (list != null && list.size() > 0){
   		for (int i = 0;i<list.size();i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con == null) continue;
			%><a href="news.jsp?id=<%=list.get(i)%>&amp;tid=24"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   		}
   }
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>