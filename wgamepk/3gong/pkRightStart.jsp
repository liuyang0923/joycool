<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
Gong3Action action = new Gong3Action(request);
action.pkRightStart(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
WGamePKBean gong3 = (WGamePKBean) request.getAttribute("gong3");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="应战">
<p align="center">应战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//gong3为空
if(gong3 == null){
%>
该局已结束!您现在还有<%=us.getGamePoint()%>个乐币<br/>
<%
} else {
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(gong3.getLeftUserId());
%>
<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(gong3.getLeftNickname())%>向您挑战,赌注是<%=gong3.getWager()%>个乐币(如果当前对手和您的乐币数比该赌注少,以你们少的一方的乐币数为准)<br/>
<a href="/wgamepk/3gong/pkRightDeal1.jsp?pkId=<%=gong3.getId()%>">应战(逃跑不扣币)</a><br/>
<%
}
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamepk/3gong/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>