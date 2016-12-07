<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@include file="../bank/checkpw.jsp"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.shopInfo(request); 
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("tongList.jsp");
%>
<card title="商店" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="<%=url%>">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("error")){
TongBean tong =(TongBean)request.getAttribute("tong");
String url=("shop.jsp?tongId="+tong.getId());
%>
<card title="商店道具" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="<%=url%>">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
DummyProductBean dummyProduct=(DummyProductBean)request.getAttribute("dummyProduct");
%>
<card title="商店">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//轰天炮
if(dummyProduct.getId()==15){%>
名称：<%=dummyProduct.getName()%><br/>
道具说明：<%=dummyProduct.getDescription()%><br/>
所需点数：1000点<br/>
<a href="shopProduct.jsp?productId=15&amp;tongId=<%=tong.getId()%>">领取道具</a><br/>
<%}//攻城战鼓
else if(dummyProduct.getId()==25){%>
名称：<%=dummyProduct.getName()%><br/>
道具说明：<%=dummyProduct.getDescription()%><br/>
所需点数：9000点<br/>
<a href="shopProduct.jsp?productId=25&amp;tongId=<%=tong.getId()%>">领取道具</a><br/>
<%}
//防护膜
else if(dummyProduct.getId()==16){%>
名称：<%=dummyProduct.getName()%><br/>
道具说明：<%=dummyProduct.getDescription()%><br/>
所需点数：600点<br/>
<a href="shopProduct.jsp?productId=16&amp;tongId=<%=tong.getId()%>">领取道具</a><br/>
<%}//守城战鼓
else if(dummyProduct.getId()==26){%>
名称：<%=dummyProduct.getName()%><br/>
道具说明：<%=dummyProduct.getDescription()%><br/>
所需点数：6000点<br/>
<a href="shopProduct.jsp?productId=26&amp;tongId=<%=tong.getId()%>">领取道具</a><br/>
<%}%>
<a href="shop.jsp?tongId=<%=tong.getId()%>">返回商店</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>