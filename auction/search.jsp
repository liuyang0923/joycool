<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction" %><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.auction.AuctionBean"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%
response.setHeader("Cache-Control","no-cache");
AuctionAction action = new AuctionAction(request);
action.search();
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
List itemList=(List)request.getAttribute("itemList");
int s = action.getParameterInt("s");
int e = action.getParameterInt("e");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="拍卖大厅">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(itemList!=null){%>
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
<a href="/auction/buy.jsp?auctionId=<%=auction.getId()%>"><%=dummyProduct.getName() %><%if(dummyProduct.getTime()>1||auction.getTime()>1){%>(<%=StringUtil.itemTimeString(auction.getTime())%>)<%}%></a>
<%=StringUtil.bigNumberFormat(auction.getCurrentPrice())%>-<%=auction.getTimeLength() %><br/><%}%>
<%}%>
<%=PageUtil.shuzifenye(pagingBean, "search.jsp?s="+s+"&amp;e="+e, true, "|", response)%>
<br/><a href="search.jsp">继续搜索</a><br/>
<%}else{%>
选择搜索拍卖行:<br/>
<a href="search.jsp?s=31&amp;e=32">通吃/旗帜合成卡</a><br/>
<a href="search.jsp?s=15&amp;e=16">轰天炮/防护膜</a><br/>
<a href="search.jsp?s=25&amp;e=26">攻城/守城战鼓</a><br/>
<a href="search.jsp?s=48&amp;e=53">渔场/狩猎卡(材料)</a><br/>
<a href="search.jsp?s=54&amp;e=55">宠物/浮生卡(材料)</a><br/>
<a href="search.jsp?s=40&amp;e=46">渔场/狩猎/宠物/浮生/论坛/通吃/旗帜卡</a><br/>
<a href="search.jsp?s=138&amp;e=149">曜石/元素碎片</a><br/>
<a href="search.jsp?s=8&amp;e=9">糖果/宝箱</a><br/>
<a href="search.jsp?s=13&amp;e=14">宠物孟婆汤/隐藏宠物卡</a><br/>
<a href="search.jsp?s=17&amp;e=18">免输金牌/宠物防弹衣</a><br/>
<br/>
<%}%>
<a href="auctionHall.jsp">返回拍卖大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>