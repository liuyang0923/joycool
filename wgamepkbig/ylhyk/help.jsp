<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富豪游戏帮助">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(9,request,response)%>
大富豪游戏规则<br/>
1.大富豪游戏最低下注金额1亿，上限200亿，金额为整数亿。<br/>
2.目前开放的大富豪游戏为“要乐还要酷”，坐庄时可以任选乐或酷作为答案，挑庄时也是2选1。<br/>
3.为了缩小乐酷贫富差距，每局的赢家将被扣除千分之五赢得的乐币，存入乐酷慈善基金。<br/>

<br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>