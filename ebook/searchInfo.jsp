<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.friendlink.RandomLinkBean"%><%@ page import="net.joycool.wap.bean.ebook.EBookBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");;
EBookBean ebook = (EBookBean)request.getAttribute("ebook");
CatalogBean catalog = (CatalogBean)request.getAttribute("catalog");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(ebook.getName())%>">
<p align="left">
<%=BaseAction.getTop(request, response)%><br/>
<%=StringUtil.toWml(ebook.getName())%><br/>
------------------<br/>
作者：<%=ebook.getAuthor()%><br/>
下载次数：<%=ebook.getDownloadSum()%><br/>
内容简介：<%=ebook.getDescription()%><br/>
<a href="EBookDownloadList.do?ebookId=<%=ebook.getId()%>">点击下载</a><br/>
<a href="EBookReadList.do?ebookId=<%=ebook.getId()%>">点击阅读</a><br/>
    <br/>
	<%=BaseAction.getBottom(request, response)%><br/>
<%
//<!--add friend link start-->
RandomLinkBean randomLinkBean = new RandomLinkBean(); %>
<img src="<%=( randomLinkBean.getRandomLink(request) ) %>" alt="loading..." />
<%//<!--add friend link end--> %>
</p>
</card>
</wml>