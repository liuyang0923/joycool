<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.user.SendAction" %><%@ page import="net.joycool.wap.action.bank.BankAction" %><%
response.setHeader("Cache-Control","no-cache");
BankAction action = new BankAction(request);
action.bankPW(request);
String result = (String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="银行密码设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(result!=null){%>
旧密码:<br/>
<input type="text" name="opw" value=""/><br/>
新密码：<br/>
<input  type="text" name="npw"  maxlength="15" value=""/><br/>
<anchor title="确定">修改
  <go href="bankPWRs.jsp" method="post">
    <postfield name="opw" value="$opw"/>
    <postfield name="npw" value="$npw"/>
  </go>
</anchor><br/>
<%}else{%>
新密码：<br/>
<input type="text" name="npw"  maxlength="15" value=""/><br/>
<anchor title="确定">确定
  <go href="bankPWRs.jsp" method="post">
    <postfield name="npw" value="$npw"/>
  </go>
</anchor><br/>
<%}%>
<a href="/Column.do?columnId=12778">?忘记银行密码</a><br/>
<a href="/user/userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>