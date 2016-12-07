<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷街霸帮助">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(3,request,response)%>
乐酷街霸规则<br/>
动作组设置：
每个人有3组动作设置，动作组用来提前设置自己的出招顺序，<br/>
每个动作组有10点行动点数，<br/>
不同的动作会消耗相应的行动点数，<br/>
在游戏开始的时候，选择事先设置好的动作组进行游戏<br/>
轻拳：消耗1个行动点，杀伤力最小<br/>
重拳：消耗2个行动点，杀伤力中等<br/>
重腿：消耗3个行动点，杀伤力最大<br/>
防御：消耗1个行动点，对重攻击效果不好<br/>
闪避：消耗2个行动点，躲避对方的所有攻击<br/>
输赢判断：打中对手的杀伤大者胜。但如果中途，累计受到的杀伤过大，就会直接被KO而失败<br/>
<br/>
<a href="/wgamefight/index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛赌坊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>