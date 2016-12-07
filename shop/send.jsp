<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.action.bank.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");

	if(BankAction.chenkUserBankPW(session)){
		response.sendRedirect("/bank/bankPWCheck.jsp?backTo=/shop/send.jsp?id="+request.getParameter("id")+"&amp;t="+request.getParameter("t")+"&amp;p="+request.getParameter("p"));
		return;
	}

	ShopAction action = new ShopAction(request);
	int id = action.getParameterInt("id");
	int typeId = action.getParameterInt("t");
	int p = action.getParameterInt("p");
	if(id ==0 ){
		response.sendRedirect("index.jsp");
		return;
	}
	//String sure = request.getParameter("sure");
	//boolean s = (sure != null);
	//System.out.println();
	//if(s) {
		//if(session.getAttribute(ShopUtil.SESSION_SHOP_BUY) == null){
		//	response.sendRedirect("/shop/buy.jsp?id="+request.getParameter("id")+"&amp;t="+request.getParameter("t")+"&amp;p="+request.getParameter("p"));
		//	return;
		//}
		//action.buy();
	//}
	ItemBean bean = ShopAction.shopService.getShopItemById(id);
	
	DummyProductBean productBean = UserBagCacheUtil.getItem(bean.getItemId());
	
	float price = bean.getPrice();
	boolean isdiscount = false;
	if(bean.getType() == 1)		{//装饰才给打折
		int uid = ((UserBean)session.getAttribute(Constants.LOGIN_USER_KEY)).getId();
		UserInfoBean userBean = ShopAction.shopService.getUserInfo(uid);
		price = ShopUtil.discount(userBean.getConsumeCount(), bean.getPrice()); //add new
		isdiscount = true;
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="赠送商品"><p>
<%=BaseAction.getTop(request, response)%>
被赠送人ID:<input name="userId" format="*N"/><br/>
赠送商品:【<a href="item.jsp?id=<%=id%>&amp;t=<%=typeId%>&amp;p=<%=p%>"><%=productBean.getName() %></a>】<br/>
<%if(isdiscount) {%>折扣价<%} else {%>价格<%} %>:<%=ShopUtil.GOLD_NAME %><%=price %>g<br/>
<anchor>确定
<go href="buy.jsp?id=<%=request.getParameter("id")%>&amp;t=<%=request.getParameter("t")%>&amp;p=<%=request.getParameter("p")%>" method="post">
<postfield name="userId" value="$userId"/>
</go></anchor><br/>
<a href="items.jsp?id=<%=typeId %>&amp;p=<%=p %>">继续购买</a>|<a href="/user/userBag.jsp">我的行囊</a><br/>
<a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>