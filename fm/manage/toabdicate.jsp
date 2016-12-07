<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil,net.joycool.wap.bean.UserBean,net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int userid=action.getParameterInt("uid");
String p="";
if(request.getParameter("p")!=null){
	p="?p="+request.getParameter("p");
}
FamilyUserBean fmUser=FamilyAction.getFmUserByID(userid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="族长让位"><p align="left"><%
if(fmUser==null||fmUser.getFm_id()!=fmLoginUser.getFm_id()){
	%>用户非本家族成员<br/><%
}else{
	%>您确认要将族长让位给:<%=fmUser.getNickNameWml()%>?<br/>
	<a href="reabdicate.jsp?uid=<%=userid%>">确认让位</a><br/><%
}
%><a href="abdicate.jsp<%=p%>">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>