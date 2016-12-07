<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.ring.PringBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UrlMapBean" %><%
response.setHeader("Cache-Control","no-cache");
int totalPage = ((Integer)request.getAttribute("totalPageCount")).intValue();
int currentPage = ((Integer)request.getAttribute("currentPage")).intValue();
String backTo = (String)request.getAttribute("backTo");
String prefixUrl = (String)request.getAttribute("prefixUrl");
String name = (String)request.getAttribute("name");
Vector rings = (Vector)request.getAttribute("ringsList");
String id = (String)request.getAttribute("id");
String rootId = (String)request.getAttribute("rootId");
String orderBy = (String)request.getAttribute("orderBy");
String cId=(String)request.getParameter("id");
//zhul_2006-08-08 modify backTo start
UrlMapBean urlBean=(UrlMapBean)URLMap.getURLCatalogMap().get(new Integer(StringUtil.toInt(id)));
//zhul_2006-08-08 modify backTo end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷炫铃">
<p align="left">
<%=BaseAction.getTop(request, response)%>
    <%if((rings == null)||(rings.size()<1)){%>
    该类别目前没有炫铃！<br/>
    <%}else
    {  %>
    <%=name%><br/>
<%if(orderBy.equals("download_sum")){%>
<a href="RingCataList.do?id=<%=id %>&amp;orderBy=id">按时间</a>|按人气<br/>
<%}else{%>
按时间|<a href="RingCataList.do?id=<%=id %>&amp;orderBy=down">按人气</a><br/>
<%}%>
    -------------<br/><%
     PringBean currentRing;
    for(int i = 0;i <rings.size();i++)
        { currentRing = (PringBean)rings.get(i);
          String ringName = currentRing.getName();
        %>
    <%=i+1%>:
    <a href="RingInfo.do?ringId=<%=currentRing.getId()%>"><%=ringName%></a><br/>
	人气：<%=currentRing.getDownload_sum()%><br/>
<%
//if(currentGame.getPicUrl() != null && !currentGame.getPicUrl().equals("")){
%>
  <!--  <img src ="<%//=currentGame.getRealPicUrl()%>" alt="loading....."/><br/>-->
<%
//}
%>	
    <%}%>
    <%=PageUtil.shuzifenye(totalPage, currentPage, prefixUrl, true, " ", response)%> 
    <br/>
第<%=(currentPage + 1)%>页  共<%=totalPage%>页<br/>
跳到第<input type="text" name="pageIndex1" maxlength="3" />页 <anchor title ="go">Go
         <go href="<%=(prefixUrl)%>" method="post">
             <postfield name="pageIndex1" value="$(pageIndex1)"/>
         </go>
         </anchor><br/>
搜索：<input name="ringName" maxlength="20" /> 
         <anchor title ="search Ring">Go
         <go href="SearchRing.do" method="post">
             <postfield name="ringName" value="$(ringName)"/>
         </go>
         </anchor><br/>
    <%}%>
    <br/>
    <%if(cId != null){%>
    <%String url=URLMap.getBacktoURL("RingCataList.do?id=",StringUtil.toInt(cId));%>
    <a href="<%=(url)%>" title="进入">返回<%=urlBean!=null?urlBean.getTitle():"上一级"%></a><br/>
    <%}%>
<%--    <a href="RingCataList.do?id=<%=rootId%>" title="返回">返回炫铃</a><br/>--%>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>