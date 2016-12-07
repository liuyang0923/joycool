<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="问答接龙">
<p align="center">问答接龙-用户协议</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
【游戏目的】<br/>
1、游戏的主旨是趣味、知识。<br/>
2、在游戏中可以闯关答题，知识面越广，奖励的乐币就越多。<br/>
【游戏守则】<br/>
问答接龙是一个提供玩家娱乐、赚币的游戏。不提倡无节制的盲目挑战。因此：<br/>
1、问答接龙每天早上8点开放营业，晚上12点结束。非营业期间游戏虽然可以正常进行，但是不会有任何奖励。<br/>
2、如果一天内闯关次数超过限度，将会自动禁止，或者停止发放任何奖励。<br/>
3、和乐酷的其他游戏一样，问答接龙游戏不允许使用任何形式的外挂，否则将给予严厉的惩罚。
对于玩家使用外挂的处理办法,请参考<a href="/admin/proto.jsp">乐酷用户协议和游戏规则总则</a><br/>
<br/>
<a href="index.jsp">返回问答接龙</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>