<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*,java.util.List,net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*"%><%
FamilyAction action=new FamilyAction(request,response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="申请建立家族"><p align="left"><%
int userid=action.getParameterInt("userId");
int applyId=action.getParameterInt("applyId");
if(userid==0||applyId==0){
%>页面过期<br/><%
}else{
UserBean userbean=UserInfoUtil.getUser(userid);
if(userbean==null){
%>页面过期<br/><%
}
FmApply fmApply=action.service.selectFmApplybyId(applyId);
if(fmApply==null){
%>页面过期<br/><%
}
UserStatusBean status =(UserStatusBean)UserInfoUtil.getUserStatus(userbean.getId());
UserBean userbean1=action.getLoginUser();
UserStatusBean status2 =(UserStatusBean)UserInfoUtil.getUserStatus(userbean1.getId());
String cmd=request.getParameter("cmd");
if(cmd!=null&&status2.getGamePoint()<action.applyMoney){
%>您的乐币不够!不能再继续邀请!<br/><%
}else{
int fmId=action.getFmId(userid);
net.joycool.wap.service.infc.IUserService userService = ServiceFactory.createUserService();
boolean isABadGuys =userService.isUserBadGuy(userid,action.getLoginUser().getId());
FmApplyUser fmApplyUser=action.service.selectFamilyApplyUser(userid);
FmApplyUser fmApplyUser2=action.service.selectFamilyApplyUser(userid,applyId);
if(cmd==null&&fmApply.getSend_total()>9){
%>您已经邀请了10位好友了,是否花费1千万乐币继续邀请!<br/>
<a href="fmbuildapply.jsp?cmd=y&#38;applyId=<%=applyId%>&#38;userId=<%=userid%>">确定</a><br/><%
}else{
if(status.getRank()<jc.family.Constants.MIN_RANK_FOR_JOIN){%>
该好友不到3级,不能协助你创建家族!<br/><%
}else if(fmId!=0){%>
该好友已经加入了其他家族/帮会,不能邀请!<br/><%
}else if(isABadGuys){%>
该好友把你列入了黑名单,不能邀请!<br/><%
}else if(fmApplyUser!=null&&!fmApplyUser.is_time(System.currentTimeMillis())){%>
已有别的人在一分钟之内邀请了该好友,不能再次邀请!<br/><%
}else if(fmApplyUser2!=null&&fmApplyUser2.getIs_apply()==1){%>
该好友已经同意了您共同创建<%=fmApply.getFm_nameToWml()%>家族的邀请,不能再重复邀请!<br/><%
}else{
action.disposeFmHomeapply(userbean1,fmApply,userid,status2.getGamePoint());
response.sendRedirect("buildfail.jsp?cmd=1&applyId="+applyId);return;
}%><a href="friendlist.jsp?applyId=<%=applyId%>">继续邀请</a><br/><%
}
}
}%>
<a href="buildfail.jsp?applyId=<%=applyId%>">返回我的家族创建</a><br/>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>