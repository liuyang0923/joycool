<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.bank();
String result =(String)request.getAttribute("result");
String url=("/fs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=FSAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回城市)<br/>
<a href="/fs/index.jsp">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
FSUserBean fsUser = action.getFsUser();
%>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行<br/>
现金：<%=fsUser.getMoney()%>元<br/>
存款：<%=fsUser.getSaving()%>元<br/>
<anchor title="确定">存入所有钱
  <go href="bankResult.jsp" method="post">
    <postfield name="saveMoney" value="<%=fsUser.getMoney()%>"/>
  </go>
</anchor>|
<anchor title="确定">取出所有钱
  <go href="bankResult.jsp" method="post">
    <postfield name="getMoney" value="<%=fsUser.getSaving()%>"/>
  </go>
</anchor><br/>
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
<%}%>
</wml>