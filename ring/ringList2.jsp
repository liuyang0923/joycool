<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.ring.PringBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
int ITEM_PER_PAGE=20;
int totalPage = StringUtil.toInt((String)request.getAttribute("totalPageCount"));
int currentPage =  StringUtil.toInt((String)request.getAttribute("pageIndex"));
Vector ringList = (Vector)request.getAttribute("ringList");
int totalPageCount=Integer.parseInt((String)request.getAttribute("totalPageCount"));
int pageIndex=Integer.parseInt((String)request.getAttribute("pageIndex"));
HashMap map=new HashMap();
map.put("fileType",request.getParameter("fileType"));
int hasItems=ITEM_PER_PAGE*pageIndex;
String pagination=JCRoomChatAction.pagination(totalPageCount,pageIndex,"http://wap.joycool.net//ring/RingCataList.do",map,response);
//zhul_2006-08-08 modify backTo start
//UrlMapBean urlBean=(UrlMapBean)URLMap.getURLCatalogMap().get(new Integer(StringUtil.toInt(id)));
//zhul_2006-08-08 modify backTo end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷铃声">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
乐酷<%=request.getParameter("fileType")%>炫铃<br/>
------------<br/>
    <%if((ringList == null)||(ringList.size()<1)){%>
    该类别目前没有铃声！<br/>
    <%}else
    {  %>
  <%
     PringBean currentGame;
    for(int i = 0;i <ringList.size();i++)
        { currentGame = (PringBean)ringList.get(i);
          String gameName = currentGame.getName();
        %>
    <%=hasItems+i+1%>:
    <a href="/ring/RingInfo.do?ringId=<%=currentGame.getId()%>"><%=gameName%></a><br/>
    <%--歌手：<%=currentGame.getSinger()%><br/>--%>
	人气：<%=currentGame.getDownload_sum()%><br/>
<%
if(currentGame.getLinkUrl() != null && !currentGame.getLinkUrl().equals("")){
%>
    <img src ="<%=currentGame.getLinkUrl()%>" alt="loading....."/><br/>
<%
}
%>	
    <%}%>
    <%if(!(pagination==null||pagination.equals(""))){%><%=pagination%><br/><%}%>
    <%}%>
    <br/>
    <a href="http://wap.joycool.net/Column.do?columnId=5188&amp;jaLineId=2133">返回铃声首页</a><br/>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>