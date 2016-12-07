<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%@ page import="jc.family.game.*,net.joycool.wap.util.UserInfoUtil"%><%
GameAction gameAction=new GameAction(request,response);
int mid=gameAction.getParameterInt("mid");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%if(request.getParameter("g")!=null){ %>
<wml><card title="游戏审核"><p align="left"><%=BaseAction.getTop(request, response)%>
<%
if(request.getParameter("g").equals("1")){%>
确定后报名情况同时也会清空!你确定不参加本轮游戏吗?<br/>
<a href="examine.jsp?mid=<%=mid %>&amp;g=2&amp;sub=1">确定</a><br/>
<a href="examine.jsp?mid=<%=mid %>">返回审核</a><br/>
<%}else if(request.getParameter("g").equals("2")){ int pass=gameAction.passGame(); if(pass==4||pass==5){%>
已报名成员列表<br/>
温馨提示:每进入游戏一个人,收取1000万乐币的报名费,从家族基金里扣除!<br/>
<%if(pass==4){ %>
本轮家族游戏已经设置为不参加状态!<br/>
<a href="examine.jsp?mid=<%=mid %>&amp;g=2&amp;sub=2">报名参加本次活动</a><br/>
<%}else if(pass==5){ %>
本轮家族游戏已经设置为参加状态!<br/>
<a href="examine.jsp?mid=<%=mid %>&amp;g=1">不参加本轮比赛</a><br/>
<%}}else if(pass==0){ %>
参数错误!<br/>
<%}else if(pass==1){ %>
不是家族人员!<br/>
<%}else if(pass==2){ %>
您没有权限!<br/>
<%}else if(pass==3){ %>
系统错误,稍后再试!<br/>
<%}else if(pass==5){ %>
比赛已经开始!不能再取消!<br/>
<%}}%>
<a href="fmgame.jsp">返回家族活动</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>
<%return;}%>
<wml><card title="游戏审核"><p align="left"><%=BaseAction.getTop(request, response)%>
已报名成员列表<br/>
温馨提示:每进入游戏一个人,收取1000万乐币的报名费,从家族基金里扣除!<br/>
<%
MatchBean smb = (MatchBean) GameAction.matchCache.get(new Integer(mid));
if(smb!=null){if(smb.getState()==2){%>
比赛结束后不能审批!<br/>
<%
}else{
boolean isApply=gameAction.isFmApply(mid);
if(isApply){
if(request.getParameter("cmd")!=null){gameAction.updateApply();}
List list=gameAction.getGameList(); 
if(list!=null){
if(list.size()>0){
for(int i=0;i<list.size();i++){
ApplyBean bean=(ApplyBean)list.get(i);
int p=0;String s="";
if(request.getParameter("p")!=null){p=gameAction.getParameterInt("p");s="&amp;p="+p;}%>
<%=i+1+p*10 %>.<%=UserInfoUtil.getUser(bean.getUid()).getNickNameWml()==null?"["+bean.getUid()+"]":UserInfoUtil.getUser(bean.getUid()).getNickNameWml() %>:<%if(bean.getState()==0){ %><a href="examine.jsp?mid=<%=mid %>&amp;cmd=2&amp;uid=<%=bean.getUid()+s %>">通过</a>&#160;<a href="examine.jsp?mid=<%=mid %>&amp;cmd=1&amp;uid=<%=bean.getUid()+s %>">不通过</a><%}else if(bean.getState()==1){%><a href="examine.jsp?mid=<%=mid %>&amp;cmd=2&amp;uid=<%=bean.getUid()+s %>">通过</a>不通过<%}else if(bean.getState()==2){%>通过<a href="examine.jsp?mid=<%=mid %>&amp;cmd=1&amp;uid=<%=bean.getUid()+s %>">不通过</a> <%} %><br/>
<%}}}else{%>暂时无人报名!<br/><%}%>
<%if(request.getAttribute("applyList")!=null&&!"".equals(request.getAttribute("applyList"))){%>
<%=request.getAttribute("applyList") %><%}%> 
<%int sure=gameAction.sureApplyGame();if(sure==1){ %>
<a href="examine.jsp?mid=<%=mid %>&amp;g=1">不参加本轮比赛</a><br/>
<%}else if(sure==2){%>
<a href="examine.jsp?mid=<%=mid %>&amp;g=2&amp;sub=2">报名参加本次活动</a><br/>
<%}%><%}else{ %>
您的家族在比赛开始前,未有通过审批成功参加该赛事的人,所以现在您不能就该比赛进行审批!<br/>
<%}}}%>
<a href="fmgame.jsp">返回家族活动</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>