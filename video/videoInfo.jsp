<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.guestbook.CommentAction"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.video.*"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.cart.CartAction" %><%
response.setHeader("Cache-Control","no-cache");
String prefixUrl = (String)request.getAttribute("prefixUrl");
VideoBean video = (VideoBean)request.getAttribute("video");
VideoFileBean videoFile = (VideoFileBean) request.getAttribute("videoFile");
VideoBean nextVideo = (VideoBean)request.getAttribute("nextVideo");
VideoBean prevVideo = (VideoBean)request.getAttribute("prevVideo");
CatalogBean catalog = (CatalogBean)request.getAttribute("catalog");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=video.getName()%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=video.getName()%><br/>
视频介绍：<%=video.getIntroduction()%><br/>
<%if(videoFile==null){%>
该视频暂时无法下载，请稍后再试！<br/><br/>
<%}else{%>
大小:<%=videoFile.getSize()%>K<br/>
格式：<%=videoFile.getFileType()%><br/>
<a href="http://wap.joycool.net/joycool-rep/video/<%=videoFile.getFile()%>">点击下载</a><br/><br/>
<%}%>
<%=CartAction.getAddEntry("视频：" + video.getName().replace("\"", ""), StringUtil.toWml(PageUtil.getCurrentPageURL(request)), request, response)%>
<%=CommentAction.getCommentEntry(4, video.getId(), "相关评论", PageUtil.getBackTo(request), response)%><br/>
<%//如果下一个游戏不为空，显示下一条
if(nextVideo != null){
%>
    <a href="<%=nextVideo.getLinkUrl()%>" title="进入">下一条：<%=nextVideo.getName()%></a><br/>
<%
}
//如果上一个游戏不为空，显示上一条
if(prevVideo != null){
%>
    <a href="<%=prevVideo.getLinkUrl()%>" title="进入">上一条：<%=prevVideo.getName()%></a><br/>
<%
}
%>   
    <a href="VideoCataList.do?id=<%=catalog.getId()%>" title="返回">返回<%=catalog.getName()%></a><br/>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>