<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.util.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");

	
	//System.out.println(id);
	//ItemBean bean2 = ShopAction.shopService.getShopItemById(id);
	
	//DummyProductBean producBean2 = UserBagCacheUtil.getItem(bean2.getItemId());
	
	ShopAction action = new ShopAction(request);
	boolean isLogin = (action.getLoginUser() != null);
	
	List list = new ArrayList();
	int type = action.getParameterInt("t");
	if(type == 0) {
		list = ShopService.getSugguestList(4);
	} else if(type == 1) {
		list = ShopService.getTopSugguestList(4);
	}
	int rank = 0;
	if(isLogin)
		rank = UserInfoUtil.getUserStatus(action.getLoginUser().getId()).getRank();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="乐秀商城"><p>
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
[开]<a href="help.jsp">乐秀商城公告</a><br/>
[新]<a href="help2.jsp">乐秀商城会员服务</a><br/>
<%if(rank>=3){%>
<%if(isLogin) {
	int uid = action.getLoginUser().getId();
	UserInfoBean bean = ShopAction.shopService.getUserInfo(uid);
	
	if(bean == null) {
		bean = ShopAction.shopService.addUserInfo(uid);
	}%>
<a href="info.jsp">个人信息</a>.<a href="favorite.jsp">收藏夹</a><br/>
您有酷币<%=ShopUtil.GOLD_NAME %><%=bean.getGoldString() %>【<a href="/pay/pay.jsp">充值</a>.<a href="/pay/myOrder.jsp">查询</a>】<br/>

<% 
} 
List ads = ShopUtil.getAds();
if(ads.size() > 0) {
int j = (int)(Math.random() * ads.size());
j = (j >= ads.size()?j-1:j);
String[] strs = (String[])ads.get(j);
%>
*<a href="<%=strs[1] %>"><%=strs[2] %></a><br/>
<%} %>
=特别推荐.<a href="news.jsp">新品上架</a>=<br/><%
ItemBean bean = ShopAction.shopService.getShopItemById(ShopConstant.INDEX[1]);
if(bean != null){
%><img src="/rep/shop/<%=bean.getPhotoUrl() %>" alt="<%=bean.getName() %>"/><br/>
<a href="item.jsp?id=<%=ShopConstant.INDEX[1] %>"><%=bean.getName() %></a><%=ShopUtil.GOLD_NAME %><%=bean.getPriceString() %><br/>
<%=bean.getDesc()%><br/>
---------------<br/>
<%}
ItemBean bean2 = ShopAction.shopService.getShopItemById(ShopConstant.INDEX[2]);
%><img src="/rep/shop/<%=bean2.getPhotoUrl() %>" alt="<%=bean2.getName() %>"/><br/>
<a href="item.jsp?id=<%=ShopConstant.INDEX[2] %>"><%=bean2.getName() %></a><%=ShopUtil.GOLD_NAME %><%=bean2.getPriceString() %><br/>
<%=bean2.getDesc()%><br/>
&lt;导航:<%
	String[] types = ShopUtil.getTypes();
	int count = types.length;
	for(int i = 1; i < count; i++) {
		if(types[i] != null && types[i].length() > 0) {
			if(ShopAction.shopService.getItemListByType(i).size() > 0){
			
%><%=i==1?"":"."%><a href="items.jsp?id=<%=i%>"><%=types[i]%></a><%}}} %>&gt;<br/>
<%if(type==0) {%>
=推荐.<a href="index.jsp?t=1">排行</a>=<br/>
<%}else { %>
=<a href="index.jsp?t=0">推荐</a>.排行=<br/>
<%} %>
<%for(int i = 0; i < list.size(); i++) {
	Integer ii = (Integer)list.get(i);
	ItemBean itemBean = ShopAction.shopService.getShopItemById(ii.intValue());
	
%><a href="item.jsp?id=<%=ii.intValue()%>&amp;t=<%=itemBean.getType()%>">
<%if(type==1){DummyProductBean productBean = UserBagCacheUtil.getItem(itemBean.getItemId());%><%=productBean.getName() %><%}else{%><%=itemBean.getName() %><%} %></a>
<%if(i < 3){if(type == 0) {%><img src="img/new.gif" alt="推荐"/>
<%}else if(type == 1) {%>
<img src="img/top.gif" alt="排行"/>
<%}} %><br/>
<%} %>
<a href="items.jsp">&gt;&gt;进入商品分类</a><br/>
<%
	}else{
%>
等级不够,无法访问商城<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>