<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UrlMapBean"%><%
response.setHeader("Cache-Control","no-cache");
int totalPage = ((Integer)request.getAttribute("totalPageCount")).intValue();
int currentPage = ((Integer)request.getAttribute("currentPage")).intValue();
String prefixUrl = (String)request.getAttribute("prefixUrl");
String name = (String)request.getAttribute("name");
Vector news = (Vector)request.getAttribute("newsList");
String backTo = (String)request.getAttribute("backTo");
String rootBackTo = (String)request.getAttribute("rootBackTo");
String rootId = (String)request.getAttribute("rootId");
String jaLineId = (String)request.getAttribute("jaLineId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=name%>">

<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%if((news == null)||(news.size()<1)){%>
    该类别目前没有新闻！<br/>
    <%}else
    {  %>
    <%=name%><br/>
<img src="../img/newsGroup.gif" alt="圈里圈外"/><br/>
    -------------<br/><%
    CatalogBean currentNew;
    for(int i = 0;i <news.size();i++)
        { currentNew = (CatalogBean)news.get(i);
          String title = currentNew.getName();
          String description = currentNew.getDescription();
          int id = currentNew.getId();
        %>
    <a href="http://wap.joycool.net/news/GroupCataList.do?id=<%=currentNew.getId()%>&amp;orderBy=id&amp;endlist=if"><%=StringUtil.toWml(title)%></a><br/>
    <%=description%> <br/>
    <%}%>
    <%=PageUtil.shuzifenye(totalPage, currentPage, prefixUrl, true, " ", response)%> 
    <br/>
第<%=(currentPage + 1)%>页  共<%=totalPage%>页
    <%}%>
    <br/>
 <%--   <a href="NewsCataList.do?id=<%=rootId%>" title="返回">返回新闻首页</a><br/>--%>
<a href="http://wap.joycool.net/Column.do?columnId=3840" title="返回">返回是非趣闻</a><br/>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>