<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%
response.setHeader("Cache-Control","no-cache");
FightAction action = new FightAction(request);
action.chlStart(request);
String result = (String) request.getAttribute("result");
String tip = (String) request.getAttribute("tip");
WGameFightBean fight = (WGameFightBean)request.getAttribute("fight");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String leftStr[] =null;
String rightStr[] =null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="挑战">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//下注有问题
if("failure".equals(result)){
%>
<%=tip%><br/>
<%
} else if("success".equals(result)){
leftStr = fight.getLeftViewed().split(",");
rightStr = fight.getRightViewed().split(",");
%>
我的招数<br/>
<%for(int i=0;i<leftStr.length;i++){%>
<%=i+1%>:<%=action.getStringName(StringUtil.toInt(rightStr[i]))%>
<%}%><br/>
<%int rResult=StringUtil.toInt((String)request.getAttribute("rResult"));if(rResult==0){%>
您的招数过人，您赢了<%=fight.getWager()%>个乐币
<%}else if(rResult==2){%>
您和庄家打平
<%}else{%>
您的招数不灵，您输了<%=fight.getWager()%>个乐币
<%}%><br/>
庄家：<a href="/user/ViewUserInfo.do?userId=<%= fight.getLeftUserId()%>"><%=StringUtil.toWml(fight.getLeftNickname())%></a><br/>
挑战者：您自己<br/>
<%}%>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamefight/index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛赌坊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>