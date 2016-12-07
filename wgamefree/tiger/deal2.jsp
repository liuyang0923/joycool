<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamefree.TigerAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingTiger") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgamefree/tiger/start.jsp", response);
	return;
}
session.removeAttribute("playingTiger");
TigerAction action = new TigerAction();
action.deal2(request);
String result = (String) request.getAttribute("result");
TigerBean tiger = (TigerBean) request.getAttribute("tiger");
int[] results = tiger.getResults();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="老虎机">
<p align="left">
<%=BaseAction.getTop(request, response)%>
老虎机<br/>
-------------------<br/>
<img src="img/<%=results[0]%>.gif" alt="<%=results[0]%>"/><img src="img/<%=results[1]%>.gif" alt="<%=results[1]%>"/><img src="img/<%=results[2]%>.gif" alt="<%=results[2]%>"/><br/>
<%
//赢了
if("win".equals(result)){
	WGameBean wins = (WGameBean) session.getAttribute("tigerWins");
	GirlBean girl = Girls.getGirl(wins.getGirlId());
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您，您赢了！赔率是1:<%=tiger.getResult()%><br/>
您赢了<%=(tiger.getWager() * tiger.getResult())%>个乐币！<br/>
现在您已经赢了<%=wins.getWins()%>次！加油吧！连赢次数越多，美女越开放！<br/>
<img src="<%=picList[picIndex]%>" alt="<%=girl.getName()%>"/><br/>
<%
} 
//输了
else {
%>
呜呜呜，您输了！<br/>
您输了<%=tiger.getWager()%>个乐币！<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续玩</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../index.jsp">返回赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>