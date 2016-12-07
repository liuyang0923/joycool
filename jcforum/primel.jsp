<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.SqlUtil"%><%@ page import="java.text.SimpleDateFormat"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%!
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.dd");
%><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
ForumUserBean forumUser = action.getForumUser();
int forumId = action.getParameterInt("forumId");
ForumBean forum = ForumCacheUtil.getForumCache(forumId);
if(forum==null){
	response.sendRedirect("index.jsp");
	return;
}
PagingBean paging = new PagingBean(action, forum.getPrimeCount(), 20, "p");
List contentList = SqlUtil.getIntList("select id from jc_forum_prime where forum_id="+forumId+" and cat_id=0 order by id limit "+paging.getStartIndex()+",20",2);

Object[] adminObj = (Object[])session.getAttribute("primeAdmin"+forum.getId());
HashSet adminSelect = null;
if(adminObj!=null) {	// 管理精华区
	adminSelect = (HashSet)adminObj[0];
	int act = action.getParameterInt("a");
	switch(act){
	case 1: {	// 选中目录
		int select = action.getParameterInt("s");
		adminSelect.add(new Integer(-select));
	} break;
	case 2: {	// 取消选中目录
		int select = action.getParameterInt("s");
		adminSelect.remove(new Integer(-select));
	} break;
	case 3: {	// 选中帖子
		int select = action.getParameterInt("s");
		adminSelect.add(new Integer(select));
	} break;
	case 4: {	// 取消选中帖子
		int select = action.getParameterInt("s");
		adminSelect.remove(new Integer(select));
	} break;
	case 5: {	// 取消所有选中
		adminSelect.clear();
	} break;
	case 6: {	// 全选本页
		for(int i=0;i<contentList.size();i++){
			adminSelect.add(contentList.get(i));
		}
	} break;
	case 7: {	// 清空选择
		adminSelect.clear();
	} break;
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="forum.jsp?forumId=<%=forum.getId()%>"><%=StringUtil.toWml(forum.getTitle())%></a>&gt;精华区<br/>
<%				
for (int i = 0; i < contentList.size(); i++) {
	int content = ((Integer) contentList.get(i)).intValue();
	ForumContentBean cons = ForumCacheUtil.getForumContent(content);
	if (cons != null) {
		%><%if(!adminSelect.contains(new Integer(cons.getId()))){%><a href="primel.jsp?forumId=<%=forumId%>&amp;p=<%=paging.getCurrentPageIndex()%>&amp;a=3&amp;s=<%=cons.getId()%>">选</a><%}else{%><a href="primel.jsp?forumId=<%=forumId%>&amp;p=<%=paging.getCurrentPageIndex()%>&amp;a=4&amp;s=<%=cons.getId()%>">消</a><%}%>
<%=i+1%>.<a href="primev.jsp?id=<%=content%>"><%=StringUtil.toWml(StringUtil.limitString(cons.getTitle(),30))%></a>(<%=sdf.format(cons.getCreateTime())%>)<br/><%
	}
}
%>

<%=paging.shuzifenye("primel.jsp?forumId="+forumId, true, "|", response)%><br/>
<a href="primel.jsp?forumId=<%=forumId%>&amp;a=6&amp;p=<%=paging.getCurrentPageIndex()%>">全选本页</a>|<a href="primel.jsp?forumId=<%=forumId%>&amp;a=7&amp;p=<%=paging.getCurrentPageIndex()%>">清空选择</a><br/>
<a href="prime.jsp?cat=<%=forum.getPrimeCat()%>">查看分类精华区</a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>