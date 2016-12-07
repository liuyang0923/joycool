<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.action.*,net.joycool.wap.bean.*,java.util.Vector"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.news.NewsBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.guestbook.CommentAction"%><%@ page import="net.joycool.wap.action.cart.CartAction"%><%
NewsBean news = (NewsBean) request.getAttribute("news");
NewsBean prevNews = (NewsBean) request.getAttribute("prevNews");
NewsBean nextNews = (NewsBean) request.getAttribute("nextNews");
String attachCode = (String) request.getAttribute("attachCode");

int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");

CatalogBean catalog = (CatalogBean)request.getAttribute("catalog");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(news.getTitle())%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(news.getTitle())%><br/>
------------------<br/>
<!--
发布时间：<br/><%=news.getReleaseDate()%><br/>
人气：<%=news.getHits()%><br/>
-->
<%=attachCode%>
内容：<br/>
<%=StringUtil.toWml(StringUtil.cutString(news.getContent().substring(pageIndex * Constants.NEWS_WORD_PER_PAGE), Constants.NEWS_WORD_PER_PAGE))%><br/>
<%=PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, " ", response)%>
<br/>
第<%=(pageIndex + 1)%>页  共<%=totalPageCount%>页
<br/>
<% 
String displayIds = ",125,123,126,127,278,279,281,283,284,285,291,293,294,373,374,318,355,366,357,358,359,361,369,298,299,300,301,302,303,735,733,";
int catalogId = catalog.getId();
if(displayIds.indexOf("," + catalogId + ",")!=-1){
    LinkBean bean = LinkAction.getRandomLink(2); 
    if(bean!=null){%>
    <a href="<%= (bean.getUrl().replace("&", "&amp;")) %>"><%= StringUtil.toWml(bean.getDesc()) %></a><br/>
    <%}
}
if(request.getAttribute("wapType") == null){
%>
<%=CartAction.getAddEntry("文章：" + news.getTitle().replace("\"", "").replace("<", "").replace(">", ""), StringUtil.toWml(PageUtil.getCurrentPageURL(request)), request, response)%>
<%=CommentAction.getCommentEntry(1, news.getId(), "相关评论", PageUtil.getBackTo(request), response)%>
<br/>
<%
}
%>
<%
//如果下一条新闻不为空，显示下一条
if(nextNews != null){
%>
    <a href="<%=nextNews.getLinkUrl()%>" title="进入">下一条：<%=StringUtil.convertString(nextNews.getTitle())%></a><br/>
<%
}
//如果上一条新闻不为空，显示上一条
if(prevNews != null){
%>
    <a href="<%=prevNews.getLinkUrl()%>" title="进入">上一条：<%=StringUtil.convertString(prevNews.getTitle())%></a><br/>
<%
}
%>   
    <a href="GroupCataList.do?id=<%=catalog.getId()%>&amp;endlist=if" title="返回上一级">返回<%=catalog.getName()%></a><br/>

<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>