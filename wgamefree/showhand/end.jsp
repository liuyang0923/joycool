<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamefree.ShowHandAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingShowHand") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgamefree/showhand/start.jsp", response);
	return;
}
session.removeAttribute("playingShowHand");
ShowHandAction action = new ShowHandAction();
action.end(request);
String result = (String) request.getAttribute("result");
ShowHandBean sh = (ShowHandBean) request.getAttribute("showhand");
int count, i;
CardBean card = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="梭哈">
<p align="left">
<%=BaseAction.getTop(request, response)%>
梭哈<br/>
-------------------<br/>
系统牌:<br/>
<%
//显示系统牌
count = sh.getSystemCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) sh.getSystemCards().get(i);
%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
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
//赢了
if("win".equals(result)){
	WGameBean wins = (WGameBean) session.getAttribute("showhandWins");
	if(wins==null){
		out.clearBuffer();
		response.sendRedirect("start.jsp");
		return;
	}
	GirlBean girl = Girls.getGirl(wins.getGirlId());
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您，您赢了！<br/>
您赢了<%=sh.getTotalWager()%>个乐币！<br/>
现在您已经赢了<%=wins.getWins()%>次！加油吧！连赢次数越多，美女越开放！<br/>
<img src="<%=picList[picIndex]%>" alt="<%=girl.getName()%>"/><br/>
<%
} 
//输了
else {
%>
呜呜呜，您输了！<br/>
您输了<%=sh.getTotalWager()%>个乐币！<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续梭哈</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../index.jsp">返回赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>