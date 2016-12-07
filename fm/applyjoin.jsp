<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction fmAction=new FamilyAction(request,response);
int id=fmAction.getParameterInt("id");
if(id==0){
response.sendRedirect("index.jsp");return;
}
fmAction.applyJoin();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%FamilyHomeBean family =FamilyAction.getFmByID(id);
if(family!=null){%>
<card title="提示" ontimer="<%=response.encodeURL("myfamily.jsp?id="+id)%>" ><timer value="30"/><p align="left">
<%=net.joycool.wap.util.StringUtil.toWml(fmAction.getTip())%><br/>
<a href="myfamily.jsp?id=<%=id%>">返回<%=family.getFm_nameWml()%>家族</a><br/>
<%}else{%>
<card title="提示" ><p align="left">
<%=net.joycool.wap.util.StringUtil.toWml(fmAction.getTip())%><br/>
<a href="index.jsp">返回家族首页</a><br/><%}%>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>