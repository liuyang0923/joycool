<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.bank.*"%><%@ page import="net.joycool.wap.bean.bank.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
BankAction bankAction=new BankAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷银行">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=loginUser.showImg("/img/bank.gif")%>
银行经理：欢迎！您现在乐币<%=us.getGamePoint()%>,存款<%=bankAction.getUserStore()%>,贷款总额<%=bankAction.getUserLoadMoney()%>,想要什么服务?<br/>
<a href="bankPWHelp.jsp">防止丢币,设置银行密码</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>