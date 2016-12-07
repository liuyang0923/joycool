<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.friendlink.RandomLinkBean,net.joycool.wap.util.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.ebook.EBookBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String ebookN=(String)request.getAttribute("ebookName");
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector ebooks = (Vector)request.getAttribute("ebooks");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="电子书搜索">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
<%
    if((ebooks == null)||(ebooks.size()<1)){%>
    搜索结果为空<br/>
    <%}else
    {  %>
<%  EBookBean currentEBook;
    for(int i = 0;i <ebooks.size();i++)
        { currentEBook = (EBookBean)ebooks.get(i);
          String ebookName = currentEBook.getName();
          int id = currentEBook.getId();
        %>
    <%=(Integer.parseInt(perPage)*Integer.parseInt(pageIndex) + i+1)%>.<a href="EBookInfo.do?ebookId=<%=id%>"><%=StringUtil.toWml(ebookName)%></a><br/>
    <%}%>
    <%}%>
<%if(ebooks!=null&&ebooks.size()>5){%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"SearchEBook.do?ebookName="+ebookN,true," ",response)%><br/>
<%}%>
	<a href="EBookCataList.do" title="返回">返回电子书城</a><br/>
	<%=BaseAction.getBottom(request, response)%><br/>
</p>
</card>
</wml>