<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.buys();
List buys = (List)request.getAttribute("buys");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
现有:<%=FarmWorld.formatMoney(action.getUser().getMoney())%><br/>
<%
for(int i=0;i<buys.size();i++){
FarmShopBean bean = (FarmShopBean)buys.get(i);
DummyProductBean item = FarmWorld.getItem(bean.getItemId());
if(bean.isFlagNoBuy()) continue;
%>
<a href="buy.jsp?id=<%=bean.getId()%>"><%=item.getName()%></a>(<%=StringUtil.bigNumberFormat(bean.getBuyPrice())%>)
买<a href="buy.jsp?a=1&amp;id=<%=bean.getId()%>">1个</a>|<a href="buy.jsp?a=1&amp;cnt=5&amp;id=<%=bean.getId()%>">5个</a><br/>
<%}%>
<a href="npc.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>