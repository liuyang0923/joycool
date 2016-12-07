<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*,java.util.List,net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.action.*,net.joycool.wap.bean.*"%><%
FamilyAction action=new FamilyAction(request,response);
int applyId=action.getParameterInt("applyId");
int cmd=action.getParameterInt("cmd");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="申请建立家族"><p align="left"><%
if(cmd==0||applyId==0){
%>页面失效<br/><%
}else{
UserBean userbean=action.getLoginUser();
FmApply fmApply=action.service.selectFmApplybyId(applyId);
FmApplyUser fmAppUser=action.service.selectFamilyApplyUser(userbean.getId(),applyId);
if(fmApply==null||fmAppUser==null){
%>页面失效<br/><%
}else{
if(cmd==1&&userbean.getId()==fmAppUser.getUserid()){
action.service.updateFamilyApplyUser(fmApply.getId(),userbean.getId(),1);
NoticeAction.sendNotice(fmApply.getId(),userbean.getNickName()+"同意和你一起共同创建"+fmApply.getFm_name(),
NoticeBean.GENERAL_NOTICE, "/fm/buildfail.jsp?applyId="+fmApply.getId());
response.sendRedirect("buildfail.jsp?applyId="+fmApply.getId());return;
}
if(cmd==2&&userbean.getId()==fmAppUser.getUserid()){
action.service.updateFamilyApplyUser(fmApply.getId(),userbean.getId(),2);
NoticeAction.sendNotice(fmApply.getId(),userbean.getNickName()+"拒绝了和你一起共同创建"+fmApply.getFm_name(),
NoticeBean.GENERAL_NOTICE, "/fm/buildfail.jsp?applyId="+fmApply.getId());%>
您拒绝了<%=fmApply.getApply_name()%>共同创建&#60;<%=fmApply.getFm_nameToWml()%>&#62;的邀请!<br/><%
}if(cmd==3&&userbean.getId()==fmAppUser.getUserid()){
action.service.updateFamilyApplyUser(fmApply.getId(),userbean.getId(),2);%>
您取消了<%=fmApply.getApply_name()%>共同创建&#60;<%=fmApply.getFm_nameToWml()%>&#62;的邀请!<br/><%
}
}
}
%>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>