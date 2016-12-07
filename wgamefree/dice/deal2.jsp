<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamefree.DiceAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingDice") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgamefree/dice/start.jsp", response);
	return;
}
session.removeAttribute("playingDice");
DiceAction da = new DiceAction();
da.deal2(request);
String result = (String) request.getAttribute("result");
DiceBean dice = (DiceBean) request.getAttribute("dice");
int[] dices = (int[]) request.getAttribute("dices");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="掷骰子">
<p align="left">
<%=BaseAction.getTop(request, response)%>
掷骰子<br/>
-------------------<br/>
<img src="img/<%=dices[0]%>.gif" alt="<%=dices[0]%>"/><img src="img/<%=dices[1]%>.gif" alt="<%=dices[1]%>"/><img src="img/<%=dices[2]%>.gif" alt="<%=dices[2]%>"/><br/>
<%
//赢了
if("win".equals(result)){
	WGameBean wins = (WGameBean) session.getAttribute("diceWins");
	GirlBean girl = Girls.getGirl(wins.getGirlId());
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您，您猜中了！<br/>
您赢了<%=dice.getWager()%>个乐币！<br/>
现在您已经赢了<%=wins.getWins()%>次！加油吧！连赢次数越多，美女越开放！<br/>
<img src="<%=picList[picIndex]%>" alt="<%=girl.getName()%>"/><br/>
<%
} 
//输了
else {
%>
呜呜呜，您猜错了！<br/>
您输了<%=dice.getWager()%>个乐币！<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续猜</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../index.jsp">返回赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>