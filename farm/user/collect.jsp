<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.UserBagBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmWorld fWorld = action.world;
FarmUserBean farmUser = action.getUser();
int id = action.getParameterInt("id");
UserCollectBean uc = fWorld.getUserCollect(id);
CollectBean collect = fWorld.getCollect(uc.getCollectId());
boolean showImg = request.getParameter("s") != null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
===<%=collect.getName()%>(<%=uc.getCount()%>/<%=collect.getCount()%>)===<br/>
<%=collect.getInfo()%><br/>

<%
switch(collect.getType()){
case 0:{
%>

<%
for(int i=0;i<collect.getCount();i++){
int itemId = ((Integer)collect.getItemList().get(i)).intValue();
DummyProductBean item = fWorld.getItem(itemId); %>
<%if(uc.hasCollect(i)){%><%=item.getName()%>(等级<%=item.getRank()%>)<br/>
<%if(showImg){%><img src="/farm/img/col/i<%=itemId%>.gif" alt="pic"/><br/><%}%>
<%}else{%><%=item.getName()%>(空缺)<br/><%}%>


<%}%>
<%if(!showImg){%><a href="collect.jsp?s=1&amp;id=<%=id%>">查看收藏图片</a><br/><%}%>

<%
}break;

case 1:{
%>

<%
for(int i=0;i<collect.getCount();i++){
int itemId = ((Integer)collect.getItemList().get(i)).intValue();
CreatureTBean item = FarmNpcWorld.one.getCreatureT(itemId); %>
<%if(uc.hasCollect(i)){%><%=item.getName()%>(等级<%=item.getLevel()%>)<br/>
<%=item.getInfo()%><br/>
<%--if(showImg){%><img src="/farm/img/col/i<%=itemId%>.gif" alt="pic"/><br/><%}--%>
<%}else{%><%=item.getName()%>(空缺)<br/><%}%>
<%}%>


<%
}break;

}
%>
建立:<%=DateUtil.formatDate2(uc.getStartTime())%><br/>
<%if(uc.getCount()>=collect.getCount()){%>
完成:<%=DateUtil.formatDate2(uc.getFinishTime())%><br/>
<%}%>
<a href="collects.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>