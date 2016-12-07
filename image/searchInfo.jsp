<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.image.ImageFileBean"%><%@ page import="net.joycool.wap.bean.image.ImageBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");;
ImageBean image = (ImageBean)request.getAttribute("image");
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
<card title="图片搜索">
<p align="left"> 
<%--马长青_2006-6-22_增加顶部信息_start--%>
<%=BaseAction.getTop(request, response)%><br/>
<%--马长青_2006-6-22_增加顶部信息_end--%>
    <%=image.getName()%><br/>
    <img src ="<%=address70%>" alt="loading....."/><br/>
    <a href="ImageDownLoad.do?code=<%=code128 %>">点击下载</a><br/>
    <br/> 
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>