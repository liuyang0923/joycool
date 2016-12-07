<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction fmAction=new FamilyAction(request,response);
int id=fmAction.getParameterInt("fmid");
if(id==0){
response.sendRedirect("/fm/index.jsp");return;
}
int result=fmAction.getfamilyInviteResult();
String backTo=fmAction.getParameterString("backTo");
if(backTo==null||"".equals(backTo)){
backTo="/fm/index.jsp";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族邀请" ontimer="<%=response.encodeURL("/fm/myfamily.jsp?id="+id)%>"><timer value="30" /><p align="left"><%
if(result==0){
out.println(fmAction.getTip()+"<br/>");
}%>
<a href="<%=backTo.replace("&", "&amp;")%>">返回上一页</a><br/>
<a href="/fm/index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>