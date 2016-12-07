<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.user.SendAction" %><%@ page import="net.joycool.wap.action.bank.BankAction" %><%
response.setHeader("Cache-Control","no-cache");
BankAction action = new BankAction(request);
action.bankPWRs(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/bank/bankPW.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="银行密码设置" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后自动跳转)<br/>
<a href="/bank/bankPW.jsp">返回银行密码设置</a><br/>
<a href="/user/userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
url=("/user/userInfo.jsp");
%>
<card title="银行密码设置"  ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后自动跳转)<br/>
<a href="/user/userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>