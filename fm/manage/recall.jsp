<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.UserBean,net.joycool.wap.util.UserInfoUtil,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int userid=action.getParameterInt("uid");
FamilyUserBean fmUser=action.getFmUserByID(userid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
String cmd=action.getParameterString("cmd");
if(fmUser!=null||fmUser.getFm_id()!=fmLoginUser.getFm_id()){
	if("y".equals(cmd)){
		if(action.recall(fmLoginUser.getFm_id(),userid)){
			%><wml><card title="罢免成功" ontimer="<%=response.encodeURL("power.jsp?")%>"><timer value="30" /><p align="left">
			罢免成功,已收回<%=fmUser.getNickNameWml()%>的称号和管理权限.(3秒后自动返回任命权限页面)<br/>
			<a href="power.jsp">直接返回</a><br/><%
		}else{
			%><wml><card title="罢免失败" ><p align="left">
			<%=action.getTip()%><br/>
			<a href="power.jsp">直接返回</a><br/><%
		}
	}else{
		%><wml><card title="罢免" ><p align="left">
		您确认要将<%=fmUser.getNickNameWml()%>的称号和管理权限收回吗?<br/>
		<a href="recall.jsp?uid=<%=userid%>&amp;cmd=y">确认罢免</a><br/>
		<a href="powermgt.jsp?uid=<%=userid%>">返回</a><br/><%
	}
}else{
	%>不是本家族成员<br/>
	<a href="power.jsp">直接返回</a><br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>