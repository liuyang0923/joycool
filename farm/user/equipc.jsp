<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.equipc();
FarmWorld fWorld = action.world;
FarmUserEquipBean[] equips = action.getUser().getEquip();
List userBagList = UserBagCacheUtil.getUserBagListCacheById(action.getUser().getUserId());
int index = action.getParameterInt("i");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<a href="equip.jsp?i=<%=index%>">替换装备</a><br/>
<%}else{%>

<%
Integer userBagId = null;
UserBagBean userBag = null;
DummyProductBean dummyProduct = null;
boolean none = true;
for(int i=0;i<userBagList.size();i++){
    userBagId=(Integer)userBagList.get(i);
    userBag=UserBagCacheUtil.getUserBagCache(userBagId.intValue());
    dummyProduct=UserBagCacheUtil.getItem(userBag.getProductId());
    if(dummyProduct == null||FarmUserEquipBean.partClass[index]!=dummyProduct.getClass2()) continue;
    none = false;
    %>
<a href="item.jsp?id=<%=userBagId%>&amp;i=<%=index%>"><%=dummyProduct.getName()%></a>-<a href="equipc.jsp?a=1&amp;i=<%=index%>&amp;id=<%=userBagId%>">选择</a><br/>
<%}%>
<%if(none){%>
没有可供选择的物品!<br/><%}%>

<%}%>
<a href="equips.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>