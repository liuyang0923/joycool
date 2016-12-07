<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FootBallAction action = new FootBallAction(request);
action.pkRightStart(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
WGamePKBean football = (WGamePKBean) request.getAttribute("football");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="应战">
<p align="center">应战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//dice为空
if(football == null){
%>
该局已结束!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
} else {
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(football.getLeftUserId());
%>
<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(football.getLeftNickname())%>向您提出挑战,赌注是<%=football.getWager()%>个乐币(如果当前对手和您的乐币数比该赌注少,以你们少的一方的乐币数为准)<br/>
请选择守门方向:<br/>
<img src="/wgamepk/football/img/shoumen.gif" alt="球门"/><br/>
<anchor title="左">左
    <go href="/wgamepk/football/pkRightDeal1.jsp" method="post">
	<postfield name="action" value="l"/>
	<postfield name="pkId" value="<%=football.getId()%>"/>
    </go>
</anchor>
&nbsp;
<anchor title="中">中
    <go href="/wgamepk/football/pkRightDeal1.jsp" method="post">
	<postfield name="action" value="m"/>
	<postfield name="pkId" value="<%=football.getId()%>"/>
    </go>
</anchor>
&nbsp;
<anchor title="右">右
    <go href="/wgamepk/football/pkRightDeal1.jsp" method="post">
	<postfield name="action" value="r"/>
	<postfield name="pkId" value="<%=football.getId()%>"/>
    </go>
</anchor><br/>

<%
}
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamepk/football/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>