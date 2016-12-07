<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
int setId = action.getParameterInt("id");		// 从装备处跳过来
ItemSetBean set = action.world.getItemSet(setId);
BattleStatus bs = action.getUser().getCurStat();
int count = bs.equipSetCount(set.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(set==null){%>
不存在的物品<br/>
<%}else{%>
套装:<%=set.getName()%>(<%=count%>/<%=set.getItemCount()%>)<br/>
--套装物品--<br/>
<% List itemList = set.getItemList();
for(int i = 0;i < itemList.size();i++){
Integer iid = (Integer)itemList.get(i);
DummyProductBean item=UserBagCacheUtil.getItem(iid.intValue());
if(item == null) continue;
%>
<%=i+1%>.<%=item.getName()%>[<%if(bs.hasEquipItem(item.getId())){%><%=item.getGradeName()%><%}else{%>未装备<%}%>]<br/>
<%}%>
--套装属性--<br/>
<%=action.world.itemSetString(set, count, null).toString()%>
<%}%>
<a href="bag.jsp">返回行囊</a><br/>
<a href="equips.jsp">返回装备</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>