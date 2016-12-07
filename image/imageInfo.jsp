<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector,java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.image.ImageFileBean"%><%@ page import="net.joycool.wap.bean.image.ImageBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.guestbook.CommentAction"%><%@ page import="net.joycool.wap.action.cart.CartAction"%><%
response.setHeader("Cache-Control","no-cache");
String prefixUrl = (String)request.getAttribute("prefixUrl");
ImageBean image = (ImageBean)request.getAttribute("image");
if(image == null){
response.sendRedirect("/Column.do?columnId=4381");
return;
}
ImageBean nextImage = (ImageBean)request.getAttribute("nextImage");
ImageBean prevImage = (ImageBean)request.getAttribute("prevImage");
//String backTo = (String)request.getAttribute("backTo");
String rootBackTo = (String)request.getAttribute("rootBackTo");
ImageFileBean image7070 = image.getFile7070();
ImageFileBean image128128 = image.getFile128128();
String address128 = image128128.getRealFileUrl();
int size128 = image128128.getSize()/1024;
String address70 = image7070.getRealFileUrl();
int size70 = image7070.getSize()/1024;
String code128 = image128128.getCode();
CatalogBean catalog = (CatalogBean)request.getAttribute("catalog");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=image.getName()%>">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%=image.getName()%><br/>
    <img src ="<%=address70%>" alt="loading....."/><br/>
    <a href="ImageDownLoad.do?code=<%=code128 %>">点击下载</a><br/>人气：<%=image.getHits()%><br/>
    <br/>
<%
if(request.getAttribute("wapType") == null){
%>
<a href="/cart/add.jsp?title=<%=URLEncoder.encode("图片:"+image.getName(),"utf8")%>&amp;url=<%=URLEncoder.encode("/image/ImageInfo.do?imageId="+image.getId(),"utf8")%>">&gt;&gt;加入收藏</a><br/>
<%=CommentAction.getCommentEntry(3, image.getId(), "相关评论", PageUtil.getBackTo(request), response)%>
<br/>
<%
}
%>
    <%
//如果下一条图片不为空，显示下一条
if(nextImage != null){
%>
    <a href="<%=nextImage.getLinkUrl()%>" title="进入">下一条：<%=nextImage.getName()%></a><br/>
<%
}
//如果上一条图片不为空，显示上一条
if(prevImage != null){
%>
    <a href="<%=prevImage.getLinkUrl()%>" title="进入">上一条：<%=prevImage.getName()%></a><br/>
<%
}
if(catalog!=null){
%>   

<a href="ImageCataList.do?id=<%=catalog.getId()%>">返回<%=catalog.getName()%></a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>