<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
	ShopAction action = new ShopAction(request);
	int uid = action.getLoginUser().getId();
	UserInfoBean bean = ShopAction.shopService.getUserInfo(uid);
	if(bean == null) {
		bean = ShopAction.shopService.addUserInfo(uid);
	}
	
	List list = (List)ShopUtil.lookRecordCache.sgt(uid);
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="个人信息"><p>
<%=BaseAction.getTop(request, response)%>
您现在酷币的余额是<%=ShopUtil.GOLD_NAME %><%=bean.getGoldString() %><br/>
<a href="/pay/pay.jsp">酷币充值</a>.<a href="record.jsp">历史记录</a><br/>
<a href="favorite.jsp">收藏夹</a>.<a href="/user/userBag.jsp">我的行囊</a><br/>
+最近查看+<br/>
<%if(list != null) {
for(int i = list.size() - 1; i >= 0; i--) {
		LookRecordBean lookBean = (LookRecordBean)list.get(i);
		ItemBean itemBean = ShopAction.shopService.getShopItemById(lookBean.getItemId());
		DummyProductBean productBean = UserBagCacheUtil.getItem(itemBean.getItemId());
%><a href="item.jsp?id=<%=itemBean.getId() %>"><%=productBean.getName()%></a><br/>
<%} }%>
<a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>