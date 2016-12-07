<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
	
	ShopAction action = new ShopAction(request);
	
	if(action.hasParam("a")){
		action.deleteFavorite();
	}
	
	int uid = action.getLoginUser().getId();	
	int count = ShopAction.shopService.getCountFavorite(uid);
	
	PagingBean paging = new PagingBean(action, count, 5,"p");
	
	
	List list = ShopAction.shopService.getFavoriteList(uid, paging.getStartIndex(), paging.getEndIndex());
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="收藏夹"><p>
<%=BaseAction.getTop(request, response)%>
<%@include file="head.jsp"%>
<%if(action.isResult("success")) {%>
删除成功<br/>
<%} %>
您一共收藏了<%=count %>件商品<br/>
<%
for(int i = 0; i < list.size() ; i ++) {
		FavoriteBean favoriteBean = (FavoriteBean)list.get(i);
		ItemBean bean = ShopAction.shopService.getShopItemById(favoriteBean.getItemId());
		if(bean != null) {
		DummyProductBean productBean = UserBagCacheUtil.getItem(bean.getItemId());
%>
<a href="item.jsp?id=<%=bean.getId()%>"><%=productBean.getName() %></a>|<%=bean.getPriceString() %><%=ShopUtil.GOLD_NAME %>|<a href="favorite.jsp?a=d&amp;id=<%=favoriteBean.getId() %>">删除</a><br/>
<%}} %>
<%=paging.shuzifenye("favorite.jsp", false,"|", response) %>
=热点推荐=<br/>
<%
List sugguestList = ShopUtil.getSugguestList();
for(int i = 0; i < sugguestList.size(); i++) {
	Integer ii = (Integer)sugguestList.get(i);
	ItemBean itemBean = ShopAction.shopService.getShopItemById(ii.intValue());
	DummyProductBean producBean = UserBagCacheUtil.getItem(itemBean.getItemId());
%><a href="item.jsp?id=<%=ii.intValue()%>&amp;t=<%=itemBean.getType()%>"><%=producBean.getName()%></a><%=ShopUtil.GOLD_NAME %><%=itemBean.getPriceString() %><br/>
<%} %><a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>