<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.action.user.UserBagAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.*" %><%!
static IUserService userService = ServiceFactory.createUserService();
%><%
response.setHeader("Cache-Control","no-cache");%><%@include file="../bank/checkpw.jsp"%><%
UserBagAction action = new UserBagAction(request);
action.tradeItem();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserBean toUser = (UserBean)request.getAttribute("toUser");
int toUserId = action.getAttributeInt("userId");
if(userService.isUserBadGuy(toUserId, loginUser.getId())){
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赠送/交易">
<p align="left">
你在对方的黑名单里，不能给他发交易！<br/>
<a href="/user/ViewUserInfo.do?userId=<%=toUserId%>">返回用户信息</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml><%
	return;
}
int tradeId = action.getParameterInt("tr");
String param = "userId=" + toUserId;
if(tradeId > 0)
	param += "&amp;tr=" + tradeId;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
List userBagList=(List)request.getAttribute("userBagList");
%><card title="赠送/交易">
<p align="left">
<%=BaseAction.getTop(request, response)%>
对方:<%=StringUtil.toWml(toUser.getNickName())%><br/>

<%
List list = new ArrayList();
if(!action.hasParam("noitem")){
// 取出所有可以交易的物品
for(int i=0;i<userBagList.size();i++){
    Integer userBagId=(Integer)userBagList.get(i);
    UserBagBean userBag=UserBagCacheUtil.getUserBagCache(userBagId.intValue());
    DummyProductBean dummyProduct=action.getDummyProduct(userBag.getProductId());
    if(dummyProduct != null&&!dummyProduct.isBind())list.add(userBag);}
%>

<%if(list.size()>0){%>
<select multiple="true"  name="items">
<%
for(int i=0;i<list.size();i++){
    UserBagBean userBag=(UserBagBean)list.get(i);
    DummyProductBean dummyProduct=action.getDummyProduct(userBag.getProductId());
%><option value="<%=userBag.getId()%>"><%=dummyProduct.getName()%><%=userBag.getTimeString(dummyProduct)%></option>
<%}%></select><br/>
<%}%>
<%}else{%>
<a href="tradeItem.jsp?userId=<%=toUserId%>">交易物品</a><br/>
<%}%>
银行转帐(乐币):<br/>
存款:<%=StringUtil.bigNumberFormat(BankCacheUtil.getStoreMoney(loginUser.getId()))%><br/>
<input type="text" name="money" format="*N" value=""/><br/>
<anchor>确认物品选择
<go href="tradeView.jsp?<%=param%>" method="post">
<%if(list.size()>0){%><postfield name="items" value="$items" /><%}%>
<postfield name="money" value="$money" />
</go>
</anchor><br/>
<a href="/user/ViewUserInfo.do?userId=<%=toUserId%>">返回用户信息</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>