<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.buyBarrenResult(request);
String result=(String)request.getAttribute("result");
String tongId=(String)request.getAttribute("tongId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("success")){%>
<card title="买荒城结果" ontimer="<%=response.encodeURL("/tong/tong.jsp?tongId="+tongId)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/tong/tong.jsp?tongId=<%=tongId%>">直接跳转 </a> <br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
 
<%}else if(result.equals("failure")){%>
<card title="买荒城结果">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/tong/buyBarren.jsp?tongId=<%=tongId%>">返回购买</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tongId%>">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="买荒城信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您要购买的荒城不存在！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>