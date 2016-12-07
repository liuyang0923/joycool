<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.URLEncoder"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.game.GameBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.guestbook.CommentAction"%><%!

static long lastTime=0;
static int visitCount=0;
%><%
response.setHeader("Cache-Control","no-cache");
visitCount++;
if(visitCount>5){
	long now=System.currentTimeMillis();
	if(now<lastTime)
		return;
	lastTime=now+20000l;
}


String prefixUrl = (String)request.getAttribute("prefixUrl");
GameBean game = (GameBean)request.getAttribute("game");
if(game == null){
response.sendRedirect(("/lswjs/index.jsp"));
return;
}
GameBean nextGame = (GameBean)request.getAttribute("nextGame");
GameBean prevGame = (GameBean)request.getAttribute("prevGame");
String backTo = (String)request.getAttribute("backTo");
CatalogBean catalog = (CatalogBean)request.getAttribute("catalog");
String downloadAddress;
if((game.getFileUrl()==null)||(game.getFileUrl().equals("")))
{
    if((game.getRemoteUrl()==null)||(game.getRemoteUrl().equals("")))
    {
        downloadAddress = "http://wap.joycool.net";
    }else{
        downloadAddress = game.getRemoteUrl().replace("&", "&amp;");
    }
}else {
    downloadAddress = game.getRealFileUrl();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=game.getName()%>">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
    <%=game.getName()%><br/>
    游戏说明：<%=game.getDescription()%><br/>
    大小：<%=game.getKb()%>Kb<br/><%--适用机型：<%=game.getFitMobile()%><br/>--%>
<%
if(game.getPicUrl() != null && !game.getPicUrl().equals("")){
%>
    <img src ="<%=game.getRealPicUrl()%>" alt="loading....."/><br/>
<%
}
%>
    <a href="<%=downloadAddress%>">点击下载</a><br/>
    <br/>
<a href="/cart/add.jsp?title=<%=URLEncoder.encode("游戏:"+game.getName(),"utf8")%>&amp;url=<%=URLEncoder.encode("/game/GameInfo.do?gameId="+game.getId(),"utf8")%>">&gt;&gt;加入收藏</a><br/>
<%--原来的backTo:PageUtil.getBackTo(request) --%>
<%=CommentAction.getCommentEntry(2, game.getId(), "相关评论", "/game/GameInfo.do?gameId=" + game.getId(), response)%>
<br/>
    <%
//如果下一个游戏不为空，显示下一条
if(nextGame != null){
%>
    <a href="<%=nextGame.getLinkUrl()%>" title="进入">下一条：<%=nextGame.getName()%></a><br/>
<%
}
//如果上一个游戏不为空，显示上一条
if(prevGame != null){
%>
    <a href="<%=prevGame.getLinkUrl()%>" title="进入">上一条：<%=prevGame.getName()%></a><br/>
<%
}if(catalog!=null){
%>   
    <a href="GameCataList.do?id=<%=catalog.getId()%>" title="返回">返回<%=catalog.getName()%></a><br/>
    <%}%>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>