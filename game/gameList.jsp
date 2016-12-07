<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.game.GameBean"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
int totalPage = ((Integer)request.getAttribute("totalPageCount")).intValue();
int currentPage = ((Integer)request.getAttribute("currentPage")).intValue();
String backTo = (String)request.getAttribute("backTo");
String prefixUrl = (String)request.getAttribute("prefixUrl");
String name = (String)request.getAttribute("name");
String orderBy = (String)request.getAttribute("orderBy");
Vector games = (Vector)request.getAttribute("gamesList");
String id = (String)request.getAttribute("id");
String rootId = (String)request.getAttribute("rootId");

//zhul_2006-08-08 modify backTo start
UrlMapBean urlBean=(UrlMapBean)URLMap.getURLCatalogMap().get(new Integer(StringUtil.toInt(id)));
//zhul_2006-08-08 modify backTo end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷游戏">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%if((games == null)||(games.size()<1)){%>
    该类别目前没有游戏！<br/>
    <%}else
    {  %>
    <%=name%><br/>
    <%if(orderBy.equals("hits")){%>
<a href="GameCataList.do?id=<%=id %>&amp;orderBy=id">按时间</a>|按人气<br/>
    <%}else{%>
按时间|<a href="GameCataList.do?id=<%=id %>&amp;orderBy=hits">按人气</a><br/>
    <%}%>
    -------------<br/><%
     GameBean currentGame;
    for(int i = 0;i <games.size();i++)
        { currentGame = (GameBean)games.get(i);
          String gameName = currentGame.getName();
        %>
    <%=i+1%>:
    <a href="GameInfo.do?gameId=<%=currentGame.getId()%>"><%=gameName%></a><br/>
	人气：<%=currentGame.getHits()%><br/>
<%
if(currentGame.getPicUrl() != null && !currentGame.getPicUrl().equals("")){
%>
    <img src ="<%=currentGame.getRealPicUrl()%>" alt="loading....."/><br/>
<%
}
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
搜索：<input name="gameName" maxlength="20" /> <anchor title ="search Game">Go
         <go href="SearchGame.do" method="post">
             <postfield name="gameName" value="$(gameName)"/>
         </go>
         </anchor><br/>
    <%}%>
    <br/>
    <%if(backTo != null){%>
    <a href="<%=backTo.replace("&","&amp;")%>" title="进入">返回<%=urlBean!=null?urlBean.getTitle():"上一级"%></a><br/>
    <%}%>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>