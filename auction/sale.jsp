<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction"%><%
response.setHeader("Cache-Control","no-cache");
AuctionAction action = new AuctionAction(request);
action.sale(request);
String result =(String)request.getAttribute("result");
String url=("/user/userBag.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="拍卖结果页面" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转我的行囊!)<br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
out.clearBuffer();
response.sendRedirect("/user/userBag.jsp");
return;
}else{%>
<card title="拍卖结果页面" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
拍卖成功(3秒后跳转我的行囊!)<br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>