<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.user.SendAction" %><%@ page import="net.joycool.wap.action.bank.BankAction" %><%
response.setHeader("Cache-Control","no-cache");
BankAction action = new BankAction(request);
action.bankPWCheck(request);
String result = (String)request.getAttribute("result");
String backTo = (String)request.getAttribute("backTo");
if(result!=null){
response.sendRedirect((backTo));
return;
}else{%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="银行密码">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请输入银行密码：<br/>
<input type="text" name="pw"  maxlength="15" value=""/><br/>
<anchor title="确定">确定
  <go href="bankLogin.jsp" method="post">
    <postfield name="pw" value="$pw"/>
    <postfield name="backTo" value="<%=backTo%>"/>
  </go>
</anchor><br/>
<%}%>
<a href="/Column.do?columnId=12778">?忘记银行密码</a><br/>
<a href="/user/userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>