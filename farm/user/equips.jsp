<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.equips();
FarmWorld fWorld = action.world;
FarmUserEquipBean[] equips = action.getUser().getEquip();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
===当前装备<a href="../help/equips.jsp">??</a>===<br/>
<%
for(int i2=0;i2<FarmUserEquipBean.partCount;i2++){
int i=FarmUserEquipBean.partOrder[i2];
FarmUserEquipBean equip = equips[i];%>
<%=FarmUserEquipBean.getPartName(i)%>:
<% if(equip != null && equip.getUserbagId()!=0){ 
UserBagBean userBag = UserBagCacheUtil.getUserBagCache(equip.getUserbagId());
if(userBag==null) {
<a href="equipc.jsp?i=<%=i%>">(未知)</a><br/>
	continue;
}
DummyProductBean item = UserBagCacheUtil.getItem(userBag.getProductId());
%>
<a href="equip.jsp?i=<%=i%>"><%=item.getName()%></a>[<%=item.getGradeName()%>]<%=FarmWorld.getDurabilityInfo(userBag.getTime(),item.getTime())%><br/>
<%}else{%>
<a href="equipc.jsp?i=<%=i%>">(无)</a><br/>
<%}%>
<%}%>
<a href="../index.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>