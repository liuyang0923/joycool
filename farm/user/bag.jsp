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
<%
if(userBagList.size()>0){%>
<%
PagingBean paging = new PagingBean(action, userBagList.size(),15, "p");
Integer userBagId = null;
UserBagBean userBag = null;
DummyProductBean dummyProduct = null;
int endIndex = paging.getEndIndex();
for(int i=paging.getStartIndex();i<endIndex;i++){
    userBagId=(Integer)userBagList.get(i);
    userBag=UserBagCacheUtil.getUserBagCache(userBagId.intValue());
    dummyProduct=UserBagCacheUtil.getItem(userBag.getProductId());
    if(dummyProduct == null||dummyProduct.getClass1()==0) continue;%>
<%if(dummyProduct.getClass1()>0){%>
<a href="item.jsp?id=<%=userBagId.intValue()%>"><%=dummyProduct.getName()%><%if(dummyProduct.getClass1()!=1&&dummyProduct.getClass1()!=2){%><%=userBag.getTimeString(dummyProduct)%><%}%></a>
<%}else{%>
<%=dummyProduct.getName()%><%=userBag.getTimeString(dummyProduct)%>
<%}%>,
<%}%><br/>
<%}else{%>您的行囊中没有物品!<br/><%}%>
<a href="../index.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>