<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
FarmUserBean farmUser = action.getUser();
action.sells();
Map sells = (Map)request.getAttribute("sells");
List userBagList = UserBagCacheUtil.getUserBagListCacheById(farmUser.getUserId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
现有:<%=FarmWorld.formatMoney(action.getUser().getMoney())%><br/>
<%
for(int i=0;i<userBagList.size();i++){
    Integer userBagId=(Integer)userBagList.get(i);
    UserBagBean userBag=UserBagCacheUtil.getUserBagCache(userBagId.intValue());
    FarmShopBean shop = (FarmShopBean)sells.get(Integer.valueOf(userBag.getProductId()));
    if(shop == null||shop.isFlagNoSell()) continue;
    DummyProductBean dummyProduct=UserBagCacheUtil.getItem(userBag.getProductId());
    if(dummyProduct == null) continue;%>
<a href="sell.jsp?id=<%=userBag.getId()%>"><%=dummyProduct.getName()%><%=userBag.getTimeString(dummyProduct)%></a>
<a href="sell.jsp?a=1&amp;id=<%=userBag.getId()%>">直接出售</a><br/>
<%}%>
<a href="npc.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>