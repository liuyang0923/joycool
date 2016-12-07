<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.collectItem();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>

<%}else{
int userbagId = action.getParameterInt("id");
UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userbagId);
DummyProductBean dummyProduct=UserBagCacheUtil.getItem(userBag.getProductId());
%>

选择收藏后,此物品将消失.<br/>
<a href="collecti.jsp?a=1&amp;id=<%=userbagId%>">确认收藏</a><br/>
<a href="item.jsp?id=<%=userbagId%>">返回</a><br/>
<%}%>
<a href="bag.jsp">返回行囊</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>