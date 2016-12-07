<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.game.GameBean"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
int ITEM_PER_PAGE=20;

int totalPage = StringUtil.toInt((String)request.getAttribute("totalPageCount"));
int currentPage =  StringUtil.toInt((String)request.getAttribute("pageIndex"));
Vector gameList = (Vector)request.getAttribute("gameList");
int totalPageCount=Integer.parseInt((String)request.getAttribute("totalPageCount"));
int pageIndex=Integer.parseInt((String)request.getAttribute("pageIndex"));
HashMap map=new HashMap();
map.put("gameName",request.getParameter("gameName"));
int hasItems=ITEM_PER_PAGE*pageIndex;
String pagination=JCRoomChatAction.pagination(totalPageCount,pageIndex,"http://wap.joycool.net/game/GameCataList.do",map,response);
//zhul_2006-08-08 modify backTo start
//UrlMapBean urlBean=(UrlMapBean)URLMap.getURLCatalogMap().get(new Integer(StringUtil.toInt(id)));
//zhul_2006-08-08 modify backTo end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷游戏">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%if((gameList == null)||(gameList.size()<1)){%>
    该类别目前没有游戏！<br/>
    <%}else
    {  %>
    搜索游戏:<%=request.getParameter("gameName")%> <br/>
    -------------<br/><%
     GameBean currentGame;
    for(int i = 0;i <gameList.size();i++)
        { currentGame = (GameBean)gameList.get(i);
          String gameName = currentGame.getName();
        %>
    <%=hasItems+i+1%>:
    <a href="GameInfo.do?gameId=<%=currentGame.getId()%>"><%=gameName%></a><br/>
	人气：<%=currentGame.getHits()%><br/>
<%
if(currentGame.getPicUrl() != null && !currentGame.getPicUrl().equals("")){
%>
    <img src ="<%=currentGame.getRealPicUrl()%>" alt="o"/><br/>
<%
}
%>	
    <%}%>
    <%if(!(pagination==null||pagination.equals(""))){%><%=pagination%><br/><%}%>
搜索：<input name="gameName" maxlength="20"/> <anchor title ="search Game">Go
         <go href="GameCataList.do" method="post">
             <postfield name="gameName" value="$(gameName)"/>
         </go>
         </anchor><br/>
    <%}%>
    <br/>
    <a href="http://wap.joycool.net/Column.do?columnId=5186&amp;jaLineId=2129">返回游戏首页</a><br/>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>