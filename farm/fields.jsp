<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.fields();
List fields = (List)request.getAttribute("fields");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源农场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
for(int i=0;i<fields.size();i++){
FarmFieldBean field = (FarmFieldBean)fields.get(i);
if(field.getCropId()>0) {
FarmCropBean crop = action.world.getCrop(field.getCropId());
%>
<a href="field.jsp?id=<%=field.getId()%>"><%=crop.getName()%></a><br/>
<%}else{%>
<a href="field.jsp?id=<%=field.getId()%>">空地</a><br/>
<%}%>
<%}%>
<br/>
<a href="afield.jsp">开垦一块耕地</a><br/><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>