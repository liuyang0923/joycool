<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
int chall=action.getParameterInt("chall");
FamilyUserBean fmUser = action.getFmUser();
if(chall==0){
response.sendRedirect("vsgame.jsp?id="+fmUser.getFm_id());return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
int cmd=action.getParameterInt("cmd");
Challenge challenge=action.vsService.getChallenge("id="+chall);
if(challenge==null){
%>操作失败,该挑战信息已过期.<br/><%
}else if(cmd==0){
%>对战家族:<%=challenge.getFmANameWml()%><br/>
挑战游戏:<%=challenge.getChallGameIdName()%><br/>
挑战金:<%=challenge.getWager()%>亿<br/>
<a href="challengedo.jsp?chall=<%=chall%>&amp;cmd=1">接受挑战</a><br/>
<a href="challengeList.jsp">查看家族挑战邀请</a><br/>
<%
}else{
VsConfig vsConfig=VsGameBean.getVsConfig(challenge.getGameId());
if(vsConfig==null){
response.sendRedirect("vsgame.jsp?id="+fmUser.getFm_id());return;
}
FamilyHomeBean fmHome=FamilyAction.getFmByID(challenge.getFmA());
VsBean myVsBean=action.getFmVsInfo(challenge.getFmB());
VsBean vsBean=action.getFmVsInfo(challenge.getFmA());
FamilyHomeBean myFmHome=FamilyAction.getFmByID(fmUser.getFm_id());
int c=action.getParameterInt("c");
if(vsBean.getChallTime()!=0&&challenge.getCTime()+Challenge.dealTime<System.currentTimeMillis()){
action.deleteChallenge(vsBean);
%>操作失败,该挑战信息已过期.<br/><%
}else if(myVsBean.getGameid()!=0){
%>操作失败,您正在游戏中.<br/><%
}else if(vsBean.getGameid()!=0){
%>操作失败,对方正在游戏中.<br/><%
}else if(!FamilyUserBean.isflag_game(fmUser.getFm_flags())){
%>操作失败,游戏管理员才有该权限.<br/><%
}else if(myFmHome.getMoney()/VsAction.e<challenge.getWager()){
%>操作失败,本家族基金不足以支付挑战金.<br/><%
}else if(fmHome.getMoney()/VsAction.e<challenge.getWager()){
%>操作失败,对方家族基金不足以支付挑战金.<br/><%
}else if(myVsBean.getGameid()!=0){
%>操作失败,本家族正在与其它家族进行家族挑战中.<br/><%
}else if(vsBean.getGameid()!=0){
%>操作失败,对方家族正在与其它家族进行家族挑战中.<br/><%/**
}else if(myVsBean.getTime()>=VsBean.maxGameTime){
%>操作失败,本家族今日已进行<%VsBean.maxGameTime%>次家族挑战,无法进行家族挑战.<br/><%
}else if(vsBean.getTime()>=VsBean.maxGameTime){
%>操作失败,对方家族今日已进行<%=VsBean.maxGameTime%>次家族挑战,无法进行家族挑战.<br/><%*/
}else if(myVsBean.getGameTime()[vsConfig.getId()]>=vsConfig.getMatchCount()){
%>操作失败,本家族今日已进行<%=myVsBean.getGameTime()[vsConfig.getId()]%>次<%=vsConfig.getName()%>,无法进行家族挑战.<br/><%
}else if(vsBean.getGameTime()[vsConfig.getId()]>=vsConfig.getMatchCount()){
%>操作失败,对方家族今日已进行<%=vsBean.getGameTime()[vsConfig.getId()]%>次<%=vsConfig.getName()%>,无法进行家族挑战.<br/><%
}else if(!action.isTime()){
%>操作失败,每日凌晨0点-8点不能发起挑战申请.<br/><%
}else if(myFmHome.getFm_member_num()<vsConfig.getMinPlayer()){
%>操作失败,本家族成员人数不足<%=vsConfig.getMinPlayer()%>人,无法进行家族挑战.<br/><%
}else if(fmHome.getFm_member_num()<vsConfig.getMinPlayer()){
%>操作失败,对方家族成员人数不足<%=vsConfig.getMinPlayer()%>人,无法进行家族挑战.<br/><%
}else if(myVsBean.getGameTime()[vsConfig.getId()]>=vsConfig.getMatchScoreCount()&&c==0){
%>本家族本日<%=vsConfig.getName()%>次数已达<%=myVsBean.getGameTime()[vsConfig.getId()]%>次,继续挑战双方将不会获得挑战积分,是否接受挑战?<br/>
<a href="challengedo.jsp?chall=<%=chall%>&amp;cmd=1&amp;c=1">继续挑战</a><br/><%
}else if(vsBean.getGameTime()[vsConfig.getId()]>=vsConfig.getMatchScoreCount()&&c==0){
%>对方家族本日<%=vsConfig.getName()%>次数已达<%=vsBean.getGameTime()[vsConfig.getId()]%>次,继续挑战双方将不会获得挑战积分,是否接受挑战?<br/>
<a href="challengedo.jsp?chall=<%=chall%>&amp;cmd=1&amp;c=1">继续挑战</a><br/><%
}else{
if(action.agreeChallenge(challenge)){
%>接受挑战成功,已扣除<%=challenge.getWager()%>亿挑战金,请进入游戏开始挑战.<br/><%
}else{
%>创建游戏失败,请联系管理员<br/><%
}
}
}%>
<a href="vsgame.jsp?id=<%=fmUser.getFm_id()%>">返回家族挑战</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>