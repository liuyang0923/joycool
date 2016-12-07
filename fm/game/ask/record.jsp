<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,jc.family.game.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.ask.*"%><%@ page import="jc.family.*"%><%
AskAction action=new AskAction(request,response);
List list=action.getRecord();
GameHistoryBean myGameBean=(GameHistoryBean)request.getAttribute("myGameBean");
MemberBean myMemberBean=(MemberBean)request.getAttribute("myMemberBean");
String backTo=request.getParameter("backto");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族问答"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(myGameBean!=null){%>
<%=myGameBean.getHoldTimeToString()%><br/>
本家族答题积分:<%=myGameBean.getShipId()%><br/>
参与人数:<%=myGameBean.getNumTotal()%><br/>
排名:<%=myGameBean.getRank()%><br/>
>>家族获得<br/>
排名积分:<%=myGameBean.getScore()%><br/>
奖金:<%=myGameBean.getPrize()%><br/>
家族经验:<%=myGameBean.getGamePoint()%><br/>
>>个人获得<br/>
个人功勋:<%=myMemberBean==null?"0":myMemberBean.getContribution()+""%><br/>
成员|答对|答错|得分<br/><%
for(int i=0;i<list.size();i++){
MemberBean bean=(MemberBean)list.get(i);
FamilyUserBean fmUser=action.getFmUserByID(bean.getUid());
%>
<a href="/user/ViewUserInfo.do?userId=<%=bean.getUid()%>"><%=fmUser==null?"":fmUser.getNickNameWml()%></a>|<%=bean.getAsk_right()%>|<%=bean.getAsk_wrong()%>|<%=bean.getAsk_score()%><br/>
<%}%><%=request.getAttribute("paging")
%><%if(backTo!=null&&!backTo.equals("")){%>
<a href="<%=backTo%>">返回家族活动记录</a><br/><%}else{%>
<a href="historyDetails.jsp?mid=<%=myGameBean==null?"0":myGameBean.getMid()+""%>">查看本轮排名</a><br/><%
}}else{%>
本场游戏家族未参赛<br/>
<%}%>
<a href="index.jsp">返回家族问答</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>