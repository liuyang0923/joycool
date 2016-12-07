<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.action.user.UserBagAction"%><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");%><%@include file="../bank/checkpw.jsp"%><%
UserBagAction action = new UserBagAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
long moneyInput = action.getParameterLong("money");
if(moneyInput<0){
	response.sendRedirect(("/lswjs/index.jsp"));
	return;
}
if(moneyInput>1000000000 && net.joycool.wap.util.ForbidUtil.isForbid("sell",loginUser.getId())){	// 被封禁的玩家最多转账限制

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赠送/交易">
<p align="left">
交易监察已经限制了你的乐币交易上限(10亿)<br/>
<a href="/admin/query.jsp">查询详情</a><br/>
<a href="/admin/list.jsp?type=9">+交易监察列表+</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>


</wml><%
return;

}
action.tradeView();
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
%>==对方交易物品==<br/>乐币:<%=StringUtil.bigNumberFormat2(o.getMoney())%><br/>
<%
List items = o.getItems();
if(items != null)
for(int i=0;i<items.size();i++){
	int[] i2 = (int[])items.get(i);
    UserBagBean userBag=action.getUserBag(i2[0]);if(userBag==null)continue;
    DummyProductBean dummyProduct=action.getDummyProduct(userBag.getProductId());if(dummyProduct==null)continue;
    long timeleft = userBag.getTimeLeft();%>
<%=i+1%>.<%=dummyProduct.getName()%>
<%if(userBag.getTime()>1||dummyProduct.getTime()>1){%>(<%=userBag.getTime()%>)<%}%>
<%if(timeleft>0&&timeleft<3600000l*24*100){%>(<%=DateUtil.formatTimeInterval(timeleft)%>)<%}%><br/><%}%>
==我的交易物品==<br/>乐币:<%=StringUtil.bigNumberFormat2(m.getMoney())%><br/>
<%
items = m.getItems();
if(items != null)
for(int i=0;i<items.size();i++){
	int[] i2 = (int[])items.get(i);
    UserBagBean userBag=action.getUserBag(i2[0]);if(userBag==null)continue;
    DummyProductBean dummyProduct=action.getDummyProduct(userBag.getProductId());if(dummyProduct==null)continue;
    long timeleft = userBag.getTimeLeft();%>
<%=i+1%>.<%=dummyProduct.getName()%>
<%if(userBag.getTime()>1){%>(<%=userBag.getTime()%>)<%} %>
<%if(timeleft>0&&timeleft<3600000l*24*100){%>(<%=DateUtil.formatTimeInterval(timeleft)%>)<%}%><br/><%}%>
------------<br/>
<%if(!m.isEmpty()){%>
<%if(o.isEmpty()){%>
<a href="tradeView.jsp?a=0&amp;tr=<%=trade.getId()%>">发出交易请求</a><br/>
<%}else{%>
<a href="tradeView.jsp?a=<%=o.getStatus() %>&amp;tr=<%=trade.getId()%>">接受以上交易</a><br/>
<%}%>
<%}%>
<%}%>
<%if(!m.isEmpty()){%>
<a href="tradeItem.jsp?tr=<%=trade.getId() %>&amp;userId=<%=toUserId%>">重新设置交易物品</a><br/>
<a href="tradeCheck.jsp?tr=<%=trade.getId()%>">直接赠送</a><br/>
<%}else{%>
<a href="tradeItem.jsp?tr=<%=trade.getId() %>&amp;userId=<%=toUserId%>">设置交易物品</a><br/>
<%}%>
<a href="tradeView.jsp?a=-2&amp;tr=<%=trade.getId()%>">取消交易</a><br/>
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