<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
int myfmId=action.getFmId();
int fmId=action.getParameterInt("fmId");
if(fmId==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fmHome=FamilyAction.getFmByID(fmId);
if(fmHome==null){
response.sendRedirect("/fm/index.jsp");return;
}
VsBean vsBean =action.getFmVsInfo(myfmId);
if(vsBean.getChallTime()!=0&&vsBean.getGameid()!=vsBean.getChallengeId()&&vsBean.getChallTime()+Challenge.dealTime<System.currentTimeMillis()){
action.deleteChallenge(vsBean);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmId==myfmId){
%>操作失败,不可以给自己家族发起挑战<br/><%/**
}else if(fmHome.getFm_member_num()<VsBean.minNumber){
%>操作失败,对方家族成员人数不足<%=VsBean.minNumber%>人,无法进行家族挑战.<br/><%*/
}else if(vsBean.getGameid()!=0){
%>操作失败,您正在与其它家族进行家族挑战中.<br/><%
}else if(vsBean.getChallengeId()!=0){
%>操作失败,您必须取消现有的挑战才可以再次发起.<br/><%
}else{
%>您选择向<%=fmHome.getFm_nameWml()%>家族发起挑战:<br/>
请选择要挑战的游戏:<select name="gameId"><%
for(int i=0;i<VsGameBean.getMaxGameId();i++){
VsConfig vsConfig=VsGameBean.getVsConfig(i);
if(vsConfig!=null&&vsConfig.getHide()==0){
%><option value="<%=i%>"><%=VsGameBean.getGameIdName(i)%></option><%
}
}%>
</select><br/>
请输入挑战金:(<%=fmHome.isAlly(myfmId)?'0':'1'%>-9)<input name="prize" maxlength="1" format="*N"/>亿<br/>
<anchor title="OK">确定
<go href="confirmchallenge.jsp" method="post">
    <postfield name="prize" value="$(prize)"/>
    <postfield name="fmId" value="<%=fmId%>"/>
    <postfield name="gameId" value="$(gameId)"/>
  </go>
</anchor><br/><%
}%><a href="vsgame.jsp?id=<%=myfmId%>">返回家族挑战</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>