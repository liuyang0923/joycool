<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%
if(session.getAttribute("buyHomeRefrush")==null){
//response.sendRedirect(("/home/buyHome.jsp"));
BaseAction.sendRedirect("/home/buyHome.jsp", response);
return;
}
session.removeAttribute("buyHomeRefrush");
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.buyHomeResult(request);
String tip = (String) request.getAttribute("tip");
String result = (String) request.getAttribute("result");
String buy = (String) request.getAttribute("buy");
String url=("/home/buyHome.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后跳转)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("goodsfailure")){%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/home/changeGoods.jsp">清理房间</a><br/>
<a href="/home/buyHomeResult.jsp?buy=<%=buy%>&amp;goods=1">确认扔掉</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if (result.equals("kufailure")){%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/home/myStore.jsp">清理仓库</a><br/>
<a href="/home/buyHomeResult.jsp?buy=<%=buy%>&amp;ku=1">确认扔掉</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{ %>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/home/home.jsp">返回家园首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%} %>
</wml>