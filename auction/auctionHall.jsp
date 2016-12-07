<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction" %><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.auction.AuctionBean"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%
response.setHeader("Cache-Control","no-cache");
AuctionAction action = new AuctionAction(request);
action.auctionHall(request);
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
List itemList=(List)request.getAttribute("itemList");
int type = action.getParameterInt("type");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="拍卖大厅">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if (loginUser != null){
%><%=loginUser.showImg("/img/auction/hall.gif")%><%	
}%>
<a href="auctionDesc.jsp">[必读]拍卖说明</a><br/>
<a href="auctionLog.jsp">最近物品成交价</a><br/>
<%if(type==0){%>宝物<%}else{%><a href="auctionHall.jsp?type=0">宝物</a><%}%>=<%if(type==1){%>家具<%}else{%><a href="auctionHall.jsp?type=1">家具</a><%}%>=<%if(type==2){%>整人<%}else{%><a href="auctionHall.jsp?type=2">整人</a><%}%>=<a href="search.jsp">搜索</a><br/>
<%
AuctionBean auction=null;
DummyProductBean dummyProduct=null;
for(int i=0;i<itemList.size();i++){
auction=(AuctionBean)itemList.get(i);
dummyProduct=action.getDummyProduct(auction.getProductId());
if(dummyProduct==null){continue;}
if(auction.getMark()==1){%>
<%=dummyProduct.getName()%>已售出<br/>
<%}else{%>
<a href="buy.jsp?auctionId=<%=auction.getId()%>"><%=dummyProduct.getName()%><%if(dummyProduct.getTime()>1||auction.getTime()>1){%>(<%=StringUtil.itemTimeString(auction.getTime())%>)<%}%></a>
<%=StringUtil.bigNumberFormat(auction.getCurrentPrice())%>-<%=auction.getTimeLength() %><br/><%}%>
<%}%>
<%=PageUtil.shuzifenye(pagingBean, "auctionHall.jsp?type=" + type, true, "|", response)%>
<a href="myBuy.jsp">我竞价的物品</a><br/>
<a href="myAuction.jsp">我拍卖的物品</a><br/>
<a href="/bank/bank.jsp">乐酷银行</a><br/>
<%--=BaseAction.getAdver(25,response)--%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>