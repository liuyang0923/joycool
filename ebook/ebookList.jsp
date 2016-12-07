<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.*,net.joycool.wap.bean.friendlink.RandomLinkBean"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.ebook.EBookBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.JoycoolSpecialUtil" %><%
response.setHeader("Cache-Control","no-cache");
int totalPage = ((Integer)request.getAttribute("totalPageCount")).intValue();
int currentPage = ((Integer)request.getAttribute("currentPage")).intValue();
String prefixUrl = (String)request.getAttribute("prefixUrl");
String name = (String)request.getAttribute("name");
Vector ebooks = (Vector)request.getAttribute("ebooksList");
String orderBy = (String)request.getAttribute("orderBy");
String backTo = (String)request.getAttribute("backTo");
String rootId = (String)request.getAttribute("rootId");
String id = (String)request.getAttribute("id");
CatalogBean catalog = (CatalogBean) request.getAttribute("catalog");
//zhul_2006-08-08 modify backTo start
UrlMapBean urlBean=(UrlMapBean)URLMap.getURLCatalogMap().get(new Integer(StringUtil.toInt(id)));
//zhul_2006-08-08 modify backTo end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=name%>">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%if((ebooks == null)||(ebooks.size()<1)){%>
    该类别目前没有电子书！<br/>
    <%}else
    {  %>
    <%=name%><br/>
    <%if(orderBy.equals("download_sum")){%>
<a href="EBookCataList.do?id=<%=id %>&amp;orderBy=id">按时间</a>|按人气<br/>
    <%}else{%>
按时间|<a href="EBookCataList.do?id=<%=id %>&amp;orderBy=down">按人气</a><br/>
    <%}%>
    -------------<br/><%
     EBookBean currentEBook;
    for(int i = 0;i <ebooks.size();i++)
        { currentEBook = (EBookBean)ebooks.get(i);
          String ebookName = currentEBook.getName();
          int ebookId = currentEBook.getId();
        %>
    <%=(i + 1)%>.<a href="<%=("EBookInfo.do?ebookId="+ebookId+"&amp;pageIndex="+currentPage+"&amp;orderBy=id")%>"><%=StringUtil.toWml(ebookName)%></a><br/>
人气：<%=currentEBook.getDownloadSum()%><br/>
    <%}%>
    <%=PageUtil.shuzifenye(totalPage, currentPage, prefixUrl, true, " ", response)%><br/>
第<%=(currentPage + 1)%>页  共<%=totalPage%>页<br/>
跳到第<input type="text" name="pageIndex1" maxlength="3" value="0"/>页 <anchor title ="go">Go
         <go href="<%=(prefixUrl)%>" method="post">
             <postfield name="pageIndex1" value="$(pageIndex1)"/>
         </go>
         </anchor><br/>
搜索：<input name="ebookName" maxlength="20" value="v"/> <anchor title ="search Ebook">Go
         <go href="SearchEBook.do" method="post">
             <postfield name="ebookName" value="$(ebookName)"/>
         </go>
         </anchor><br/>
    <%}%>
    <%//书城添加论坛个随机聊天室链接start%>
    <a href="<%= ("/jcforum/forum.jsp?forumId=906") %>">+论坛•书虫菜园+</a><br/>
	<%--=JoycoolSpecialUtil.getChatMessage(request,response,0,1)--%>
	<%//书城添加论坛个随机聊天室链接end%>
	<a href="<%=backTo%>" title="进入">返回<%=urlBean!=null?urlBean.getTitle():"上一级"%></a><br/>
	<%=BaseAction.getBottom(request, response)%><br/>
</p>
</card>
</wml>