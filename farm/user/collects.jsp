<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmWorld fWorld = action.world;
FarmUserBean farmUser = action.getUser();
List cList = fWorld.getUserCollectList(farmUser.getUserId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
===收藏盒===<br/>
<%if(cList.size()==0){%>
（无）<br/>
<%}else{%>
<%
for(int i=0;i<cList.size();i++){
int[] iid = (int[])cList.get(i);
UserCollectBean uc = fWorld.getUserCollect(iid[0]);
CollectBean collect = fWorld.getCollect(uc.getCollectId());
if(collect==null) continue;
 %>
<a href="collect.jsp?id=<%=uc.getId()%>"><%=collect.getName()%></a>(<%=uc.getCount()%>/<%=collect.getCount()%>)<br/>
<%}%>

<%}%>
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>