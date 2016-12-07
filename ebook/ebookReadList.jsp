<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.bean.friendlink.RandomLinkBean"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.ebook.FileNameBean"%><%@ page import="net.joycool.wap.bean.ebook.EBookBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
    Vector files = (Vector)request.getAttribute("fileNames");
    String rootId = (String)request.getAttribute("rootId");
    EBookBean ebook = (EBookBean)request.getAttribute("ebook");
    CatalogBean catalog = (CatalogBean)request.getAttribute("catalog");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(ebook.getName())%>">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%if((files == null)||(files.size()<1)){%>
    目前没有提供该电子书的相关资料！<br/>
    <%}else
    {  %>
<%
     FileNameBean currentFile;
    for(int i = 0;i <files.size();i++)
        { currentFile = (FileNameBean)files.get(i);
          String address = currentFile.getAddress();
          int id = currentFile.getId();
          String chapters = currentFile.getChapters();
          String downloadLink = "download.jsp?address=" + address;
          String readLink = ("EBookRead.do?address=" + address + "&amp;ebookId=" + ebook.getId() + "&amp;rootId=" + rootId);
        %>
    <%=id%>.<a href="<%=readLink%>"><%=StringUtil.toWml(chapters)%></a><br/>
    <%}%>
    <%}%>
    <br/>
        <%if((rootId != null)&&(catalog != null)){%>
<!--
	<a href="EBookDownloadList.do?ebookId=<%=ebook.getId()%>&amp;rootId=<%=rootId%>">点击下载</a><br/>
-->
    <a href="EBookInfo.do?ebookId=<%=ebook.getId()%>&amp;pageIndex=0&amp;orderBy=id" title="返回">返回介绍</a><br/>
    <a href="EBookCataList.do?id=<%=catalog.getId()%>" title="返回">返回<%=catalog.getName()%></a><br/>    
    <%}%>
	<%=BaseAction.getBottom(request, response)%><br/>
</p>
</card>
</wml>
