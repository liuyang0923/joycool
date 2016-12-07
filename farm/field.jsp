<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.field();
FarmUserBean user = action.getUser();
FarmFieldBean field = (FarmFieldBean)request.getAttribute("field");
FarmCropBean crop = field.getCrop();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源农场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(crop!=null){%>
<%=crop.getName()%><br/>
<%if(field.canHarvest(action.now)){%>
<a href="act.jsp?a=2&amp;id=<%=field.getId()%>">收割</a><br/>
<%}else{%>
<%if(field.canAct(action.now)){%>
<a href="act.jsp?a=3&amp;id=<%=field.getId()%>">灌溉</a><br/>
<%}else{%>
灌溉(还需<%=DateUtil.formatTimeInterval(field.actTimeLeft(action.now))%>)<br/>
<%}%>
收割(还需<%=DateUtil.formatTimeInterval(field.harvestTimeLeft(action.now))%>)<br/>
<%}%>
<%}else{%>
<a href="plant.jsp?id=<%=field.getId()%>">种！</a><br/>
<%}%>
<a href="fields.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>