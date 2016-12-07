<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
List userBagList = UserBagCacheUtil.getUserBagListCacheById(action.getUser().getUserId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
--使用物品--<br/>
<%
Integer userBagId = null;
UserBagBean userBag = null;
DummyProductBean dummyProduct = null;
boolean none = true;
for(int i=0;i<userBagList.size();i++){
    userBagId=(Integer)userBagList.get(i);
    userBag=UserBagCacheUtil.getUserBagCache(userBagId.intValue());
    dummyProduct=UserBagCacheUtil.getItem(userBag.getProductId());
    if(dummyProduct == null||dummyProduct.getClass1()!=4) continue;
    none = false;
    %>
<a href="item.jsp?id=<%=userBagId%>"><%=dummyProduct.getName()%><%=userBag.getTimeString(dummyProduct)%></a>-<a href="use.jsp?id=<%=userBagId%>">直接使用</a><br/>
<%}%>
<%if(none){%>
没有可供选择的物品!<br/><%}%>
<%if(action.getUser().getTargetList().size()>0){%>
<a href="../cb/cb.jsp">返回战斗</a><br/>
<%}%>
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>