<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.news.NewsBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UrlMapBean"%><%
response.setHeader("Cache-Control","no-cache");
int totalPage = ((Integer)request.getAttribute("totalPageCount")).intValue();
int currentPage = ((Integer)request.getAttribute("currentPage")).intValue();
String prefixUrl = (String)request.getAttribute("prefixUrl");
String name = (String)request.getAttribute("name");
Vector news = (Vector)request.getAttribute("newsList");
String backTo = (String)request.getAttribute("backTo");
String rootBackTo = (String)request.getAttribute("rootBackTo");
String rootId = (String)request.getAttribute("rootId");
String sid = (String)request.getAttribute("id");
String jaLineId = (String)request.getAttribute("jaLineId");
String parentTitle=null;
if(request.getParameter("id")!=null){
	int catalogId=StringUtil.toInt(request.getParameter("id"));
	UrlMapBean url=(UrlMapBean)URLMap.getURLCatalogMap().get(new Integer(catalogId));
	if(url!=null){
		parentTitle=url.getTitle();
		if(parentTitle==null||parentTitle.equals(""))
			parentTitle="上一级";
	}else{
		parentTitle="上一级";
	}
}else{
	parentTitle="上一级";
}

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
    <a href="<%=("http://wap.joycool.net/news/NewsCataList.do?id="+sid+"&amp;jaLineId="+jaLineId+"&amp;orderBy=id"+"&amp;pageIndex="+totalPage)%>">到尾页看最新章节</a><br/>
    -------------<br/><%
     NewsBean currentNew;
    for(int i = 0;i <news.size();i++)
        { currentNew = (NewsBean)news.get(i);
          String title = currentNew.getTitle();
          int id = currentNew.getId();
        %>
    <a href="<%=("http://wap.joycool.net/news/NewsInfo.do?newsId="+id+"&amp;jaLineId="+jaLineId+"&amp;orderBy=id")%>"><%=StringUtil.toWml(title)%></a><br/>
    <%}%>
    
    <%=PageUtil.shuzifenye(totalPage, currentPage, prefixUrl, true, " ", response)%> 
    <br/>
第<%=(currentPage + 1)%>页  共<%=totalPage%>页
    <%}%>
    <br/>
    <!--<a href="NewsCataList.do?id=<%=rootId%>" title="返回">返回新闻</a><br/>-->
    <%if(backTo != null){%>
    <a href="<%=(backTo.replace("&","&amp;"))%>" title="进入">返回<%=parentTitle%></a><br/>
    <%}%>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>