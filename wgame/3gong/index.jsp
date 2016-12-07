<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="三公">
<p align="left">
<%=BaseAction.getTop(request, response)%>
三公<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="history.jsp">查询战绩</a><br/>
三公游戏说明<br/>
-------------------<br/>
胜负判定规则为：<br/>
开局前必须先下注，下注后系统自动发给玩家发3张牌，通过比较大小，最后由牌点最大的获胜。如点数相等则打平。<br/>
牌型大小：<br/>
(1)三公：3张牌牌点相同时且为K、Q、J为最大牌型； <br/>
(2)三条：3张牌牌点相同时且为10以下数字为第二大牌型；  <br/>
(3)混三公牌：3张牌里分别有K、Q、J。如：K、Q、J等；<br/>
(4)普通牌：按面值计算点数，10、J、Q、K、为0点，10以下的即为其牌点，将三张牌的点数相加后，通过比较个位数的大小来决定牌面的大小；<br/>
(5)以上4种牌型大小依次为：三公＞三条＞混三公牌＞普通牌。<br/>
积分规则：<br/>
(1)普通牌:点数相加后最大的赢得1倍积分，点数相同的算平局；<br/>
(2)普通牌：三张牌点数相加后为8、9点，可以得到下注的2倍积分，点数相同的算平局；<br/>
(3)混三公牌：可以得到的3倍积分，牌型相同的算平局；<br/>
(4)三条：可以得到4倍积分，牌型相同的算平局；<br/>
(5)三公：(即最大牌型)可以得到5倍积分，牌型相同的算平局；<br/>
<br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>