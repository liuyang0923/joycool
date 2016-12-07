<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
int id=action.getParameterInt("id");
if(id==0){
	FamilyUserBean fmLoginUser=action.getFmUser();
	if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
		response.sendRedirect("/fm/index.jsp");return;
	}else{
		id=fmLoginUser.getFm_id();
	}
}
FamilyHomeBean fmhome=FamilyAction.getFmByID(id);
String cmd=request.getParameter("cmd");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族等级"><p align="left"><%=BaseAction.getTop(request, response)%><%if(cmd==null||!cmd.equals("see")){%>
您的家族当前等级为<%=jc.family.Constants.FM_LEVEL_NAME[fmhome.getFm_level()]%>,可容纳人数为<%=fmhome.getMaxMember()%>.<br/><%
if(fmhome.getFm_level()<5){%>
升级后容纳人数增加:<%=Constants.FM_LEVEL[fmhome.getFm_level()+1]-Constants.FM_LEVEL[fmhome.getFm_level()]%><br/><%
}%>
<a href="nextlevel.jsp">提升到下一级</a><br/>
<a href="management.jsp">返回家族管理</a><br/><%
}else{%>
家族当前等级为<%=jc.family.Constants.FM_LEVEL_NAME[fmhome.getFm_level()]%>,共能容纳族人<%=fmhome.getMaxMember()%>个!<br/>
<%}%>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>