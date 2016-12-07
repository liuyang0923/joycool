<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.image.ImageBean"%><%@ page import="net.joycool.wap.bean.image.ImageFileBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
int totalPage = ((Integer)request.getAttribute("totalPageCount")).intValue();
int currentPage = ((Integer)request.getAttribute("currentPage")).intValue();
int wapType = ((Integer)request.getAttribute("wapType")).intValue();
//String backTo = (String)request.getAttribute("backTo");
String prefixUrl = (String)request.getAttribute("prefixUrl");
String name = (String)request.getAttribute("name");
Vector images = (Vector)request.getAttribute("imagesList");
String id = (String)request.getAttribute("id");
String orderBy = (String)request.getAttribute("orderBy");
String rootBackTo = (String)request.getAttribute("rootBackTo");
String rootId = (String)request.getAttribute("rootId");
String jaLineId = (String)request.getAttribute("jaLineId");
String cId=(String)request.getParameter("id");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=name%>">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%if((images == null)||(images.size()<1)){%>
    该类别目前没有图片！<br/>
    <%}else
    {  %>
    <%=name%><br/>
    <%if(orderBy.equals("hits")){%>
<a href="ImageCataList.do?id=<%=id %>&amp;orderBy=id&amp;jaLineId=<%=jaLineId%>">按时间</a>|按人气<br/>
    <%}else{%>
按时间|<a href="ImageCataList.do?id=<%=id %>&amp;orderBy=hits&amp;jaLineId=<%=jaLineId%>">按人气</a><br/>
    <%}%>
    -------------<br/><%
     ImageBean currentImage;
    for(int i = 0;i <images.size();i++)
        { currentImage = (ImageBean)images.get(i);
          String imageName = currentImage.getName();
          String code = currentImage.getCode();
          ImageFileBean file7070 = currentImage.getFile7070();
          if(file7070==null)
          	continue;
          String imageUrl = file7070.getRealFileUrl();
        %>
    <%=i+1%>:
    <a href="<%=(currentImage.getLinkUrl())%>"><%=StringUtil.toWml(imageName)%></a><br/>
    <img src="<%=imageUrl%>" alt="loading..."/><br/>
    <%}%>
    <%=PageUtil.shangxiafenye(totalPage, currentPage, prefixUrl, true, "<br/>", response)%> 
    <br/>
第<%=(currentPage + 1)%>页  共<%=totalPage%>页<br/>
<%
if(wapType == 0){
%>
跳到第<input type="text" name="pageIndex1" maxlength="3" value="0"/>页 <anchor title ="go">Go
         <go href="<%=(prefixUrl)%>" method="post">
             <postfield name="pageIndex1" value="$(pageIndex1)"/>
         </go>
         </anchor><br/>
搜索：<input name="imageName" maxlength="20" value="v"/> <anchor title ="search Image">Go
         <go href="SearchImage.do" method="post">
             <postfield name="imageName" value="$(imageName)"/>
         </go>
         </anchor><br/>
<%
}
%>
    <%}%>
    <br/>
<a href="/Column.do?columnId=4381">返回图片首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>