<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.action.bank.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");

	if(BankAction.chenkUserBankPW(session)){
		response.sendRedirect("/bank/bankPWCheck.jsp?backTo=/shop/buy.jsp?id="+request.getParameter("id")+"&amp;t="+request.getParameter("t")+"&amp;p="+request.getParameter("p"));
		return;
	}

	ShopAction action = new ShopAction(request);
	int id = action.getParameterInt("id");
	String userIdStr = action.getParameterString("userId");
	int userId = action.getParameterInt("userId");
	int typeId = action.getParameterInt("t");
	int p = action.getParameterInt("p");
	if(id ==0 ){
		response.sendRedirect("index.jsp");
		return;
	}
	String sure = request.getParameter("sure");
	boolean s = (sure != null);
	//System.out.println();
	if(s) {
		if(session.getAttribute(ShopUtil.SESSION_SHOP_BUY) == null){
			response.sendRedirect("/shop/buy.jsp?id="+request.getParameter("id")+"&amp;t="+request.getParameter("t")+"&amp;p="+request.getParameter("p"));
			return;
		}
		synchronized(ShopAction.class){
			action.buy();
		}
	}
	//ItemBean bean = ShopAction.shopService.getShopItemById(id);
	
	//DummyProductBean productBean = UserBagCacheUtil.getItem(bean.getItemId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买结果"><p>
<%=BaseAction.getTop(request, response)%>
<%if(s) {%>
<%if(action.isResult("lack")) {%>
您的酷币不足导致您购买失败,请您<a href="/pay/pay.jsp">充值</a>后再来购买<br/>
<%} else if(action.isResult("nostock")) {%>
该物品已经卖完,请下次再来<br/>
<%} else if(action.isResult("success")) {
		if(request.getAttribute("send") != null) {
			UserBean user = UserInfoUtil.getUser(userId);
%>您成功的赠送给<a href="/home/home.jsp?userId=<%=userId %>"><%=user.getNickNameWml() %></a>一个【<%=request.getAttribute("name") %>】<br/>
<%} else { %>您成功的购买了【<%=request.getAttribute("name") %>】,商品已经放入您的行囊.我们还有更多的商品等待您来选购<br/>
<%}} else if(action.isResult("no")) {%>没有该商品<br/>
<%} else if(action.isResult("nobuy")){%>不能购买该商品<%}else if(action.isResult("double")) {%>交易正在处理中...<br/>
<%}} else {
	session.setAttribute(ShopUtil.SESSION_SHOP_BUY,"b");	
	ItemBean bean = ShopAction.shopService.getShopItemById(id);	
	float price = bean.getPrice();
	boolean isdiscount = false;
	if(bean.getType() == 1)		{//装饰才给打折
		int uid = ((UserBean)session.getAttribute(Constants.LOGIN_USER_KEY)).getId();
		UserInfoBean userBean = ShopAction.shopService.getUserInfo(uid);
		price = ShopUtil.discount(userBean.getConsumeCount(), bean.getPrice()); //add new
		isdiscount = true;
	}	
	
	DummyProductBean productBean = UserBagCacheUtil.getItem(bean.getItemId());
	if(userId > 0 || (userIdStr != null && userIdStr.length() > 0)) {
		UserBean user = UserInfoUtil.getUser(userId);
		if(user == null) {
%>没有该用户<br/>
<%} else {%>确定赠送给用户:<br/>
ID:<%=userId %><br/>
昵称:<%=user.getNickNameWml() %><br/>
商品:【<%=productBean.getName()%>】<br/>
<%if(isdiscount) {%>折扣价<%} else {%>价格<%} %>:<%=price %><%=ShopUtil.GOLD_NAME %><br/>
<a href="/shop/buy.jsp?id=<%=request.getParameter("id")%>&amp;t=<%=request.getParameter("t")%>&amp;p=<%=request.getParameter("p")%>&amp;sure=1&amp;userId=<%=userId%>">确定</a><br/>
<%}} else {%>确定购买【<%=productBean.getName()%>】<br/>
<%if(isdiscount) {%>折扣价<%} else {%>价格<%} %>:<%=price %><%=ShopUtil.GOLD_NAME %><br/>
<a href="/shop/buy.jsp?id=<%=request.getParameter("id")%>&amp;t=<%=request.getParameter("t")%>&amp;p=<%=request.getParameter("p")%>&amp;sure=1">确定</a><br/>
<%} %>
<%} %><a href="items.jsp?id=<%=typeId %>&amp;p=<%=p %>">继续购买</a>|<a href="/user/userBag.jsp">我的行囊</a><br/>
<a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>