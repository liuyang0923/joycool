<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
	ShopAction action = new ShopAction(request);
	int uid = action.getLoginUser().getId();
	int type = action.getParameterInt("t");
	String condition = "";
	if(type == 0) {
		condition = " and type in(0,1) order by time desc ";
	} else {
		condition = " and type in(0,1) order by gold desc ";
	}
	
	
	int count = ShopAction.shopService.getCountBuyRecord(uid, condition);
	
	PagingBean paging = new PagingBean(action, count, 5,"p");
	
	List list = ShopAction.shopService.getBuyRecord(uid, condition, paging.getStartIndex(), paging.getEndIndex());
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="历史记录"><p>
<%=BaseAction.getTop(request, response)%>
<%@include file="head.jsp"%>
购买.<a href="sendRecord.jsp?o=3">赠送</a>.<a href="receiveRecord.jsp?o=4">收礼</a>.<a href="/pay/myOrder.jsp?o=1">充值</a><br/>
您一共购买了<%=count %>件商品<br/>
<%if(type==0) {%>
按时间.<a href="record.jsp?t=1">按价格</a><br/>
<%}else { %>
<a href="record.jsp?t=0">按时间</a>.按价格<br/>
<%} 
for(int i = 0; i < list.size() ; i ++) {
		GoldRecordBean recordBean = (GoldRecordBean)list.get(i);
		if(recordBean.getType() == 0) {
		ItemBean bean = ShopAction.shopService.getShopItemById(recordBean.getItemId());
		if(bean != null) {
		DummyProductBean productBean = UserBagCacheUtil.getItem(bean.getItemId());
%>
<a href="item.jsp?id=<%=recordBean.getItemId()%>"><%=productBean.getName() %></a>.<%=ShopUtil.GOLD_NAME %><%=recordBean.getGoldString() %><br/>
<%=DateUtil.formatDate2(recordBean.getTime()) %><br/>
<%}} else if(recordBean.getType() == ShopUtil.RECORD_TYPE_PROPOSAL) { 
%>
购买求婚礼物花费<%=recordBean.getGoldString() %><%=ShopUtil.GOLD_NAME %><br/>
<%=DateUtil.formatDate2(recordBean.getTime()) %><br/>
<%		
}
} %>
<%=paging.shuzifenye("record.jsp?t="+type, true,"|", response) %>
<a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>