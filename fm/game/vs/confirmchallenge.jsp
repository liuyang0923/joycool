<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
FamilyUserBean fmUser = action.getFmUser();
int fmId=action.getParameterInt("fmId");
int prize=action.getParameterInt("prize");
int gameId=action.getParameterInt("gameId");
if(fmId==0||gameId==0){
response.sendRedirect("vsgame.jsp?id="+fmUser.getFm_id());return;
}
FamilyHomeBean fmHome=FamilyAction.getFmByID(fmId);
VsConfig vsConfig=VsGameBean.getVsConfig(gameId);
if(fmHome==null||vsConfig==null){
response.sendRedirect("vsgame.jsp?id="+fmUser.getFm_id());return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
int cmd=action.getParameterInt("cmd");
if(fmId==fmUser.getFm_id()){
%>操作失败,不可以给自己家族发起挑战<br/><%
}else if(prize>100||prize<0||prize==0&&!fmHome.isAlly(fmUser.getFm_id())){
%>操作失败,请重新设置挑战金额<br/>
<a href="dealchallenge.jsp?fmId=<%=fmId%>">返回发起挑战</a><br/><%
}else if(cmd==0){
%>您确认向<%=fmHome.getFm_nameWml()%>家族发起&#60;<%=VsGameBean.getGameIdName(gameId)%>&#62;挑战,挑战金为<%=prize%>亿.<br/>
<anchor title="OK">确认挑战
<go href="confirmchallenge.jsp?cmd=1" method="post">
    <postfield name="prize" value="<%=prize%>"/>
    <postfield name="fmId" value="<%=fmId%>"/>
    <postfield name="gameId" value="<%=gameId%>"/>
  </go></anchor><br/><%
}else{
VsBean myVsBean=action.getFmVsInfo(fmUser.getFm_id());
VsBean vsBean=action.getFmVsInfo(fmId);
FamilyHomeBean myFmHome=FamilyAction.getFmByID(fmUser.getFm_id());
int c=action.getParameterInt("c");
if(prize>=10||prize<0||prize==0&&!fmHome.isAlly(fmUser.getFm_id())){
%>操作失败,挑战金额只能是1到9的数字<br/>
<a href="dealchallenge.jsp?fmId=<%=fmId%>">返回发起挑战</a><br/><%
}else if(!FamilyUserBean.isflag_game(fmUser.getFm_flags())){
%>操作失败,游戏管理员才有该权限.<br/><%
}else if(myVsBean.getChallengeId()!=0){
%>操作失败,您必须取消现有的挑战才可以再次发起.<br/><%
}else if(myVsBean.getGameid()!=0){
%>操作失败,您正在游戏中.<br/><%
}else if(myFmHome.getMoney()/VsAction.e<prize){
%>操作失败,本家族基金不足以支付挑战金.<br/><%/**
}else if(fmHome.getMoney()/VsAction.e<prize){
%>操作失败,对方家族基金不足以支付挑战金.<br/><%*/
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
<a href="confirmchallenge.jsp?fmId=<%=fmId%>&amp;prize=<%=prize%>&amp;gameId=<%=gameId%>&amp;cmd=1&amp;c=1">继续挑战</a><br/><%
}else if(vsBean.getGameTime()[vsConfig.getId()]>=vsConfig.getMatchScoreCount()&&c==0){
%>对方家族本日<%=vsConfig.getName()%>次数已达<%=vsBean.getGameTime()[vsConfig.getId()]%>次,继续挑战双方将不会获得挑战积分,是否接受挑战?<br/>
<a href="confirmchallenge.jsp?fmId=<%=fmId%>&amp;prize=<%=prize%>&amp;gameId=<%=gameId%>&amp;cmd=1&amp;c=1">继续挑战</a><br/><%
}else{
action.startChallenge(fmId,prize,gameId);
%>操作成功,已向该家族发出挑战申请<br/><%
}
}
%>
<a href="vsgame.jsp?id=<%=fmUser.getFm_id()%>">返回家族挑战</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>