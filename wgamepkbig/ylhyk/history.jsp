<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.big.HistoryBigBean" %><%@ page import="net.joycool.wap.action.wgamepk.big.YlhykAction" %><%
response.setHeader("Cache-Control","no-cache");
YlhykAction action = new YlhykAction(request);
action.history(request);
HistoryBigBean history = (HistoryBigBean) request.getAttribute("history");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富豪战绩">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您今天的战绩:<br/>
-------------------<br/>
<%=history.getWinCount()%>胜<%=history.getDrawCount()%>平<%=history.getLoseCount()%>负,总共<%if(history.getMoney() >= 0){%>赢了<%}else{%>输了<%}%><%=(history.getMoney() >= 0 ? history.getMoney() : - history.getMoney())%>乐币.<br/>
最近<a href="history2.jsp">坐庄</a>|<a href="history3.jsp">挑战</a>记录<br/>
<br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>