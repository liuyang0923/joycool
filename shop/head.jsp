<%@ page contentType="text/vnd.wap.wml;charset=utf-8" import="net.joycool.wap.spec.shop.*"%><%
ShopAction sAction = new ShopAction(request);
boolean isLogin = (sAction.getLoginUser() != null);
%><%if(isLogin) {
	int uids = sAction.getLoginUser().getId();
	UserInfoBean beanS = ShopAction.shopService.getUserInfo(uids);
	if(beanS == null) {
		beanS = ShopAction.shopService.addUserInfo(uids);
	}%>
您有<%=beanS.getGoldString() %>【<a href="/pay/pay.jsp">充值</a>.<a href="/pay/myOrder.jsp">查询</a>】<br/><%}%>
