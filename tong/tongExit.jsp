<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><jsp:directive.page import="net.joycool.wap.bean.tong.TongBean"/>
<jsp:directive.page import="net.joycool.wap.util.StringUtil"/>
<%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongExit(request);
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="帮会" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/tong/tongList.jsp">帮会列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("tongError")){
TongBean tong =(TongBean)request.getAttribute("tong");
String url=("/tong/tong.jsp?tongId="+tong.getId());
%>
<card title="帮会" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转帮会首页)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong =(TongBean)request.getAttribute("tong");
String url=("/tong/tong.jsp?tongId="+tong.getId());
%>
<card title="帮会" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您已成功退出<%=StringUtil.toWml(tong.getTitle()) %>(3秒后跳转帮会首页)<br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">直接跳转</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>