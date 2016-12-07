<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");

String id = request.getParameter("id");
String validated = request.getParameter("validated");
%>
<wml>
<card title="乐乐卖场">
<p align="left">
手机号认证说明<br/>
--------<br/>
<%
if(id == null){
%>
在乐乐卖场发布信息，凡留有联系手机号者，乐酷将帮忙验证其手机号是否真实(属于发布人)，如号码真实，则会在你的信息里加一个“已验证”标记，加上标记后你的信息可以让大家更信服哦~~<br/>
验证方法：请在发贴时按提示操作。<br/>
<%
}
else {
	if(validated == null){
%>
请使用您填写的联系号码，发送短信<%=id%>到13718998855，系统将在贴子里帮你标注手机号为已认证。<br/>
<%
	}
    else {
%>
你填写的电话号码已经自动通过真实性认证。<br/>
<%
	}
}
%>
<anchor>返回
  <prev/>
</anchor><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>