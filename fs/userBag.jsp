<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.userBag();
String result =(String)request.getAttribute("result");
String url=("/fs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=FSAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/fs/index.jsp">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
FSUserBean fsUser = action.getFsUser();
%>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
房屋中介<br/>
中介非常诚恳地看着你。<br/>
现金：<%=fsUser.getMoney()%>元<br/>
存款：<%=fsUser.getSaving()%>元<br/>
房屋容量:<%=fsUser.getUserBag()%>个<br/>
<a href="/fs/userBagResult.jsp">租个大一点的房子</a><br/>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>