<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmWorld fWorld = action.world;
FarmUserBean farmUser = action.getUser();
List cList = fWorld.collectList;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
现有:<%=FarmWorld.formatMoney(action.getUser().getMoney())%><br/>
===收藏盒===<br/>
<%
for(int i=0;i<cList.size();i++){
CollectBean collect = (CollectBean)cList.get(i);
if(collect.getRank()>farmUser.getRank()) continue;
 %>
<a href="collect.jsp?id=<%=collect.getId()%>"><%=collect.getName()%>(<%=collect.getCount()%>)</a>-
<a href="collect.jsp?a=1&amp;id=<%=collect.getId()%>">直接购买</a><br/>
<%}%>
<a href="npc.jsp">返回</a>|<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>