<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.ring.PringBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.guestbook.CommentAction" %><%@ page import="net.joycool.wap.bean.ring.PringFileBean" %><%@ page import="java.util.Vector" %><%!
static long lastTime=0;
static int visitCount=0;
%><%
response.setHeader("Cache-Control","no-cache");
visitCount++;
if(visitCount>5){
	long now=System.currentTimeMillis();
	if(now<lastTime)
		return;
	lastTime=now+20000l;
}

jc.download.RingAction action = new jc.download.RingAction(request);
String prefixUrl = (String)request.getAttribute("prefixUrl");
PringBean ring = (PringBean)request.getAttribute("ring");
Vector ringFileList = (Vector) request.getAttribute("ringFileList");
PringBean nextRing = (PringBean)request.getAttribute("nextRing");
PringBean prevRing = (PringBean)request.getAttribute("prevRing");
CatalogBean catalog = (CatalogBean)request.getAttribute("catalog");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="铃声下载">
<p align="left">
<%if(ring==null){%>
没有找到该铃声!<br/>
<a href="/Column.do?columnId=5188" title="返回">返回铃声首页</a><br/>
<%}else{%>
<%=BaseAction.getTop(request, response)%>
歌曲名称：<%=ring.getName()%><br/>
演唱者：<%=ring.getSinger()%><br/>
<%if(ringFileList.size()==0){%>
该炫铃暂时无法下载，请稍后再试！<br/><br/>
<%}else{
if(ringFileList.size()>1){
%>
<a href="DownloadRing.do?id=<%=ring.getId()%>&amp;catalog_id=<%=ring.getCatalog_id()%>">点击下载</a><br/><br/>
<%}else{
PringFileBean ringFile=(PringFileBean)ringFileList.get(0);
%>
大小:<%=ringFile.getsize()%>K<br/>
格式：<%=ringFile.getFile_type()%><br/>
<a href="<%=action.getRingUrlPath() + ringFile.getFile()%>">点击下载</a><br/><br/>
<%}}%>
<a href="/cart/add.jsp?title=<%=URLEncoder.encode("铃声:"+ring.getName(),"utf8")%>&amp;url=<%=URLEncoder.encode("/ring/RingInfo.do?ringId="+ring.getId(),"utf8")%>">&gt;&gt;加入收藏</a><br/>
<%--原来的backTo：PageUtil.getBackTo(request) --%>
<%=CommentAction.getCommentEntry(4, ring.getId(), "相关评论", "/ring/RingInfo.do?ringId=" + ring.getId(), response)%><br/>
<%
//如果下一个游戏不为空，显示下一条
if(nextRing != null){
%>
<a href="<%=nextRing.getLinkUrl()%>" title="进入">下一条：<%=nextRing.getName()%></a><br/>
<%
}
//如果上一个游戏不为空，显示上一条
if(prevRing != null){
%>
    <a href="<%=prevRing.getLinkUrl()%>" title="进入">上一条：<%=prevRing.getName()%></a><br/>
<%
}
%>   
    <a href="RingCataList.do?id=<%=catalog.getId()%>" title="返回">返回<%=catalog.getName()%></a><br/>
<%}%>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>