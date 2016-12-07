<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
	response.sendRedirect("/fm/index.jsp");
	return;
}
FamilyHomeBean fmhome=FamilyAction.getFmByID(fmLoginUser.getFm_id());
String cmd=request.getParameter("cmd");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族等级"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(cmd!=null&&cmd.equals("next")){
	%><%=action.updateLevel(fmLoginUser.getFm_id())%><br/>
	<a href="management.jsp">返回家族管理</a><br/><%
}else{
	%>确定用<%=Constants.FM_LEVEL_MONEY[fmhome.getFm_level()>5?5:fmhome.getFm_level()]%>乐币提升家族等级吗?<br/>
	<a href="nextlevel.jsp?cmd=next">确定</a><br/>
	<a href="level.jsp">返回</a><br/><%
}
%>&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>