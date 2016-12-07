<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock2.StockAction" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StockAction.STOCK_TITLE%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
敬请细读:炒股五步指南<br/>
炒股第五步:转乐币出股市花
赚钱了怎么办？在“我的账户信息”里选择“兑换乐币”，进去就能把股市中的自己转到社区花了。这里的转出是没有限制的，您赚了多少都能拿出去花。<br/>
<br/>
<a href="/stock2/help/step4.jsp">炒股第四步:等待系统交易</a><br/>
<a href="../index.jsp">返回股市大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>