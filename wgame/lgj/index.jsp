<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="老虎杠子鸡">
<p align="left">
<%=BaseAction.getTop(request, response)%>
老虎杠子鸡<br/>
客官，来划两拳怎么样？不罚酒，罚乐币的!<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="history.jsp">查询战绩</a><br/>
老虎杠子鸡游戏说明<br/>
-------------------<br/>
胜负判定规则为：杠子>老虎>鸡>虫子>杠子，同样的或者相隔一个的为平手。<br/>
<br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>