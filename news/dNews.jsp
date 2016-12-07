<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%!
	static String[] newsType={"新闻","八卦","专题"};
%><%
response.setHeader("Cache-Control","no-cache");
ForumxAction action=new ForumxAction(request);
UserBean loginUser = action.getLoginUser();
if(!ForbidUtil.isForbid("newsa",loginUser.getId())){
	response.sendRedirect("list.jsp");
	return;
}
int id = action.getParameterInt("id");
ForumContentBean con = ForumCacheUtil.getForumContent(id);
if(con == null||!con.isNews()) {
	response.sendRedirect("list.jsp");
	return;
}
// int type=SqlUtil.getIntResult("select type from forum_news where id="+id,2);
int typeId = action.getParameterInt("tid");
int type = SqlUtil.getIntResult("select type from forum_news_type where id="+typeId,2);

if(type<0||!ForbidUtil.isForbid("newsa2",loginUser.getId())){
	response.sendRedirect("list.jsp");
	return;
}
boolean confirm = (request.getParameter("c")!=null);
if(confirm){
	action.delNews(con, typeId);
	response.sendRedirect("list.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="删除新闻">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(con.getTitle())%><br/>
[<%=DateUtil.formatDate2(con.getCreateTime())%>]<br/>
<% if (type == 0){
		%><a href="dNews.jsp?c=1&amp;id=<%=id%>&amp;tid=<%=typeId%>">确认删除本条<%=newsType[type]%></a><br/><a href="index.jsp">返回新闻</a><br/><%
} else {
		%><a href="dNews.jsp?c=1&amp;id=<%=id%>&amp;tid=<%=typeId%>">确认删除本条<%=newsType[type]%></a><br/><a href="index2.jsp">返回八卦</a><br/><%
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>