<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="二十一点">
<p align="left">
<%=BaseAction.getTop(request, response)%>
二十一点<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="history.jsp">查询战绩</a><br/>
二十一点游戏说明<br/>
-------------------<br/>
一开始给用户发两张牌，用户可以要牌直到手中的牌最接近21点为止。最多能有五张牌。开牌后：未超过21点，以点数大者胜。都超过21点，视为"暴掉"，无输赢，算平局。同样点数，视为平局，无输赢。JQK均代表十点，A可代表一点，也可以代表十一点。<br/>
Blackjack:如果只有两张牌，一张为Ace(A)，一张为分值为十点的牌（10和JQK），则是Blackjack（黑杰克）。如果对方也是Blackjack，则打平，否则赢得2倍赌注。<br/>
<br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>