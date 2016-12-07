<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction"%><%
response.setHeader("Cache-Control","no-cache");
AuctionAction action = new AuctionAction(request);
action.buyResult(request);
int userBagId = action.getAttributeInt("userBagId");
String result =(String)request.getAttribute("result");
String url=("auctionHall.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="购买物品" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转拍卖大厅!)<br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
out.clearBuffer();
response.sendRedirect("auctionHall.jsp");
return;
}else{
%>
<card title="购买物品" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转拍卖大厅!)<br/>
<%if(userBagId > 0){%>
<a href="/user/userBagUse.jsp?userBagId=<%=userBagId%>">查看刚拍得的物品</a><br/>
<%}%>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<a href="auctionHall.jsp">拍卖大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>





