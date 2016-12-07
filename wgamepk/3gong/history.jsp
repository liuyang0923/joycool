<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.action.wgamepk.Gong3Action"%><%
response.setHeader("Cache-Control","no-cache");
Gong3Action action = new Gong3Action(request);
action.history(request);

HistoryBean history = (HistoryBean) request.getAttribute("history");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="三公战绩">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您今天的战绩:<br/>
-------------------<br/>
<%=history.getWinCount()%>胜<%=history.getDrawCount()%>平<%=history.getLoseCount()%>负,总共<%if(history.getMoney() >= 0){%>赢了<%}else{%>输了<%}%><%=(history.getMoney() >= 0 ? history.getMoney() : - history.getMoney())%>乐币.<br/>
<br/>
<a href="/wgamepk/3gong/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>