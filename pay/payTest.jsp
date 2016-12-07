<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%
	response.setHeader("Cache-Control","no-cache");	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="金币充值"><p>
<%=BaseAction.getTop(request, response)%>
充值：<br/>
<select name="cardType">
<option value="2">测试</option>
<option value="1">联通</option>
<option value="0">神州行</option>
</select>
<select name="type">
<option value="50">50充值卡</option>
<option value="100">100充值卡</option>
</select><br/>
卡号:<input name="cardId" value=""/><br/>
密码:<input name="cardPwd" value=""/><br/>
<anchor>充值<go href="doPay.jsp" method="post">
<postfield name="type" value="$type"/>
<postfield name="cardType" value="$cardType"/>
<postfield name="cardId" value="$cardId"/>
<postfield name="cardPwd" value="$cardPwd"/>
</go></anchor><br/>
<a href="/shop/index.jsp">&gt;&gt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>