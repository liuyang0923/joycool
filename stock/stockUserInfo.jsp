<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.stock.StockAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser= (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean status=(UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
StockAction action = new StockAction(request);
long price=action.stockUserInfo(request);%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="用户信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
用户信息：<br/><br/>
昵称：<%=loginUser.getNickName()%><br/>
开户时间：<%if(status.getStockDatetime()!=null){%>
<%=status.getStockDatetime()%>
<%}else{%>
您还未开户！买支股票就开户拉！
<%}%>
<br/>
资金：<%=status.getGamePoint()%>乐币<br/>
总资金：<%=status.getGamePoint()+price%>乐币<br/>
股票市值：<%=price%>乐币<br/>
<br/>
<a href="/stock/stockAccount.jsp">返回我的帐户</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
