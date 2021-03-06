<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction" %><%@page import="java.util.List" %><%@ page import="net.joycool.wap.bean.auction.AuctionBean" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
AuctionAction action = new AuctionAction(request);
action.myAuction(request);
String result =(String)request.getAttribute("result");
String url=("/auction/auctionHall.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="我拍卖的物品" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<a href="auctionHall.jsp">拍卖大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List userAuctionList=(List)request.getAttribute("userAuctionList");
%>
<card title="我拍卖的物品">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(userAuctionList.size()>0){
Integer auctionId=null;
AuctionBean auction = null;
DummyProductBean dummyProduct=null;
for(int i=0;i<userAuctionList.size();i++){
    auctionId=(Integer)userAuctionList.get(i);
    auction=action.getAuction(auctionId.intValue());
    if(auction==null)continue;
    dummyProduct=action.getDummyProduct(auction.getProductId());
    if(dummyProduct==null)continue;
    if(auction.getMark()==1){%>
<%=i+1%>.<%=dummyProduct.getName()%><%=StringUtil.bigNumberFormat(auction.getCurrentPrice())%>乐币已售<br/>
<%}else{%>
<%=i+1%>.<a href="buy.jsp?auctionId=<%=auction.getId()%>"><%=dummyProduct.getName() %></a>
<%=StringUtil.bigNumberFormat(auction.getCurrentPrice())%>乐币<br/><%}%>
<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}}else{%>您没有拍卖过物品!<br/><%}%>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<a href="auctionHall.jsp">拍卖大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>