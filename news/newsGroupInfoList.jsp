<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.news.NewsBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UrlMapBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%
response.setHeader("Cache-Control","no-cache");
//int totalPage = ((Integer)request.getAttribute("totalPageCount")).intValue();
//int currentPage = ((Integer)request.getAttribute("currentPage")).intValue();
//String prefixUrl = (String)request.getAttribute("prefixUrl");
String name = (String)request.getAttribute("name");
Vector news = (Vector)request.getAttribute("newsList");
String backTo = (String)request.getAttribute("backTo");
String rootBackTo = (String)request.getAttribute("rootBackTo");
String rootId = (String)request.getAttribute("rootId");
String jaLineId = (String)request.getAttribute("jaLineId");
String picture = (String) request.getAttribute("picture");
CatalogBean preGroup = (CatalogBean)request.getAttribute("preGroup");
CatalogBean nexGroup = (CatalogBean)request.getAttribute("nexGroup");
CatalogBean nowGroup = (CatalogBean)request.getAttribute("nowGroup");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="圈里圈外">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%=nowGroup.getName()%><br/>
        <%=picture%>
            <%if (nowGroup != null) {%>
              <%=nowGroup.getDescription()%>
            <%}%>
   <br/>
    <%if((news == null)||(news.size()<1)){%>
    该类别目前没有新闻！<br/>
    <%}else
    {  %>
     <%
     NewsBean currentNew;
    for(int i = 0;i <news.size();i++)
        { currentNew = (NewsBean)news.get(i);
          String title = currentNew.getTitle();
          int id = currentNew.getId();
        %>
    <a href="http://wap.joycool.net/news/NewsGroupInfo.do?newsId=<%=id%>&amp;orderBy=id"><%=StringUtil.toWml(title)%></a><br/>
    <%}%>
							    <%-- <%=PageUtil.shuzifenye(totalPage, currentPage, prefixUrl, true, " ", response)%>
							     <br/>
							    第<%=(currentPage + 1)%>页  共<%=totalPage%>页 --%>
    <%}%>
    <%if (preGroup != null) {%>
    下一专题:<a href="<%=("http://wap.joycool.net/news/GroupCataList.do?id="+preGroup.getId()+"&amp;jaLineId="+jaLineId+"&amp;orderBy=id&amp;endlist=if")%>"><%=StringUtil.toWml(preGroup.getName())%></a><br/>
      <%}%>
    <%if (nexGroup != null) {%>
    上一专题:<a href="<%=("http://wap.joycool.net/news/GroupCataList.do?id="+nexGroup.getId()+"&amp;jaLineId="+jaLineId+"&amp;orderBy=id&amp;endlist=if")%>"><%=StringUtil.toWml(nexGroup.getName())%></a><br/>
      <%}%>
<a href="<%=backTo%>">返回八卦专题</a><br/>
<a href="http://wap.joycool.net/Column.do?columnId=3840" title="返回">返回是非趣闻</a><br/>

<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>