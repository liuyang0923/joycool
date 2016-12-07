<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.wgamepk.big.YlhykAction" %><%@ page import="net.joycool.wap.bean.wgame.big.WGamePKBigBean" %><%
response.setHeader("Cache-Control","no-cache");
YlhykAction action = new YlhykAction(request);
action.pkOut(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="挑战">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//不够庄家的乐币数币
if("failure".equals(result)){
%>
<%=tip%><br/>
<a href="index.jsp">返回房间</a><br/>
<%
} else if("success".equals(result)){
WGamePKBigBean ylhyk =(WGamePKBigBean) request.getAttribute("ylhyk");
%>
<%=StringUtil.toWml(ylhyk.getContent())%><br/>
庄家:<%=StringUtil.toWml(ylhyk.getLeftNickname())%><br/>
金额:<%=action.bigNumberFormat(ylhyk.getWager())%><br/>
要乐还是要酷？<br/>
选择：<a href="chlStart.jsp?bkId=<%=ylhyk.getId()%>&amp;result=0">乐</a><br/>
选择：<a href="chlStart.jsp?bkId=<%=ylhyk.getId()%>&amp;result=1">酷</a><br/>
<%}%><br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="/wgame/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>