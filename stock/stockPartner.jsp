<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
StockAction action = new StockAction(request);
action.stockPartner(request);%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="公司十大股东">
<p align="left">
<%=BaseAction.getTop(request, response)%>
公司十大股东：<br/>
<%Vector partner=(Vector)request.getAttribute("partner");
String id=(String)request.getAttribute("id");
for(int i=0;i<partner.size();i++)
{String userId=(String)partner.get(i);
UserBean user=(UserBean)UserInfoUtil.getUser(StringUtil.toInt(userId));%>
<%=i+1%>.
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
<%}%>
<br/>
<a href="/stock/stockInfo.jsp?id=<%=id%>">返回个股信息</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>