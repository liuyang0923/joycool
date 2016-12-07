<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.video.*"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
Vector videoFile = (Vector)request.getAttribute("videoFile");
String catalogId=(String)request.getAttribute("catalogId");
String backTo=(String)request.getParameter("backTo");
String catalogname=(String)request.getAttribute("catalogname");
String downAddress="";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="视频下载">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if((videoFile == null)||(videoFile.size()<1)){%>
该视频暂时无法下载，请稍后再试！
<%}else{
    VideoFileBean currentVideoFile;
    for(int i = 0;i <videoFile.size();i++)
        { currentVideoFile = (VideoFileBean)videoFile.get(i);
          downAddress = currentVideoFile.getFile();
		  %>
    <%=i+1%>:
	<%=currentVideoFile.getFileType()%>格式下载：<br/>
	<a href="http://wap.joycool.net/joycool-rep/video/<%=downAddress%>">点击下载</a><br/>
	<%}%>
	<%}%>
    <br/>
    <a href="VideoCataList.do?id=<%=catalogId%>" title="返回">返回<%=catalogname%></a><br/>
    <%if(backTo != null){%>
    <a href="<%=backTo.replace("&","&amp;")%>" title="进入">返回上一级</a><br/>
    <%}%>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>