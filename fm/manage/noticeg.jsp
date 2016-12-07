<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.List,jc.family.*"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
String notice=action.getParameterString("notice");
List list=action.getFmUserIdList(fmLoginUser.getFm_id());
%><wml><card title="家族通知"><p align="left">
<select multiple="true"  name="glist" ><%
for(int i=0;i<list.size();i++){
	Integer userid=(Integer)list.get(i);
	FamilyUserBean fmUsers=action.getFmUserByID(userid.intValue());
	if(fmUsers!=null&&fmUsers.isOnline()&&fmUsers.getId()!=fmLoginUser.getId()){
	%><option  value="<%=fmUsers.getId()%>"><%=fmUsers.getNickNameWml()%></option><%	
	}
}
%></select><br/>
<anchor>提交发送
<go href="renotice.jsp?cmd=g" method="post">
<postfield name="glist" value="$glist"/>
<postfield name="notice" value="<%=notice%>"/>
</go>
</anchor><br/>
<a href="management.jsp">返回家族管理</a><br/>
<a href="/fm/myfamily.jsp">返回我的家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>