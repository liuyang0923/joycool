<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.LoadResource,net.joycool.wap.util.DateUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
int fmId=action.getFmId();
FamilyUserBean fmUser = action.getFmUser();
int id=action.getParameterInt("id");
if(id==0){
	if(fmId>0){
		id=fmId;
	}else{
		response.sendRedirect("index.jsp");
		return;
	}
}
FamilyHomeBean fm=action.getFmByID(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族勋章"><p align="left"><%=BaseAction.getTop(request, response)%>
【<%=fm.getFm_nameWml()%>】勋章<br/>
<%
List medalList = fm.getMedalList();
if(medalList.size()>0){
%><%for(int i = 0;i < medalList.size();i++){
FamilyMedalBean medal = (FamilyMedalBean)medalList.get(i);
%><img src="/rep/family/medal/<%=medal.getImg()%>" alt="<%=medal.getName()%>"/><%=medal.getInfo()%>(<%=DateUtil.formatDate1(new java.util.Date(medal.getCreateTime()))%>)<br/><%
}%><%}%>
<br/>
&lt;<a href="myfamily.jsp?id=<%=id%>">返回家族</a>&lt;<a href="index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>