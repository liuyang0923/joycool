<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FootBallAction action = new FootBallAction(request);
action.pkOut(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean us = action.getUserStatus();
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String bkId=(String) request.getAttribute("bkId");
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="挑战">
<p align="center">挑战</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//不够庄家的乐币数币
if("failure".equals(result)){
%>
<%=tip%><br/>
<a href="/wgamepk/football/index.jsp">返回房间</a><br/>
<%
} else if("success".equals(result)){
%>
请选择守门方向:<br/>
<img src="/wgamepk/football/img/shoumen.gif" alt="球门"/><br/>
<a href="/wgamepk/football/chlStart.jsp?action=l&amp;bkId=<%=bkId%>">左</a>&nbsp;
<a href="/wgamepk/football/chlStart.jsp?action=m&amp;bkId=<%=bkId%>">中</a>&nbsp;
<a href="/wgamepk/football/chlStart.jsp?action=r&amp;bkId=<%=bkId%>">右</a><br/>
<%
} 
%><br/>
<a href="/wgamepk/football/index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>