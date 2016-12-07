<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
FarmUserBean farmUser = action.getUser();
//int npcId = farmUser.getNpcId();
//FarmNpcBean npc = world.getNpc(npcId);
List userBagList = UserBagCacheUtil.getUserBagListCacheById(farmUser.getUserId());
List sells = new ArrayList(userBagList.size());
List sellsi = new ArrayList(userBagList.size());
for(int i=0;i<userBagList.size();i++){
    Integer userBagId=(Integer)userBagList.get(i);
    UserBagBean userBag=UserBagCacheUtil.getUserBagCache(userBagId.intValue());
    DummyProductBean item=UserBagCacheUtil.getItem(userBag.getProductId());
    if(item == null||item.getClass1()==0||item.getClass1()>9) continue;
    sells.add(userBag);
    sellsi.add(item);
}
PagingBean paging = new PagingBean(action,sells.size(),15,"p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
现有:<%=FarmWorld.formatMoney(action.getUser().getMoney())%><br/>
<%
for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
    UserBagBean userBag=(UserBagBean)sells.get(i);
    DummyProductBean item=(DummyProductBean)sellsi.get(i);%>
<a href="selle.jsp?id=<%=userBag.getId()%>"><%=item.getName()%></a>
<%=FarmWorld.formatSimpleMoney(item.getPrice()*userBag.getTime()/item.getTime())%>
<a href="selle.jsp?a=1&amp;id=<%=userBag.getId()%>">出售</a><br/>
<%}%>
<%=paging.shuzifenye("selles.jsp",false,"|",response)%>
<a href="npc.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>