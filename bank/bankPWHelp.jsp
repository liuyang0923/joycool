<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="银行密码帮助">
<p align="left">
<%=BaseAction.getTop(request, response)%>
可以设定每次进入银行的密码，密码为15位以下数字或字母，请牢记！如果不设定或设定为空，则进入银行不需要密码。<br/><br/>
<a href="/bank/bankPW.jsp">设置银行密码</a><br/>
<br/>
<a href="/user/userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>