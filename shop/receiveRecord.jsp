<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.dummy.*,net.joycool.wap.cache.*,net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
	ShopAction action = new ShopAction(request);
	int uid = action.getLoginUser().getId();
	//int type = action.getParameterInt("t");
	String condition = " user_id = " + uid + " and type in(0,1) order by time desc ";
	
	
	int count = ShopAction.shopService.getCountRecord(condition);
	
	PagingBean paging = new PagingBean(action, count, 5,"p");
	
	List list = ShopAction.shopService.getRecord(condition, paging.getStartIndex(), paging.getEndIndex());
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="历史记录"><p>
<%=BaseAction.getTop(request, response)%>
<%@include file="head.jsp"%>
<a href="record.jsp">购买</a>.<a href="sendRecord.jsp">赠送</a>.收礼.<a href="/pay/myOrder.jsp">充值</a><br/>
您一共收到了<%=count %>件商品<br/>
<%
for(int i = 0; i < list.size() ; i ++) {
		GoldRecordBean recordBean = (GoldRecordBean)list.get(i);
		UserBean user = UserInfoUtil.getUser(recordBean.getUid());
		ItemBean bean = ShopAction.shopService.getShopItemById(recordBean.getItemId());
		
		DummyProductBean productBean = UserBagCacheUtil.getItem(bean.getItemId());
%>
收到<a href="/home/home.jsp?userId=<%=recordBean.getUserId() %>"><%=user.getNickNameWml() %></a>赠送的【<a href="item.jsp?id=<%=recordBean.getItemId()%>"><%=productBean.getName() %></a>】.<%=ShopUtil.GOLD_NAME %><%=recordBean.getGoldString() %><br/>
<%=DateUtil.formatDate2(recordBean.getTime()) %><br/>
<%}%>
<%=paging.shuzifenye("receiveRecord.jsp", false,"|", response) %>
<a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>