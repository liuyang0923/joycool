<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.friendlink.RandomLinkBean"%><%@ page import="java.net.URLEncoder"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.ebook.FileNameBean"%><%@ page import="net.joycool.wap.bean.ebook.EBookBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.action.wgame.WGameAction"%><%
    Vector files = (Vector)request.getAttribute("fileNames");
    String rootId = (String)request.getAttribute("rootId");
    CatalogBean catalog = (CatalogBean)request.getAttribute("catalog");
    EBookBean ebook = (EBookBean)request.getAttribute("ebook");
    String result =(String)request.getAttribute("result");
    String url = ("/Column.do?columnId=8774");
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=request.getAttribute("tip") %><br/>
<a href="/Column.do?columnId=8774">返回书城</a><br/>
</p>
</card>
<%}else{%>
<card title="<%=StringUtil.toWml(ebook.getName())%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
下载电子书需要等级3<br/>
<%
if(loginUser == null){
%>
您未登录，请先<a href="/user/login.jsp?backTo=<%=PageUtil.getBackTo(request)%>">登录</a><br/>
<%
} else {
	//WGameAction action = new WGameAction();
	UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
	//Liq 2007.3.26下载电子书需要等级3
%>
您目前的等级是：<%=us.getRank()%>级<br/>
<a href="/lswjs/index.jsp">进入赌城赚乐币</a><br/>
<%
}

if((files == null)||(files.size()<1)){%>
    目前没有提供该电子书的相关下载！<br/>
    <%}else
    {  %>
<%
     FileNameBean currentFile;
    for(int i = 0;i <files.size();i++)
        { currentFile = (FileNameBean)files.get(i);
          String address = currentFile.getAddress();
          int id = currentFile.getId();
          String chapters = currentFile.getChapters();
          String downloadLink = ("ebookDownload.jsp?address=" + URLEncoder.encode(address));
        %> 
    <%=id%>.
    <a href="<%=downloadLink%>"><%=StringUtil.toWml(chapters)%></a><br/>
    <%}%>
    <%}%>
    
    <br/>	
    <%if((rootId != null)&&(catalog != null)){%>
	<a href="EBookReadList.do?ebookId=<%=ebook.getId()%>&amp;rootId=<%=rootId%>">点击阅读</a><br/>
    <a href="EBookInfo.do?ebookId=<%=ebook.getId()%>&amp;pageIndex=0&amp;orderBy=id" title="返回">返回介绍</a><br/>
    <a href="EBookCataList.do?id=<%=catalog.getId()%>" title="返回">返回<%=catalog.getName()%></a><br/>
    <%}%>
	<%=BaseAction.getBottom(request, response)%><br/>
<%
//<!--add friend link start-->
RandomLinkBean randomLinkBean = new RandomLinkBean(); %>
<img src="<%=( randomLinkBean.getRandomLink(request) ) %>" alt="loading..." />
<%//<!--add friend link end--> %>
</p>
</card>
<%}%>
</wml>
