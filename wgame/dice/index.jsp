<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="猜大小">
<p align="left">
<%=BaseAction.getTop(request, response)%>
猜大小<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="history.jsp">查询战绩</a><br/>
猜大小游戏说明<br/>
-------------------<br/>
系统会随机掷出3个骰子，游戏中玩家只需猜大或者猜小，并对大小下注即可。10点以上（不含10点）为大。猜中后积分翻倍。<br/>
<br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>