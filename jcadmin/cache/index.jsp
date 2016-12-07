<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%
String clear = request.getParameter("clear");
if(clear != null){
	if("news".equals(clear)){
		OsCacheUtil.flushGroup(OsCacheUtil.NEWS_GROUP);
	}
	else if("game".equals(clear)){
		OsCacheUtil.flushGroup(OsCacheUtil.GAME_GROUP);
	}
	else if("image".equals(clear)){
		CacheManage.image.clear();
	}
	else if("column".equals(clear)){
		CacheManage.column.clear();
	}
	else if("notice".equals(clear)){
		NoticeCacheUtil.removeAll();
		CacheManage.notice.clear();
		CacheManage.noticeList.clear();
	}
	else if (OsCacheUtil.FRIEND_LINK_GROUP.equals(clear)){
		OsCacheUtil.flushGroup(OsCacheUtil.FRIEND_LINK_GROUP);
	}
	//response.sendRedirect("index.jsp");
	BaseAction.sendRedirect("/jcadmin/cache/index.jsp", response);
	return;
}
%>

缓存管理<br>
<br>
<a href="index.jsp?clear=news">清空新闻缓存</a><br>
<a href="index.jsp?clear=game">清空游戏缓存</a><br>
<a href="index.jsp?clear=ebook">清空电子书缓存</a><br>
<a href="index.jsp?clear=image">清空图片缓存</a><br>
<a href="index.jsp?clear=column">清空树状页面缓存</a><br>
<a href="index.jsp?clear=notice">清空通知系统缓存</a><br>
<a href="index.jsp?clear=<%= OsCacheUtil.FRIEND_LINK_GROUP %>">清空友链系统缓存</a><br>
<br>
查看缓存<br>
<br>
<a href="cacheList.jsp?group=news">查看新闻缓存</a><br>
<a href="cacheList.jsp?group=game">查看游戏缓存</a><br>
<a href="cacheList.jsp?group=ebook">查看电子书缓存</a><br>
<a href="cacheList.jsp?group=image">查看图片缓存</a><br>
<a href="cacheList.jsp?group=column">查看树状页面缓存</a><br>