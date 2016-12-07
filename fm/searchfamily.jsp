<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction fmAction=new FamilyAction(request,response);
String fmname =fmAction.getParameterNoEnter("familyname");
boolean result=false;
List list=null;
String tip=null;
if(fmname==null||"".equals(fmname.replace(" ", ""))){
tip="请输入家族名称!";
}else{
list=fmAction.service.selectsearchFamilyList(StringUtil.toSqlLike(fmname),0,20);
if(list==null||list.size()==0){
tip="没有搜索到符合条件的家族!";
}else{
result=true;
}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(!result){%>
<card title="家族首页" ontimer="<%=response.encodeURL("index.jsp")%>"><timer value="30"/><p align="left">
<%=tip%>(3秒后跳转家族首页)<br/>
<%}else{%>
<card title="家族"><p align="left">
搜索关键字:<%=StringUtil.toWml(fmname)%><br/>
共搜索到<%=list.size()%>个<br/><%
for(int i=0;i<list.size();i++){
FamilyHomeBean home=(FamilyHomeBean)list.get(i);
%><%=i+1%>.<a href="myfamily.jsp?id=<%=home.getId()%>"><%=StringUtil.toWml(home.getFm_name())%></a>(<%=home.getFm_member_num()%>人)<br/>
<%}
}%>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>