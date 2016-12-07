<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.bean.friendlink.RandomLinkBean"%><%@ page import="java.util.Vector,java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.ebook.EBookBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.guestbook.CommentAction"%><%@ page import="net.joycool.wap.action.cart.CartAction"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%
EBookBean ebook = (EBookBean) request.getAttribute("ebook");
if(ebook == null){
response.sendRedirect(("/lswjs/index.jsp"));
return;
}
EBookBean prevEBook = (EBookBean) request.getAttribute("prevEBook");
EBookBean nextEBook = (EBookBean) request.getAttribute("nextEBook");
CatalogBean catalog = (CatalogBean)request.getAttribute("catalog");
UserBean loginUser=(UserBean)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
String ifdown = (String)request.getAttribute("ifdown");
String rootId = (String)request.getAttribute("rootId");
String backTo = (String)request.getAttribute("backTo");
int temp = StringUtil.toInt((String)request.getAttribute("temp"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(ebook.getName())%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(ebook.getName())%><br/>
作者：<%=ebook.getAuthor()%><br/>
下载次数：<%=ebook.getDownloadSum()%><br/>
内容简介：<%=ebook.getDescription()%><br/>
<a href="/cart/add.jsp?title=<%=URLEncoder.encode("电子书:"+ebook.getName(),"utf8")%>&amp;url=<%=URLEncoder.encode("/ebook/EBookInfo.do?ebookId="+ebook.getId(),"utf8")%>">&gt;&gt;加入收藏</a><br/>
<a href="EBookReadList.do?ebookId=<%=ebook.getId()%>&amp;rootId=<%=rootId%>">直接在线阅读</a><br/>
<%--
<%
//Liq 2007.3.26  新书1周之后才提供下载选项
if(ifdown != null) {%>
<a href="EBookDownloadList.do?ebookId=<%=ebook.getId()%>&amp;rootId=<%=rootId%>">下载到手机阅读</a><br/>
<%}
//Liq 2007.3.26  新书1周之后才提供下载选项
%>

--%>
<%--<%=CommentAction.getCommentEntry(4, ebook.getId(), "相关评论", PageUtil.getBackTo(request), response)%><br/>--%>
<a href="EBookDownloadList.do?ebookId=<%=ebook.getId()%>&amp;rootId=<%=rootId%>">下载到手机阅读</a><br/>

<%if(nextEBook != null){
%>
    <a href="<%=nextEBook.getLinkUrl()%>" title="进入">下一条：<%=StringUtil.toWml(nextEBook.getName())%></a><br/>
<%
}
//如果上一本电子书不为空，显示上一条
if(prevEBook != null){
%>
    <a href="<%=prevEBook.getLinkUrl()%>" title="进入">上一条：<%=StringUtil.toWml(prevEBook.getName())%></a><br/>
<%
}
%>
<% if(catalog!=null){
%>
    <a href="EBookCataList.do?id=<%=catalog.getId()%>" title="返回">返回<%=catalog.getName()%></a><br/>   
    <%}%>
	<%=BaseAction.getBottom(request, response)%><br/>
</p>
</card>
</wml>