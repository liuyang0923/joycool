<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.bean.item.*,net.joycool.wap.bean.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
	ShopAction action = new ShopAction(request);
	int type = action.getParameterInt("id");
	int st = action.getParameterInt("st");
	
	String condition = " hidden = 1 ";
	
	if(type > 0) {
		condition += " and type = " + type;
	}
	HashMap map = UserBean.uService.getItemShowMap("1");
	
	boolean flag = false;
	if(st == 1) {
		condition += " order by price desc";
		flag = true;
	} else if(st == 2) {
		condition += " order by price asc";
		flag = true;
	} else if(st == 3) {
		condition += " order by count desc";
		flag = true;
	} else if(st == 4) {
		condition += " order by count asc";
		flag = true;
	} else if(st == 5) {
		condition += " order by time desc";
		flag = true;
	}
	
	if(!flag) {
		condition += " order by seq desc";
	} else {
		condition += ", seq desc";
	}
	List list = ShopAction.shopService.getItemListByCondition2(condition);
	int p = action.getParameterInt("p");
	PagingBean paging = new PagingBean(action, list.size(), 6, "p");
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="乐秀商品展示"><p>
<%=BaseAction.getTop(request, response)%>
<%@include file="head.jsp"%>
<%
	String[] types = ShopUtil.getTypes();
	int count = types.length;
	for(int i = 0; i < count; i++) {
		if(types[i] != null && types[i].length() > 0) {
			if(ShopAction.shopService.getItemListByType(i).size() > 0){
			if(type == i) {
%><%=i==0?"":"."%><%=types[i] %><%} else {%><%=i==0?"":"."%><a href="items.jsp?id=<%=i%>&amp;st=<%=st%>"><%=types[i]%></a><%}}}} %><br/>

<%for(int i = paging.getStartIndex();i < paging.getEndIndex(); i ++) {
	Integer ii = (Integer)list.get(i);
	ItemBean itemBean = ShopAction.shopService.getShopItemById(ii.intValue());
	DummyProductBean productBean = UserBagCacheUtil.getItem(itemBean.getItemId());
%><a href="item.jsp?id=<%=ii.intValue()%>&amp;t=<%=itemBean.getType()%>&amp;p=<%=p%>"><%=productBean.getName()%></a>
<%if(itemBean.getType() == 1) {
	ShowBean bean = (ShowBean)map.get(new Integer(itemBean.getItemId()));
	if(bean != null){
%><img src="/rep/lx/e<%=bean.getItemId()%>.gif" alt=""/><%}else{%>
<%=ShopUtil.GOLD_NAME %><%}}else {%><%=ShopUtil.GOLD_NAME %>
<%} %><%=itemBean.getPriceString() %><br/>
<%} %><%=paging.shuzifenye("items.jsp?id="+type+"&amp;st="+st, true, "|", response)%>
<select name="st" value="2">
<option value="0">默认排序</option>
<option value="2">价格从低到高</option>
<option value="1">价格从高到低</option>
<option value="3">人气从高到低</option>
<option value="4">人气从低到高</option>
<option value="5">按上架时间</option>
</select>
<a href="items.jsp?id=<%=type%>&amp;st=$st">排序</a><br/>
<%List ads = ShopAction.shopService.getAdsByType(type);
if(ads.size() > 0) {
	int j = (int)(Math.random() * ads.size());
	j = (j >= ads.size()?j-1:j);
	String[] strs = (String[])ads.get(j);
%>*<a href="<%=strs[1] %>"><%=strs[2] %></a><br/>
<%} %>
<%

if(type==1) {%>
=精品推荐=<br/><%
ItemBean bean = ShopAction.shopService.getShopItemById(ShopConstant.ITEM[1]);//yule23 youxi104 zhuangshi 45
if(bean!=null){
%><img src="/rep/shop/<%=bean.getPhotoUrl() %>" alt="<%=bean.getName() %>"/><br/>
<a href="item.jsp?id=<%=ShopConstant.ITEM[1] %>"><%=bean.getName() %></a><%=ShopUtil.GOLD_NAME %><%=bean.getPriceString() %><br/>
<%=bean.getDesc()%><br/>
<%}}else if(type==2) {%>
=精品推荐=<br/><%
ItemBean bean = ShopAction.shopService.getShopItemById(ShopConstant.ITEM[2]);//yule23 youxi104 zhuangshi 45
if(bean!=null){
%><img src="/rep/shop/<%=bean.getPhotoUrl() %>" alt="<%=bean.getName() %>"/><br/>
<a href="item.jsp?id=<%=ShopConstant.ITEM[2] %>"><%=bean.getName() %></a><%=ShopUtil.GOLD_NAME %><%=bean.getPriceString() %><br/>
<%=bean.getDesc()%><br/>
<%}}else if(type==3) {%>
=精品推荐=<br/><%
ItemBean bean = ShopAction.shopService.getShopItemById(ShopConstant.ITEM[3]);//yule23 youxi104 zhuangshi 45
if(bean!=null){
%><img src="/rep/shop/<%=bean.getPhotoUrl() %>" alt="<%=bean.getName() %>"/><br/>
<a href="item.jsp?id=<%=ShopConstant.ITEM[3] %>"><%=bean.getName() %></a><%=ShopUtil.GOLD_NAME %><%=bean.getPriceString() %><br/>
<%=bean.getDesc()%><br/>
<%}} %>
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