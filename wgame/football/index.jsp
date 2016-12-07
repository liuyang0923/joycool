<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="射门">
<p align="left">
<%=BaseAction.getTop(request, response)%>
射门<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="history.jsp">查询战绩</a><br/>
射门游戏说明<br/>
-------------------<br/>
玩家选择射门方向，如果守门员判断失误，点球罚进，玩家赢得1倍下注积分。<br/>
如果守门员判断正确，点球被扑住，玩家输1倍下注积分。<br/>
<br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>