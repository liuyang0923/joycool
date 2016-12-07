<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="三公帮助">
<p align="center">三公帮助</p>
<p align="left"><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(8,request,response)%>
游戏规则：<br/>
开局前闲家必须先下注，下注后给每个玩家发3张牌，通过比较大小，最后由牌点最大的玩家获胜。如点数相等则打平。<br/>
牌型大小：<br/>
(1)三公：3张牌牌点相同时且为K、Q、J为最大牌型； <br/>
(2)三条：3张牌牌点相同时且为10以下数字为第二大牌型；  <br/>
(3)混三公牌：3张牌里分别有K、Q、J。如：K、Q、J等；<br/>
(4)普通牌：按面值计算点数，10、J、Q、K、为0点，10以下的即为其牌点，将三张牌的点数相加后，通过比较个位数的大小来决定牌面的大小；<br/>
(5)以上4种牌型大小依次为：三公＞三条＞混三公牌＞普通牌。<br/>
积分规则：<br/>
(1)普通牌:点数相加后最大的玩家赢得1倍积分,点数相同的算平局；<br/>
(2)普通牌：三张牌点数相加后为8、9点，可以得到下注的2倍积分,点数相同的算平局；<br/>
(3)混三公牌：该玩家可以得到的3倍积分,牌型相同的算平局；<br/>
(4)三条：玩家得到三条，可以得到4倍积分,牌型相同的算平局；<br/>
(5)三公：玩家得到三公(即最大牌型),可以得到5倍积分,牌型相同的算平局；<br/>

游戏玩法：<br/>
进入每个房间后，玩家可以：<br/>
1、坐庄，等待别人挑战。<br/>
2、找空闲玩家强行PK，如果对方两分钟内没有回应，可以自动获胜。<br/>
3、挑战其他玩家的坐庄。<br/>
4、回应其他玩家的强行PK。<br/>
注意事项：<br/>
1、一个玩家同一时间只能坐庄一个赌局，要等其他玩家挑战了该赌局结束之后才能坐庄其他的赌局。玩家也可以取消自己坐庄的赌局，但是要交纳该局赌注的的十分之一作为场地费。<br/>
2、一个玩家同一时间只能强行PK一次，要等对方回应或者是对方超时，该PK结束之后才能再找其他人PK。<br/>
3、玩家在大厅里的各个页面转悠的时候，一有任何消息系统都会自动通知玩家。玩家在大厅内时千万不要在同一页面停留两分钟以上！否则可能因为超过两分钟而没收到其他玩家的强行PK信息而被自动判负。<br/>
4、进入一个房间之后要退出，一定要点“退出房间”链接。否则会到30分钟后系统才会认为玩家退出，而这段时间玩家可能会被别人强行PK多次！<br/>
<br/>
<a href="/wgamepk/3gong/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>