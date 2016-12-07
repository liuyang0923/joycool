<%@ page language="java" contentType="text/html;charset=UTF-8"%><%@ page import="com.jspsmart.upload.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.photo.*"%><%@ page import="jc.family.photo.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
FmPhotoAction action = new FmPhotoAction(request,response);
%><%@include file="inc.jsp"%><%
if (!action.hasParam("id")){response.sendRedirect("phocat.jsp");return;}
int aid = action.getParameterInt("id");
FmAlbumBean albumBean = null;
if (aid != 0) {albumBean = FmPhotoAction.service.getAlbumBean("id="+aid+" and fm_id="+fid);}
if (action.hasParam("u")) {if (albumBean == null) {albumBean = new FmAlbumBean();albumBean.setFid(fid);}
action.addPhoto(pageContext,albumBean,loginUser);}
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>上传参赛照片</title>
</head>
<body>
<%=BaseAction.getTop(request, response)%>
增加照片<br/>------------------------<br/>
<% if (request.getAttribute("tip") != null) {%><font color="red"><%=(String)request.getAttribute("tip")%></font><br/><%}%>
<% if (!"success".equals((String)request.getAttribute("result"))){%>
<form enctype="multipart/form-data" method="post" action="<%=response.encodeURL("/fm/pho/addpho.jsp?u=1&amp;id="+aid)%>">
标题(18字内):<br/>&#160;&#160;&#160;&#160;<input name="pn" size="18"/><br/>
图片*:128*128以内<br/>&#160;&#160;&#160;&#160;<input type="file" name="url" /><br/>
<input type="submit" value="添加"/><br/>
注：支持WAP2.0的手机才可以上传图片。
</form>
<%}%>
<a href="cat.jsp?id=<%=aid%>">返回相册</a><br/>
<%=BaseAction.getBottom(request, response)%></body></html>