<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.equip();
FarmWorld fWorld = action.world;
FarmUserEquipBean[] equips = action.getUser().getEquip();
int i = action.getParameterInt("i");
FarmUserEquipBean equip = equips[i];
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{%>

==装备(<%=FarmUserEquipBean.getPartName(i)%>)==<br/>
<%if(equip!=null&&equip.getUserbagId()!=0){
UserBagBean userBag = UserBagCacheUtil.getUserBagCache(equip.getUserbagId());
DummyProductBean item = UserBagCacheUtil.getItem(userBag.getProductId());
%>
<%=item.getName()%><%if(item.getRank()>0){%>
(等级<%=item.getRank()%>)
<%}%>[<%=item.getGradeName()%>]<br/>
耐久度:<%=userBag.getTime()%>/<%=item.getTime()%><br/>
<%=FarmWorld.itemString(userBag)%>
<% ItemSetBean set = action.world.getItemToSet(item.getId());
	if(set != null){	
BattleStatus bs = action.getUser().getCurStat();
int count = bs.equipSetCount(set.getId());%>
--<a href="itemSet.jsp?id=<%=set.getId()%>"><%=set.getName()%></a>(套装)--<br/>
<%=action.world.itemSetString(set, count, null).toString()%>
<%}%>
<a href="equip.jsp?a=1&amp;i=<%=i%>">取消装备</a><br/>
<%}else{%>
(无)<br/>
<%}%>
<a href="equipc.jsp?i=<%=i%>">从行囊选择替换</a><br/>
<%}%>
<a href="equips.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>