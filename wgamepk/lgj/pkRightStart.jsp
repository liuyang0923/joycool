<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
LgjAction action = new LgjAction(request);
action.pkRightStart(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
WGamePKBean lgj = (WGamePKBean) request.getAttribute("lgj");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="应战">
<p align="center">应战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//dice为空
if(lgj == null){
%>
该局已结束!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
} else {
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(lgj.getLeftUserId());
%>
<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(lgj.getLeftNickname())%>向您提出挑战,赌注是<%=lgj.getWager()%>个乐币(如果当前对手和您的乐币数比该赌注少,以你们少的一方的乐币数为准)<br/>
请选择应战手式:<br/>
<anchor title="杠子">杠子
    <go href="/wgamepk/lgj/pkRightDeal1.jsp" method="post">
	<postfield name="action" value="a"/>
	<postfield name="pkId" value="<%=lgj.getId()%>"/>
    </go>
</anchor><br/>
<anchor title="老虎">老虎
    <go href="/wgamepk/lgj/pkRightDeal1.jsp" method="post">
	<postfield name="action" value="b"/>
	<postfield name="pkId" value="<%=lgj.getId()%>"/>
    </go>
</anchor><br/>
<anchor title="鸡">鸡
    <go href="/wgamepk/lgj/pkRightDeal1.jsp" method="post">
	<postfield name="action" value="c"/>
	<postfield name="pkId" value="<%=lgj.getId()%>"/>
    </go>
</anchor><br/>
<anchor title="虫子">虫子
    <go href="/wgamepk/lgj/pkRightDeal1.jsp" method="post">
	<postfield name="action" value="d"/>
	<postfield name="pkId" value="<%=lgj.getId()%>"/>
    </go>
</anchor><br/>
<%
}
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamepk/lgj/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>