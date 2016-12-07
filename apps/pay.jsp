<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%@ page import="net.joycool.wap.spec.shop.*"%><%!

%><%
response.setHeader("Cache-Control","no-cache");
AppPayAction action = new AppPayAction(request);
int count = action.getParameterInt("count");
int item = action.getParameterInt("item");
int appId = action.getParameterInt("app");
String info = request.getParameter("info");
action.pay();
AppPayBean pay = (AppPayBean)request.getAttribute("appPay");
AppBean app = AppAction.getApp(appId);
boolean lack = false;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="支付">
<p align="left">
<%if(app!=null){%>【<%=app.getName()%>】<br/><%}%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{%>
商品:<%=info%><br/>
<%if(item==0){
UserInfoBean shopInfo = ShopAction.shopService.getUserInfo(pay.getUserId());
lack = shopInfo == null || (float)count/100>shopInfo.getGold();	// 判断是否酷币不足
%>价格:<%=(float)count/100%>酷币<br/>
(您现有<%if(shopInfo==null){%>0酷币<%}else{%><%=shopInfo.getGoldString()%><%}%><%if(lack){%>.您的酷币不足,需要先<a href="/pay/pay.jsp">充值</a>酷币才能购买<%}%>)<br/>
<%}else{
DummyProductBean itemP = UserBagCacheUtil.getItem(item);
if(itemP != null){
%>需要:<%=itemP.getName()%><%}%><br/><%}%>
<%if(lack){%>确认购买<%}else{%><anchor title="post">确认购买
<go href="pay.jsp?app=<%=appId%>" method="post">
<postfield name="conf" value="<%=session.getId()%>"/>
</go></anchor><%}%>|<a href="/<%=app.getDir()%>/">取消购买</a><br/>
<%}%>
<br/>
<%if(app!=null){%><a href="/<%=app.getDir()%>/">返回<%=app.getName()%></a><%}else{%><a href="/bottom.jsp">返回</a><%}%>
</p>
</card>
</wml>