<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamefree.JsbAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingJsb") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgamefree/jsb/start.jsp", response);
	return;
}
session.removeAttribute("playingJsb");
JsbAction action = new JsbAction();
action.deal2(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
String systemAction = (String) request.getAttribute("systemAction");
JsbBean jsb = (JsbBean) request.getAttribute("jsb");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="剪刀石头布">
<p align="left">
<%=BaseAction.getTop(request, response)%>
剪刀石头布<br/>
-------------------<br/>
<img src="img/<%=jsb.getAction()%>.gif" alt="<%=jsb.getAction()%>"/>&lt;-&gt;<img src="img/<%=systemAction%>.gif" alt="<%=systemAction%>"/><br/>
<%
//打平
if("draw".equals(result)){
%>
您和系统打平了。<br/>
<%
}
//赢了
else if("win".equals(result)){
	WGameBean wins = (WGameBean) session.getAttribute("jsbWins");
	GirlBean girl = Girls.getGirl(wins.getGirlId());
	String[] picList = girl.getPicList();
	int picIndex = wins.getWins() - 1;
	if(picIndex > (picList.length - 1)){
		picIndex = picList.length - 1;
	}
%>
恭喜您，您赢了！<br/>
您赢了<%=jsb.getWager()%>个乐币！<br/>
现在您已经赢了<%=wins.getWins()%>次！加油吧！连赢次数越多，美女越开放！<br/>
<img src="<%=picList[picIndex]%>" alt="<%=girl.getName()%>"/><br/>
<%
} 
//输了
else {
%>
呜呜呜，您输了！<br/>
您输了<%=jsb.getWager()%>个乐币！<br/>
<%
}
%>
<br/>
<a href="start.jsp">继续PK</a><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../index.jsp">返回赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>