<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="jc.family.*"%><%
FamilyAction fmUserAction=new FamilyAction(request,response);
int applyId=fmUserAction.getParameterInt("applyId");
int result=fmUserAction.createFmHome(applyId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="建立家族"><p align="left"><%
if(result==2){
Integer fhid=(Integer)request.getAttribute("fhid");
String fhname=(String)request.getAttribute("fhname");
%>您的"<%=StringUtil.toWml(fhname)%>"家族已经成立!!恭喜您成为<%=StringUtil.toWml(fhname)%>家族首任族长!维护乐酷和平,构建和谐社会,就靠你啦!<br/>
<a href="management.jsp?id=<%=fhid.intValue()%>">管理家族</a><br/>
<a href="myfamily.jsp?id=<%=fhid.intValue()%>">我的家族</a><br/><%
}else if(result==1){%>
<%=fmUserAction.getTip()%>
请重新输入您想要的家族名字<br/>
<input name="fname" maxlength="6"/><br/>
<anchor title="改名">确定<go href="rebuild.jsp?applyId=<%=applyId%>" method="post">
<postfield name="fname" value="$(fname)" />
</go></anchor><br/>
<%}else{
%><%=fmUserAction.getTip()%><br/>
<a href="index.jsp">返回家族首页</a><br/><%
}%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>