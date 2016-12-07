<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="掷骰子比大小">
<p align="left">
<%=BaseAction.getTop(request, response)%>
掷骰子比大小<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="history.jsp">查询战绩</a><br/>
掷骰子比大小游戏说明<br/>
-------------------<br/>
下注后，系统会随机掷出2对骰子（系统骰子和玩家骰子），玩家的骰子大于系统的骰子玩家获胜，反之玩家输给系统。<br/>
<a href="/guest/index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>