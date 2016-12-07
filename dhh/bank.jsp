<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.bank();
DhhUserBean dhhUser = action.getDhhUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行<br/>
现金：<%=dhhUser.getMoney()%>￥<br/>
存款：<%=dhhUser.getSaving()%>￥<br/>
<input name="saveMoney" format="*N" maxlength="9" value="1000"/>
<anchor title="确定">存
  <go href="bankResult.jsp" method="post">
    <postfield name="saveMoney" value="$saveMoney"/>
  </go>
</anchor><br/>
<input name="getMoney" format="*N" maxlength="9" value="1000"/>
<anchor title="确定">取
  <go href="bankResult.jsp" method="post">
    <postfield name="getMoney" value="$getMoney"/>
  </go>
</anchor><br/>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>