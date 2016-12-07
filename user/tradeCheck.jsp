<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.action.user.UserBagAction"%><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");%><%@include file="../bank/checkpw.jsp"%><%
UserBagAction action = new UserBagAction(request);
action.tradeView();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
TradeBean trade = (TradeBean)request.getAttribute("trade");
int toUserId;
if(trade != null)
	toUserId = trade.getOUser(loginUser.getId()).getUserId();
else
	toUserId = StringUtil.toInt(request.getParameter("userId"));
UserBean toUser = UserInfoUtil.getUser(toUserId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%if(action.isResult("show")){
%><card title="赠送/交易">
<p align="left">
<%=BaseAction.getTop(request, response)%>
对方:<%=StringUtil.toWml(toUser.getNickName())%><br/>
<%
TradeUserBean o = trade.getOUser(loginUser.getId());
TradeUserBean m = trade.getUser(loginUser.getId());
synchronized(trade){
m.setRead(true);
%>
==我的赠送物品==<br/>乐币:<%=StringUtil.bigNumberFormat2(m.getMoney())%><br/>
<%
List items = m.getItems();
if(items != null)
for(int i=0;i<items.size();i++){
	int[] i2 = (int[])items.get(i);
    UserBagBean userBag=action.getUserBag(i2[0]);if(userBag==null)continue;
    DummyProductBean dummyProduct=action.getDummyProduct(userBag.getProductId());if(dummyProduct==null)continue;%>
<%=i+1%>.<%=dummyProduct.getName()%>
<%if(userBag.getTime()>1){%>(<%=userBag.getTime()%>)<%} %><br/><%}%>
------------<br/>
<%}%>
你选择的是直接赠送以上物品,确认要这么做吗?<br/>
<a href="tradeView.jsp?a=-3&amp;tr=<%=trade.getId()%>">确定直接赠送</a><br/>
<a href="tradeView.jsp?tr=<%=trade.getId()%>">返回</a><br/>
<a href="ViewUserInfo.do?userId=<%=toUserId%>">查看用户信息</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(action.isResult("success")){
List sentItems=(List)request.getAttribute("sentItems");
long money = action.getAttributeLong("money");
%>
<card title="赠送/交易">
<p align="left">
<%=BaseAction.getTop(request, response)%>
已送给<%=StringUtil.toWml(toUser.getNickName())%>:<br/>
<%if(money > 0){%>
<%=money%>乐币<br/>
<%}%>
<%if(sentItems != null){
for(int i=0;i<sentItems.size();i++){
    Integer userBagId=(Integer)sentItems.get(i);
    DummyProductBean dummyProduct=action.getDummyProduct(userBagId.intValue());%>
<%=i+1%>.<%=dummyProduct.getName()%><br/>
<%}}%>
<a href="/user/ViewUserInfo.do?userId=<%=toUserId%>">返回用户信息</a><br/>
<a href="/user/userBag.jsp">返回我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="赠送/交易">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<%if(trade != null){%>
<a href="tradeView.jsp?tr=<%=trade.getId()%>">查看当前交易</a><br/>
<%}%>
<%if(toUserId > 0){%>
<a href="tradeItem.jsp?userId=<%=toUserId%>">返回</a><br/>
<a href="/user/ViewUserInfo.do?userId=<%=toUserId%>">返回用户信息</a><br/>
<%}%>
<a href="/user/userBag.jsp">返回我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card><%}%>
</wml>