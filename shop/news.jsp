<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.bean.item.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
	ShopAction action = new ShopAction(request);
	List list = ShopAction.shopService.getNew(30);
	int p = action.getParameterInt("p");
	PagingBean paging = new PagingBean(action, list.size(), 8, "p");
	HashMap map = UserBean.uService.getItemShowMap("1");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="最新上架"><p>
<%=BaseAction.getTop(request, response)%>
<%@include file="head.jsp"%>
=新品上架=<br/>
<%for(int i = paging.getStartIndex();i < paging.getEndIndex(); i ++) {
	Integer ii = (Integer)list.get(i);
	ItemBean itemBean = ShopAction.shopService.getShopItemById(ii.intValue());
	DummyProductBean productBean = UserBagCacheUtil.getItem(itemBean.getItemId());
%><a href="item.jsp?id=<%=ii.intValue()%>&amp;t=<%=itemBean.getType()%>&amp;p=<%=p%>"><%=productBean.getName()%></a>
<%if(itemBean.getType() == 1) {
	ShowBean bean = (ShowBean)map.get(new Integer(itemBean.getItemId()));
	if(bean != null){
%><img src="/rep/lx/e<%=bean.getItemId()%>.gif" alt=""/><%}else {%>
<%=ShopUtil.GOLD_NAME %><%}}else {%><%=ShopUtil.GOLD_NAME %>
<%} %><%=itemBean.getPriceString() %><br/>
<%} %><%=paging.shuzifenye("news.jsp", false, "|", response)%>
=精品推荐=<br/><%
ItemBean bean = ShopAction.shopService.getShopItemById(167);
%><img src="/rep/shop/<%=bean.getPhotoUrl() %>" alt="<%=bean.getName() %>"/><br/>
<a href="item.jsp?id=45"><%=bean.getName() %></a><%=ShopUtil.GOLD_NAME %><%=bean.getPriceString() %><br/>
<%=bean.getDesc()%><br/>
=热点推荐=<br/>
<%
List sugguestList = ShopService.getSugguestList(4);
for(int i = 0; i < sugguestList.size(); i++) {
	Integer ii = (Integer)sugguestList.get(i);
	ItemBean itemBean = ShopAction.shopService.getShopItemById(ii.intValue());
%><a href="item.jsp?id=<%=ii.intValue()%>&amp;t=<%=itemBean.getType()%>"><%=itemBean.getName()%></a><%=ShopUtil.GOLD_NAME %><%=itemBean.getPriceString() %><br/>
<%} %><a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>