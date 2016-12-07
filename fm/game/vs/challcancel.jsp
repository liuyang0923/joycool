<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
FamilyUserBean fmUser = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
VsBean vsBean=VsAction.getFmVsInfo(fmUser.getFm_id());
if(!FamilyUserBean.isflag_game(fmUser.getFm_flags())){
%>操作失败,您不是游戏管理员或族长没有权限.<br/><%
}else if(vsBean.getGameid()==vsBean.getChallengeId()&&vsBean.getGameid()!=0){
%>操作失败,该家族已接受了您的挑战邀请.<br/><%
}else if(!vsBean.isChallenge()){
%>操作失败,该挑战申请已被取消.<br/><%
}else if(vsBean.getChallTime()!=0&&vsBean.getChallTime()+Challenge.dealTime<System.currentTimeMillis()){
action.deleteChallenge(vsBean);
%>取消挑战申请成功.<br/><%
}else if(vsBean.getChallTime()+60*1000>System.currentTimeMillis()){
%>操作失败,发消息时间不足一分钟,不能取消<br/><%
}else{
action.cancelchall(fmUser,vsBean);
%><%=action.getTip()%><br/><%
}
%><a href="vsgame.jsp?id=<%=fmUser.getFm_id()%>">返回家族挑战</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>