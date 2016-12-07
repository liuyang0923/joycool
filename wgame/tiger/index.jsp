<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="老虎机">
<p align="left">
<%=BaseAction.getTop(request, response)%>
老虎机<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="history.jsp">查询战绩</a><br/>
老虎机游戏说明<br/>
-------------------<br/>
下注->开跑->赢钱或输钱，就这么简单！最大赔率1:80，也就是说，如果您得到三个酷狗头的话，您将赢得您的赌注的80倍那么多乐币哦！<br/>
赔率表(无序):<br/>
狗狗狗:80<br/>
狗狗＄:40<br/>
狗狗心:20<br/>
狗狗七:10<br/>
狗狗铃:5<br/>
一个狗:1<br/>
<br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>