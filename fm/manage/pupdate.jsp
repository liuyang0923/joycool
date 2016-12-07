<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int userid=action.getParameterInt("uid");
FamilyUserBean fmUser=action.getFmUserByID(userid);
FamilyHomeBean fm=FamilyAction.getFmByID(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(fmUser==null||fmUser.getFm_id()!=fm.getId()){
	%><card title="修改称号"><p align="left">
	族员和家族不相符,您不能对其修改称号!<br/>
	<a href="power.jsp">返回权限管理</a><br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}else if(!fmLoginUser.isflagAppoint()||fmUser.isflagAppoint()&&!fmLoginUser.isflagLeader()){	// 有任命权的人，只有族长可以改他的称号
	%><card title="修改称号" ontimer="<%=response.encodeURL("/fm/myfamily.jsp")%>"><timer value="30" /><p align="left">
	无法修改该成员的称号!(3秒后返回我的家族)<br/>
	<a href="/fm/myfamily.jsp">返回我的家族</a><br/><%
}else{
	if(request.getParameter("cmd")==null){ %>
		<card title="修改称号"><p align="left">
		<%=fmUser.getNickNameWml()%>-<%=net.joycool.wap.util.StringUtil.toWml(fmUser.getFm_name())%>:<br/>
		修改称号(最多4个字)<br/>
		<input name="fmname" maxlength="4"/>
		<anchor title="修改">修改
			<go href="pupdate.jsp?cmd=update&amp;uid=<%=fmUser.getId() %>" method="post">
				<postfield name="fmname" value="$(fmname)" />
		</go></anchor><br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>">返回</a><br/><%
	}else if("update".equals(request.getParameter("cmd"))){
		if(action.updateUserPosition(fm.getId())){
			%><card title="修改称号" ontimer="<%=response.encodeURL("powermgt.jsp?&amp;uid="+fmUser.getId())%>"><timer value="10" /><p align="left">
			<%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><%
		}else{
			%><card title="修改称号" ontimer="<%=response.encodeURL("pupdate.jsp?uid="+fmUser.getId())%>"><timer value="30" /><p align="left">
			<%=net.joycool.wap.util.StringUtil.toWml(action.getTip())%><%
		}
		%><br/>
		<a href="powermgt.jsp?uid=<%=fmUser.getId()%>">直接返回</a><br/><%
	}
}%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>