<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumxAction action=new ForumxAction(request);
UserBean loginUser = action.getLoginUser();
int id = action.getParameterInt("id");
ForumContentBean con = ForumCacheUtil.getForumContent(id);
if(con==null){
	response.sendRedirect("list.jsp");
	return;
}
String tip = "";
int end = 0;
int start = 0;
PagingBean paging = null;
NewsTypeBean type = null;
	int typeId = action.getParameterInt("tid");
	type = action.getNewsType(typeId);
	if (type == null){
		response.sendRedirect("list.jsp");
		return;
	}


List replyList = new ArrayList();
boolean showFaceImg = true;
if(loginUser!=null) {
	action.addReply(con);
	if(loginUser.getUserSetting()!=null){
		showFaceImg = !loginUser.getUserSetting().isFlagHideFace();
	}
}

	paging = new PagingBean(action, con.getReply(), 10, "p");
	end = paging.getEndIndex();
	start = paging.getStartIndex();
	replyList = ForumCacheUtil.getReplys(con, start, end - start, true);
	end = con.getReply()-paging.getStartIndex();

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="新闻评论">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><a href="news.jsp?id=<%=id%>&amp;tid=<%=type.getId()%>"><%=StringUtil.toWml(con.getTitle())%></a><br/>
<%for(int i=0;i<replyList.size();i++){
Integer iid = (Integer)replyList.get(i);
ForumReplyBean reply = ForumCacheUtil.getForumReply(iid.intValue());
%><%=end-i%>.<%if(reply.getCType()==1){%><%if(showFaceImg){%><img src="../img/jcforum/action/<%=reply.getContent()%>.gif" alt=""/><%}else{%>表情(<%=ForumAction.getFaceString(reply.getContent())%>)<%}%>
<%}else{%><%=ForumAction.speContent(response,StringUtil.toWml(reply.getContent()))%><%}%>(<%=DateUtil.sformatTime(reply.getCreateTime())%>)
<%UserBean user=(UserBean)UserInfoUtil.getUser(reply.getUserId());
   if(user!=null){
if(user.getId()==100){%>系统信息
    <%}else if(true){%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>
     <%}else{%>
<a href="/chat/post.jsp?roomId=0&amp;toUserId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>
<%}  }else{%>
游客<%}%><br/>
<%}%>
<%=paging.shuzifenye("reply.jsp?id="+id+"&amp;tid="+type.getId(), true, "|", response)%>
<input name="content"  maxlength="1000" value=""/>
<anchor title="确定">回复
<go href="reply.jsp?id=<%=con.getId()%>&amp;tid=<%=type.getId()%>" method="post">
    <postfield name="content" value="$content"/>
</go></anchor><br/>
<a href="news.jsp?id=<%=id%>&amp;tid=<%=type.getId()%>">返回</a><br/>
<%if(type==null){%>
<a href="index.jsp">返回新闻首页</a><br/>
<%}else{%>
<a href="list.jsp">返回<%=type.getName()%></a><br/>
<%}	
} else {
%><%=tip%><br/><a href="news.jsp?id=<%=id%>&amp;tid=<%=type.getId()%>">返回新闻</a><br/><%	
}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>