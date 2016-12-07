<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.UserBean,jc.family.*,java.util.*,net.joycool.wap.bean.PagingBean"%><%
FamilyAction action=new FamilyAction(request,response);
int applyId=action.getParameterInt("applyId");
UserBean userBean=action.getLoginUser();
if(applyId==0){
response.sendRedirect("index.jsp");return;
}
FmApplyUser fmAppUser=action.service.selectFamilyApplyUser(userBean.getId(),applyId);
if(fmAppUser==null){
response.sendRedirect("index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="申请建立家族"><p align="left"><%=BaseAction.getTop(request, response)%><%
FmApply fmApply=action.service.selectFmApplybyId(applyId);
FamilyUserBean fmUser=action.getFmUser();
if(fmApply==null||(fmUser!=null&&fmUser.getFm_id()!=0)){
%>页面已经过期<br/><%
}else{
if(action.getParameterInt("cmd")==1){%>
邀请发送成功!<br/><%
}
if(userBean.getId()!=fmApply.getId()&&fmAppUser.getIs_apply()==0){
%><%=fmApply.getApply_nameToWml()%>邀请您和他一起创建家族&#60;<%=fmApply.getFm_nameToWml()%>&#62;!<br/>
<a href="fmbuildapplymes.jsp?cmd=1&#38;applyId=<%=applyId%>">同意</a>&#160;<a href="fmbuildapplymes.jsp?cmd=2&#38;applyId=<%=applyId%>">拒绝</a><br/><%
}if(userBean.getId()!=fmApply.getId()&&fmAppUser.getIs_apply()==1){
%>欢迎您参与到创建&#60;<%=fmApply.getFm_nameToWml()%>&#62;家族的队伍中,一起加油共同努力!<br/><%
}%>
————————<br/>
家族申请状态<br/>
被邀请人|状态<br/><%
int c=action.service.selectIntResult("select count(distinct userid) from fm_apply_user where fm_apply_id="+applyId);
PagingBean paging=new PagingBean(action, c, 10, "p");
List list=action.service.selectFamilyApplyUserList(applyId,paging.getStartIndex(),paging.getCountPerPage());
for(int i=0;i<list.size();i++){
FmApplyUser fmApplyUser=(FmApplyUser)list.get(i);
%><a href="/user/ViewUserInfo.do?userId=<%=fmApplyUser.getUserid()%>"><%=fmApplyUser.getNickNameWml()%></a>|<%=fmApply.getId()==fmApplyUser.getUserid()?"发起人":fmApplyUser.getIs_apply_String()%><br/><%
}%><%=paging.shuzifenye("buildfail.jsp?applyId="+applyId, true, "|", response)%><%
if(userBean.getId()!=fmApply.getId()&&fmAppUser.getIs_apply()==1){%>
<a href="fmbuildapplymes.jsp?cmd=3&#38;applyId=<%=applyId%>">取消参与该家族的共同创建</a><br/><%
}if(userBean.getId()==fmApply.getId()){
if(action.service.selectcheckedNumber(applyId)){%>
<a href="rebuild.jsp?applyId=<%=applyId%>">成立家族</a><br/><%
}%><a href="friendlist.jsp?applyId=<%=applyId%>">继续邀请</a><br/>x<a href="fmbuildc.jsp?applyId=<%=applyId%>">取消创建</a>x<br/><%
}
}%>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>