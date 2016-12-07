<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamefree.G21Action"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");

G21Action ga = new G21Action();
ga.deal2(request);
String next = (String) request.getAttribute("next");
G21Bean g21 = (G21Bean) session.getAttribute("g21");
if(g21==null){
	response.sendRedirect("start.jsp");
	return;
}
int count, i;
CardBean card = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//继续
if("continue".equals(next)){
%>
<card title="二十一点">
<p align="left">
<%=BaseAction.getTop(request, response)%>
二十一点<br/>
-------------------<br/>
系统牌:<br/>
<%
//显示系统牌
count = g21.getSystemCards().size();
for(i = 0; i < count; i ++){
%><img src="http://wap.joycool.net/wgame/cardImg/back.gif" alt="x"/><%
}
%>
<br/>
您的牌:<br/>
<%
//显示系统牌
count = g21.getUserCards().size();
for(i = 0; i < count; i ++){
	card = (CardBean) g21.getUserCards().get(i);
%><img src="<%=card.getPicUrl()%>" alt="<%=card.getValue()%>"/><%
}
%>
<br/>
<a href="deal2.jsp">继续要牌</a><br/>
<a href="deal3.jsp">我要开牌</a><br/>
<a href="start.jsp">放弃这局</a><br/>
<a href="start.jsp">返回上一级</a><br/>
<a href="../index.jsp">返回赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else {
	String url = ("deal3.jsp");
%>
<card title="二十一点" ontimer="<%=response.encodeURL(url)%>">
<timer value="10"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
二十一点<br/>
-------------------<br/>
<%if("breakWin".equals(next)){%>要开牌啦！您已经有五张牌了！<%}else{%>要开牌啦！您的牌大于21点了，可能要输了哦！<%}%>
<br/>
1秒后出结果！<br/>
心急了？<a href="<%=url%>">直接进入</a><br/>
</p>
</card>
<%
}
%>
</wml>