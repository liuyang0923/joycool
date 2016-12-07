<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
int userbagId = action.getParameterInt("id");
UserBagBean userBag = (userbagId>0?UserBagCacheUtil.getUserBagCache(userbagId):null);
if(userBag==null){
response.sendRedirect(("bag.jsp"));
return;
}
DummyProductBean dummyProduct=UserBagCacheUtil.getItem(userBag.getProductId());
int part = action.getParameterIntS("i");		// 从装备处跳过来
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
*<%=dummyProduct.getName()%><%if(dummyProduct.getClass1()!=1&&dummyProduct.getClass1()!=2){%><%=userBag.getTimeString(dummyProduct)%><%}%>
<%if(dummyProduct.getRank()>0){%>
(等级<%=dummyProduct.getRank()%>)
<%}%>[<%=dummyProduct.getGradeName()%>]<br/>
<%if(dummyProduct.isFlagQuest()){%>(任务物品)<br/><%}%>
<%if(dummyProduct.getClass1()==1||dummyProduct.getClass1()==2){%>
耐久度:<%=userBag.getTime()%>/<%=dummyProduct.getTime()%><br/>
<%}%>
<%if(dummyProduct.getIntroduction().length()>0){%>
“<%=dummyProduct.getIntroduction()%>”<br/>
<%}%>
<%=FarmWorld.itemString(userBag)%>
<% ItemSetBean set = action.world.getItemToSet(dummyProduct.getId());
	if(set != null){	%>
--<a href="itemSet.jsp?id=<%=set.getId()%>"><%=set.getName()%></a>(套装)--<br/>
<%=action.world.itemSetString(set, null).toString()%>
<%}%>
<br/>
<%if(dummyProduct.getClass1()==4){%>
<a href="use.jsp?id=<%=userbagId%>">使用物品</a><br/>
<%}%>
<%if(part>=0){%>
<a href="equipc.jsp?a=1&amp;i=<%=part%>&amp;id=<%=userbagId%>">装备此物品</a><br/>
<a href="equipc.jsp?i=<%=part%>">返回装备页面</a><br/>
<%}%>
<a href="bag.jsp">返回行囊</a>
<%if(dummyProduct.isFlagCollect()){%>|<a href="collecti.jsp?id=<%=userbagId%>">收藏此物品</a><%}%><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>