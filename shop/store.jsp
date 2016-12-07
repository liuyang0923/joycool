<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
	ShopAction action = new ShopAction(request);
	int id = action.getParameterInt("id");
	int typeId = action.getParameterInt("t");
	int p = action.getParameterInt("p");
	if(id ==0 ){
		response.sendRedirect("index.jsp");
		return;
	}
	
	action.store();
	//ItemBean bean = ShopAction.shopService.getShopItemById(id);
	
	//DummyProductBean productBean = UserBagCacheUtil.getItem(bean.getItemId());
%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="收藏结果"><p>
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("a")) {%>
您的收藏夹中已经添加了【<%=request.getAttribute("name") %>】,无法重复添加同一个商品<br/>
<%} else if(action.isResult("b")) {%>
您已经将【<%=request.getAttribute("name") %>】添加到您的收藏夹中<br/>
<%} else if(action.isResult("no")){%>
没有该商品<br/>
<%} %>
<a href="items.jsp?id=<%=typeId %>&amp;p=<%=p %>">&lt;&lt;返回商品列表</a><br/>
<a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>