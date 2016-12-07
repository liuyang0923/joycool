<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="决战单双">
<p align="left">
<%=BaseAction.getTop(request, response)%>
决战单双<br/>

下多赢多，下少赢少，撑死胆大的，饿死胆小的喽！<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="history.jsp">查询战绩</a><br/>
决战单双游戏说明<br/>
-------------------<br/>
庄家随机撒出一些小辣椒，把辣椒的数量除以5，看余数是单还是双，只要玩家买对了，就会赢得相应的赌注。例如：庄家撒出23个小辣椒，23÷5=4余3，那买单的玩家赢。若余数为0，则不论单双庄家赢。<br/>

<br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>