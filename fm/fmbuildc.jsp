<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*,java.util.List,net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*"%><%
FamilyAction action=new FamilyAction(request,response);
int applyId=action.getParameterInt("applyId");
if(applyId==0){
response.sendRedirect("index.jsp");return;
}
UserBean userBean=action.getLoginUser();
FmApplyUser fmAppUser=action.service.selectFamilyApplyUser(userBean.getId(),applyId);
if(fmAppUser==null){
response.sendRedirect("index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="申请建立家族"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(action.hasParam("c")){
	if(userBean.getId()==fmAppUser.getUserid()){
	action.service.upd("delete from fm_apply where id="+applyId);
	action.service.upd("delete from fm_apply_user where fm_apply_id="+applyId);
	%>取消成功~<br/><%
	}
}else{
	%>您是否要取消家族创建?<br/>
	<a href="fmbuildc.jsp?applyId=<%=applyId%>&amp;c=1">是</a>|<a href="buildfail.jsp?applyId=<%=userBean.getId()%>">否</a><br/><%
}
%><a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>