<%--
if(net.joycool.wap.util.SecurityUtil.isWap20(request)){
request.getRequestDispatcher("editContentX.jsp").forward(request, response);
return;
}
--%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.jcforum.*,net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
int forumId=StringUtil.toInt(request.getParameter("forumId"));
if(forumId<=0){
forumId=1;
}
ForumBean forum = ForumCacheUtil.getForumCache(forumId);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
int count = UserBagCacheUtil.getUserBagItemCount(ForumContentBean.SPARK_ITEM,loginUser.getId());

session.setAttribute("forumcontent","true");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(count > 0) {%>
发表闪耀主题于[<%=StringUtil.toWml(forum.getTitle())%>]<br/>
<select name="sid">
<%for(int i = 1; i < ForumContentBean.vipTypes.length; i++) {
	if(ForumContentBean.vipTypes[i] != null && ForumContentBean.vipTypes[i].length() > 0){%>
<option value="<%=i%>"><%=ForumContentBean.vipTypes[i] %></option>
<%} }%></select>
标题:<input name="title"  maxlength="30" value=""/><br/>
内容:<input name="content"  maxlength="1000" value=""/><br/>
<anchor title="确定">发表闪耀主题
    <go href="forum.jsp?forumId=<%=forumId%>" method="post">
    <postfield name="content" value="$content"/>
    <postfield name="title" value="$title"/>
    <postfield name="sid" value="$sid"/>
    </go>
</anchor><br/>
<%} else { %>
你还没有论坛闪耀卡！！<br/>
<a href="/shop/index.jsp">&gt;&gt;商城购买</a><br/>
<%} %>
<a href="forum.jsp?forumId=<%=forumId%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>