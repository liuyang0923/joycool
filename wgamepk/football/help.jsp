<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="射门帮助">
<p align="center">射门帮助</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
游戏规则：<br/>
庄家选择射门方向，如果守门员判断失误，点球罚进，庄家赢2倍下注积分,守门员输2倍积分。<br/>
如果守门员判断正确，点球被扑住，庄家输1倍下注积分，守门员赢1倍积分。<br/>
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
<a href="/wgamepk/football/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>