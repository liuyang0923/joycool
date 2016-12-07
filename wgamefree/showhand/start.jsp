<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%
response.setHeader("Cache-Control","no-cache");
session.setAttribute("playingShowHand", "playing");

WGameBean wins = (WGameBean) session.getAttribute("showhandWins");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="梭哈">
<p align="left">
<%=BaseAction.getTop(request, response)%>
梭哈<br/>
<%
//已经选择美女
if(wins != null){
	GirlBean girl = (GirlBean) Girls.getGirl(wins.getGirlId());
%>
现在是<%=girl.getName()%>在陪您玩，您已经赢了<%=wins.getWins()%>次了！加油哦！连赢次数越多，美女越开放！<br/>
<%
} else {
%>
美女陪玩！连赢次数越多，美女越开放！<br/>
<%
}
%>
-------------------<br/>
设定本局最大赌注（最少100乐币）：<br/>
<input type="text" name="wager" format="*N" maxlength="10" value="100" title="下注"/><br/>
<%
if(wins == null){
	Vector girls = Girls.getGirls();
	Iterator itr = girls.iterator();
	GirlBean girl = null;
%>
选择美女：<br/>
<select name="girlId" value="1">
<%
	while(itr.hasNext()){
	    girl = (GirlBean) itr.next();
%>
    <option value="<%=girl.getId()%>"><%=girl.getName()%></option>
<%
    }
%>
</select><br/>
<%
}
%>
<anchor title="确定">确定
    <go href="deal1.jsp" method="post">
    <postfield name="wager" value="$wager"/>
<%
if(wins == null) {
%>
	<postfield name="girlId" value="$girlId"/>
<%
}
%>
    </go>
</anchor><br/>
<br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../index.jsp">返回赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>