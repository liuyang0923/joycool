<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.action.auction.AuctionAction"%><%@ page import="net.joycool.wap.bean.auction.AuctionBean" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%!
	public static String bigNumberFormat2(long number) {
		if(number <= -100000l) {
			if(number > -1000000000l)
				return String.valueOf(number / 10000l) + "万";
			else
				return String.valueOf(number / 100000000l) + "亿";
		} else if(number < 100000l)
			return String.valueOf(number);
		if(number < 100000000l)
			return String.valueOf(number / 10000l) + "万";
		long left = (number % 100000000l) / 10000;
		if(left > 0)
			return (number / 100000000l) + "亿" + left + "万";
		return String.valueOf(number / 100000000l) + "亿";
	}
%><%
response.setHeader("Cache-Control","no-cache");
AuctionAction action = new AuctionAction(request);
action.buy(request);
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
<%}else{
AuctionBean auction=(AuctionBean)request.getAttribute("auction");
DummyProductBean dummyProduct =(DummyProductBean)request.getAttribute("dummyProduct");
UserBagBean userBag=null;
long timeleft=10000000000l;
if(auction.getUserBagId()!=0){
	userBag = UserBagCacheUtil.getUserBagCache(auction.getUserBagId());
	timeleft = userBag.getTimeLeft();
}
UserBean leftUser = UserInfoUtil.getUser(auction.getLeftUserId());
UserBean rightUser = UserInfoUtil.getUser(auction.getRightUserId());
%>
<card title="购买物品" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=dummyProduct.getName()%>(<%=auction.getTime()%><%if(dummyProduct.getTime() > 1){%>/<%=dummyProduct.getTime()%><%}%>)<%if(timeleft<=0){%>已过期<%}else if(timeleft<3600000l*24*100){%>(<%=DateUtil.formatTimeInterval(timeleft)%>)<%}%><br/>
<%=dummyProduct.getDescription()%><br/>
拍卖人：<a href="/user/ViewUserInfo.do?userId=<%=leftUser.getId()%>"><%=StringUtil.toWml(leftUser.getNickName())%></a><br/>
<%if(rightUser!=null){%>竞价人：<a href="/user/ViewUserInfo.do?userId=<%=rightUser.getId()%>"><%=StringUtil.toWml(rightUser.getNickName())%></a><br/><%}%>
现价:<%=bigNumberFormat2(auction.getCurrentPrice())%>乐币<br/>
一口价:<%=bigNumberFormat2(auction.getBitePrice())%>乐币<br/>
时间:<%=auction.getTimeLength() %><br/>
<%if(auction.getCurrentPrice()<5000000000l){%><a href="buyResult.jsp?type=1&amp;auctionId=<%=auction.getId()%>">竞价</a><%}else{%><a href="buy3.jsp?type=1&amp;auctionId=<%=auction.getId()%>">竞价</a><%}%><br/>
<a href="buy2.jsp?auctionId=<%=auction.getId()%>">一口价</a><br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<a href="auctionHall.jsp">拍卖大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>