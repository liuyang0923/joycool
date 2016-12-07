<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.repair();
FarmNpcBean npc = (FarmNpcBean)request.getAttribute("npc");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%if(npc==null){%>
<a href="map.jsp">返回</a><br/>
<%}else{%>
<a href="repair.jsp">返回</a><br/>
<%}%>

<%}else{%>
现有:<%=FarmWorld.formatMoney(action.getUser().getMoney())%><br/>
<a href="repair.jsp?o=-2">修理所有装备</a><br/>
<%
FarmUserEquipBean[] equips = action.getUser().getEquip();
for(int i=0;i<FarmUserEquipBean.partCount;i++){
FarmUserEquipBean equip = equips[i];
if(equip != null && equip.getUserbagId()!=0){ 
UserBagBean userBag = UserBagCacheUtil.getUserBagCache(equip.getUserbagId());
DummyProductBean item = FarmWorld.getItem(userBag.getProductId());
%>
<%=item.getName()%>(<%=userBag.getTime()%>/<%=item.getTime()%>)
<%if(userBag.getTime()<item.getTime()){%><a href="repair.jsp?o=<%=userBag.getId()%>">修理</a><%}%><br/>
<%}else{%>

<%}%>
<%}%>
<a href="npc.jsp">返回</a><br/>
<%}%>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>