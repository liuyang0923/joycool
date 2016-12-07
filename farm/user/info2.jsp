<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
int id = action.getParameterInt("id");
FarmUserBean user = null;
if(id > 0)
	user = action.world.getFarmUserCache(id);
TongBean tong = null;
if(user.getTongUser()!=null)
	tong = FarmTongWorld.getTong(user.getTongUser().getTongId());
BattleStatus bs = user.getCurStat();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(user != null){
FarmUserEquipBean[] equips = user.getEquip();
%>

玩家:<%=user.getNameWml()%>(等级<%=user.getRank()%>)<br/>
职业:<%=user.getClass1Name()%>|门派:<%if(tong==null){%>(无)<%}else{%><a href="../tong/tong.jsp?id=<%=tong.getId()%>"><%=tong.getNameWml()%></a><%}%><br/>
战斗等级:<%=user.getProRank(FarmProBean.PRO_BATTLE)%><br/>
<%if(user.getPro(FarmProBean.PRO_BATTLE)!=null){%>
血:<%=user.hp*100/bs.hp%>%|
<%--气力:<%=user.mp*100/bs.mp%>%|--%>
体力:<%=user.sp*100/bs.sp%>%<br/>
==当前装备==<br/>
<%
for(int i2=0;i2<FarmUserEquipBean.partCount;i2++){
int i=FarmUserEquipBean.partOrder[i2];
FarmUserEquipBean equip = equips[i];%>
<%=FarmUserEquipBean.getPartName(i)%>:
<% if(equip != null && equip.getUserbagId()!=0){ 
UserBagBean userBag = UserBagCacheUtil.getUserBagCache(equip.getUserbagId());
DummyProductBean item = UserBagCacheUtil.getItem(userBag.getProductId());
%>
<%=item.getName()%>[<%=item.getGradeName()%>]<br/>
<%}else{%>
(无)<br/>
<%}%>
<%}%>
<%}%>
<%}else{%>
该人物不存在或者不在线<br/>
<%}%>

<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>