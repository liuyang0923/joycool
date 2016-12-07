<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.pgame.PGameBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");

PGameBean pgame = (PGameBean)request.getAttribute("pgame");
PGameBean nextGame = (PGameBean)request.getAttribute("nextGame");
PGameBean prevGame = (PGameBean)request.getAttribute("prevGame");
String orderBy = (String)request.getAttribute("orderBy");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=pgame.getName()%>">
<p align="left"> 
<%=BaseAction.getTop(request, response)%>
<%=pgame.getName()%><br/>
<%if(pgame.getPicUrl() != null && !pgame.getPicUrl().equals("")){%><img src="<%=pgame.getRealPicUrl()%>" alt="loading..."/><br/><%}%>
游戏说明：<%=pgame.getDescription()%><br/>
大小：<%=pgame.getKb()%>K<br/>
适用机型：<%=pgame.getFitMobile()%><br/>
<%
if(pgame.getFree() == 0){
%>
<a href="http://211.152.13.44:8000/hz/lszx1.jsp?u=<%=URLEncoder.encode("http://wap.joycool.net/pgame/pd.jsp?id=" + pgame.getId())%>">点击下载</a><br/>
<%
} else {
%>
<a href="<%=pgame.getRealFileUrl()%>">点击下载</a><br/>
<%
}
%>
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
}
%>
    <a href="PGameList.do?providerId=<%=pgame.getProviderId() %>&amp;orderBy=<%=orderBy%>" title="进入">返回上一级</a><br/>
	<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>