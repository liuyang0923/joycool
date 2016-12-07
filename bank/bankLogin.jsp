<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.user.SendAction" %><%@ page import="net.joycool.wap.action.bank.BankAction" %><%
response.setHeader("Cache-Control","no-cache");
BankAction action = new BankAction(request);
action.bankLogin(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String backTo = StringUtil.toWml((String)request.getAttribute("backTo"));
String url=("/bank/bankPWCheck.jsp?backTo="+backTo);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="银行账户登录" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后自动跳转)<br/>
<a href="/bank/bankPWCheck.jsp">返回银行登录</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
url=(backTo);
%>
<card title="银行账户登录" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后自动跳转)<br/>
<a href="<%=url%>">直接进入</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>