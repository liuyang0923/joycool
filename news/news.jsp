<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="java.util.Vector,java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.db.DbOperation" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumxAction action=new ForumxAction(request);
UserBean loginUser = action.getLoginUser();
int id = action.getParameterInt("id");
int typeId = action.getParameterInt("tid");
ForumContentBean con = ForumCacheUtil.getForumContent(id);
if(con==null){
	response.sendRedirect("list.jsp");
	return;
}

boolean showFaceImg = true;
if(loginUser!=null&&loginUser.getUserSetting()!=null) {
	showFaceImg = !loginUser.getUserSetting().isFlagHideFace();
}

DbOperation db = new DbOperation(2);
if (typeId <= 0){
	typeId =db.getIntResult("select type from forum_news where id="+id); 
}
if(typeId<=0){
	db.release();
	response.sendRedirect("index.jsp");
	return;
}
int prev = db.getIntResult("select id from forum_news where type="+typeId+" and id>"+id+" order by id limit 1");
int next = db.getIntResult("select id from forum_news where type="+typeId+" and id<"+id+" order by id desc limit 1");
db.release();
NewsTypeBean type = action.getNewsType(typeId);
if (type == null){
	response.sendRedirect("index.jsp");
	return;
}
session.setAttribute("ntId",Integer.valueOf(type.getId()));

int view = Math.min(3, con.getReply());
List replyList = ForumCacheUtil.getReplys(con, 0, view, true);
//boolean newsAdmin = false;
boolean news2Admin = false;
if(loginUser!=null) {
//	newsAdmin = ForbidUtil.isForbid("newsa",loginUser.getId());
	news2Admin = ForbidUtil.isForbid("newsa2",loginUser.getId());
}
UserBean creator = UserInfoUtil.getUser(con.getUserId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=type==null?"新闻":type.getName()%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(con.getTitle())%><br/>
<%if(creator!=null){%>乐酷记者<a href="/user/ViewUserInfo.do?userId=<%=con.getUserId()%>" ><%=creator.getNickNameWml()%></a><%}%>[<%=DateUtil.sformatTime(con.getCreateTime())%>]
<%if(news2Admin){%><a href="dNews.jsp?id=<%=id%>&amp;tid=<%=typeId%>">删除</a><%}%><%if(news2Admin){%>|<a href="/jcforum/editTitleX.jsp?contentId=<%=id%>">编辑</a>|<%}%><a href="/cart/add.jsp?title=<%=URLEncoder.encode("新闻:"+con.getTitle(),"utf8")%>&amp;url=<%=URLEncoder.encode("/news/news.jsp?id="+con.getId()+"&tid="+typeId,"utf8")%>">收藏</a><br/>
<%if(con.getAttach()!=null & !("").equals(con.getAttach())){%>
<a href ="<%=Constants.IMG_ROOT_URL%><%=con.getAttach()%>"><img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%><%=con.getAttach()%>" alt="o"/></a><br/>
<%}%>
<%=ForumAction.speContent(response,StringUtil.toWml(con.getContent()))%><br/>
<input name="content"  maxlength="1000" />
<anchor title="确定">回复
<go href="reply.jsp?id=<%=con.getId()%>&amp;tid=<%=typeId%>" method="post">
    <postfield name="content" value="$content"/>
</go></anchor><br/>
<%UserBean user = null;
for(int i=0;i<replyList.size();i++){
Integer iid = (Integer)replyList.get(i);
ForumReplyBean reply = ForumCacheUtil.getForumReply(iid.intValue());
user = UserInfoUtil.getUser(reply.getUserId());
%><%=con.getReply()-i%>.<%if(reply.getCType()==1){%><%if(showFaceImg){%><img src="../img/jcforum/action/<%=reply.getContent()%>.gif" alt=""/><%}else{%>表情(<%=ForumAction.getFaceString(reply.getContent())%>)<%}%>(<%=reply.getCreateDatetime() %>)<%if(user!=null){%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>" ><%=user.getNickNameWml()%></a><%}%>
<%}else{%><%=StringUtil.toWml(StringUtil.limitString(reply.getContent(),20))%>(<%=reply.getCreateDatetime() %>)<%if(user!=null){%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>" ><%=user.getNickNameWml()%></a><%}%><%}%><br/>
<%}%>
<a href="reply.jsp?id=<%=con.getId()%>&amp;tid=<%=typeId%>">查看更多评论(<%=con.getReply()%>)</a><br/>
<%if(next!=-1){%><a href="news.jsp?id=<%=next %>&amp;tid=<%=typeId%>">下一条</a><%}else{%>下一条<%}%>|<%if(prev!=-1){%><a href="news.jsp?id=<%=prev %>&amp;tid=<%=typeId%>">上一条</a><%}else{%>上一条<%}%><br/>
<% if (type != null && type.getType() == 1){
	// 显示5条最新新闻
	List list = ForumxAction.getRandomLatestNews(type.getId(),5);
	if (list != null && list.size() > 0){
		%>新:<%
   		for (int i = 0;i<list.size();i++){
   			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
   			if (con != null){
				%><a href="news.jsp?id=<%=list.get(i)%>&amp;tid=<%=typeId%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%></a><br/><%   		
   			}
   		}
   }
}%>
<%if(type==null){%>
<a href="index.jsp">返回新闻首页</a><br/>
<%}else if(type.getColumnId()!=0){%>
<a href="/Column.do?columnId=<%=type.getColumnId()%>"><%=type.getColumnName()%></a>&gt;<a href="list.jsp?id=<%=type.getId()%>"><%=type.getName()%></a><br/>
<%}else{%>
<a href="list.jsp?id=<%=type.getId()%>">&gt;返回<%=type.getName()%></a><br/>
<% if (type.getType() == 1){
	%><a href="index2.jsp">>返回八卦首页</a><br/><%
}%>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>