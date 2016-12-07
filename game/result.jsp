<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.game.GameBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String gameN=(String)request.getAttribute("gameName");
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector games = (Vector)request.getAttribute("games");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏搜索">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%if((games == null)||(games.size()<1)){%>
    搜索结果为空<br/>
    <%}else
    {  %>
<%
     GameBean currentGame;
    for(int i = 0;i <games.size();i++)
        { currentGame = (GameBean)games.get(i);
          String gameName = currentGame.getName();
        %>
    <%=(Integer.parseInt(perPage)*Integer.parseInt(pageIndex) + i+1)%>:
    <a href="GameInfo.do?gameId=<%=currentGame.getId()%>"><%=gameName%></a><br/>
	人气：<%=currentGame.getHits()%><br/>
    <%}%>
    <%}%>
<%if(games!=null&&games.size()>5){%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"SearchGame.do?gameName="+gameN,true," ",response)%><br/>
<%}%>
<a href="/Column.do?columnId=5186" title="返回">返回游戏首页</a><br/>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>