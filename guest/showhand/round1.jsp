<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.guest.wgame.ShowHandAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingShowHand2") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/guest/showhand/start.jsp", response);
	return;
}
//马长青_2006-6-21_显示系统用户名_start
WGameBean wins = (WGameBean) session.getAttribute("showhandWins");
GirlBean girl = Girls.getGirl(wins.getGirlId());
String girlName=girl.getName().substring(4,6);
//马长青_2006-6-21_显示系统用户名_end
ShowHandAction action = new ShowHandAction();
action.round1(request);
String next = (String) request.getAttribute("next");
request.removeAttribute("next");
String tip = (String) request.getAttribute("tip");
//继续下一轮
if("continue".equals(next)){
    String url = ("round2.jsp");
%>
<wml>
<card title="梭哈" ontimer="<%=response.encodeURL(url)%>">
<timer value="10"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
梭哈<br/>
-------------------<br/>
发第3张牌中...<br/>
1秒后跳转!<br/>
心急了?<a href="<%=url%>">直接进入</a><br/>
</p>
</card>
</wml>
<%
	return;
}
if("quit".equals(next)){
    String url = ("quit.jsp");
    out.clearBuffer();
    response.sendRedirect(url);
	return;
}

jc.guest.wgame.ShowHandBean sh = (jc.guest.wgame.ShowHandBean) session.getAttribute("showhand");
int cWager = sh.getMaxWager() - sh.getTotalWager();
int roundMaxWager = sh.getMaxWager() / 4;
int count, i;
CardBean card = null;
%>
<wml>
<card title="梭哈">
<p align="left">
<%=BaseAction.getTop(request, response)%>
梭哈<br/>
<%=(tip == null? "":"提示:" + tip + "<br/>")%>
-------------------<br/>
<%=girlName%>的牌:<br/>
<%
//显示系统牌
count = sh.getSystemCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) sh.getSystemCards().get(i);
	if(!card.isOpen()){
%><img src="/wgame/cardImg/back.gif" alt="x"/><%
    }else{%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
	}
}
%>
<br/>
您的牌:<br/>
<%
//显示用户牌
count = sh.getUserCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) sh.getUserCards().get(i);
%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
}
%>
<br/>
<%
//系统下注
if(sh.getFlag() == 0){
%>
对家下注<%=sh.getRoundWager()%>个游币!<br/>
<a href="round1.jsp?action=follow">跟了</a>|<a href="round1.jsp?action=quit">放弃</a><br/>
<input type="text" name="wager" format="*N" maxlength="10" value="<%=sh.getRoundWager()%>" title="下注"/><br/>
<anchor title="加注">加注
    <go href="round1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
	<postfield name="action" value="add"/>
    </go>
</anchor><br/>
<%
}else{
%>
<input type="text" name="wager" format="*N" maxlength="10" value="<%=sh.getRoundWager()%>" title="下注"/><br/>
<anchor title="下注">下注<go href="round1.jsp" method="post"><postfield name="wager" value="$wager"/><postfield name="action" value="add"/></go></anchor>|<a href="round1.jsp?action=quit">放弃</a><br/>
<%
}
%>
提示:<br/>
本轮最多可下注<%=roundMaxWager%>个游币<br/>
这局您还有<%=cWager%>个游币<br/>
当前台面上的游币是<%=(sh.getTotalWager() * 2)%>个.<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>