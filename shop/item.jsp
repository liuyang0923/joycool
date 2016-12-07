<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
	ShopAction action = new ShopAction(request);
	int id = action.getParameterInt("id");
	int typeId = action.getParameterInt("t");
	int p = action.getParameterInt("p");
	if(id ==0 ){
		response.sendRedirect("index.jsp");
		return;
	}
	
	ItemBean bean = ShopAction.shopService.getShopItemById(id);
	
	
	ShopUtil.updateLookRecord(action.getLoginUser().getId(), bean);
	
	
	List list = ShopAction.shopService.getItemListByType(bean.getType());
	
	int count = list.size() > 4?4:list.size();
	
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=bean.getName() %>"><p>
<%=BaseAction.getTop(request, response)%>
<%@include file="head.jsp"%>
<%=bean.getName()%><br/>
<%if(bean.getPhotoUrl() != null && bean.getPhotoUrl().length() > 0) {%>
<img src="/rep/shop/<%=bean.getPhotoUrl() %>" alt=""/><br/>
<%}

float price = bean.getPrice();
boolean isdiscount = false;
if(bean.getType() == 1)		{//装饰才给打折
	int uid = ((UserBean)session.getAttribute(Constants.LOGIN_USER_KEY)).getId();
	UserInfoBean userBean = ShopAction.shopService.getUserInfo(uid);
	price = ShopUtil.discount(userBean.getConsumeCount(), bean.getPrice()); //add new
	isdiscount = true;
}
DummyProductBean productBean = UserBagCacheUtil.getItem(bean.getItemId());%>
商品价格:<%=ShopUtil.GOLD_NAME %><%=bean.getPriceString() %><br/>
<%if(isdiscount) {%>折扣价格:<%=price %>g<br/><%} %>
<%if(productBean.getMode()!=0&&productBean.getMode()!=6&&productBean.getMode()!=7){ %>
使用次数:<%if(bean.getTimes() > 1) {%><%=bean.getTimes() %><%} else { %><%=productBean.getTime() %><%} %><br/>
<%} %>
<%if(bean.getDue() > 0) {%>
有效期:<%=ShopUtil.dueTime(bean.getDue())%>天<br/>
<%} else {
	if(productBean.getDue() > 0){
%>有效期:<%=ShopUtil.dueTime(productBean.getDue())%>天<br/><%}%><% }%>
<%=bean.getDesc()%><br/>
<%if(bean.getMax() > -1) {
	if(bean.getOdd() <= 0) {
%>该商品已缺货<br/>
<%	} else {%>
<%=bean.getOdd() < 0?"":"剩余数量:"+bean.getOdd()+"<br/>"%>
<%}}%><%if(bean.getMax() > -1&&bean.getOdd() <= 0) {
%>购买.赠送<%}else{
%><a href="buy.jsp?id=<%=request.getParameter("id")%>&amp;t=<%=typeId %>&amp;p=<%=p %>">购买</a>.<a href="send.jsp?id=<%=request.getParameter("id")%>&amp;t=<%=typeId %>&amp;p=<%=p %>">赠送</a><%}%>.<a href="store.jsp?id=<%=request.getParameter("id")%>&amp;t=<%=typeId %>&amp;p=<%=p %>" >收藏</a><br/>
<a href="items.jsp?id=<%=typeId %>&amp;p=<%=p %>">返回</a><br/>
=相关产品=<br/>
<%
for(int i = 0; i < count; i++) {
	Integer ii = (Integer)list.get(i);
	ItemBean itemBean = ShopAction.shopService.getShopItemById(ii.intValue());
	DummyProductBean producBean = UserBagCacheUtil.getItem(itemBean.getItemId());
%><a href="item.jsp?id=<%=ii.intValue()%>&amp;t=<%=itemBean.getType()%>"><%=itemBean.getName()%></a><br/>
<%} %>【<%
	String[] types = ShopUtil.getTypes();
	int counts = types.length;
	for(int i = 1; i < counts; i++) {
		if(types[i] != null && types[i].length() > 0) {
			if(ShopAction.shopService.getItemListByType(i).size() > 0){
			
%><%=i==1?"":"."%><a href="items.jsp?id=<%=i%>"><%=types[i]%></a><%}}} %>】<br/>
=热点推荐=<br/>
<%
List sugguestList = ShopService.getSugguestList(4);
for(int i = 0; i < sugguestList.size(); i++) {
	Integer ii = (Integer)sugguestList.get(i);
	ItemBean itemBean = ShopAction.shopService.getShopItemById(ii.intValue());
	DummyProductBean producBean = UserBagCacheUtil.getItem(itemBean.getItemId());
%><a href="item.jsp?id=<%=ii.intValue()%>&amp;t=<%=itemBean.getType()%>"><%=producBean.getName()%></a><%=ShopUtil.GOLD_NAME %><%=itemBean.getPriceString() %><br/>
<%} %><a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>