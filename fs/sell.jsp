<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.sell();
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
FSUserBagBean userProduct = (FSUserBagBean)request.getAttribute("userBag");
%>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您有:<%=userProduct.getName()%><br/>
成本:<%=userProduct.getPrice()%>元/个)<br/>
共计:<%=userProduct.getCount()%>个<br/>
卖<input name="count" format="*N" maxlength="10000" value="10"/>个<br/>
<anchor title="确定">确定
  <go href="sellResult.jsp" method="post">
    <postfield name="count" value="$count"/>
    <postfield name="productId" value="<%=userProduct.getProductId()%>"/>
  </go>
</anchor>|
<anchor title="确定">全部卖出
  <go href="sellResult.jsp" method="post">
    <postfield name="count" value="<%=userProduct.getCount()%>"/>
    <postfield name="productId" value="<%=userProduct.getProductId()%>"/>
  </go>
</anchor><br/>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>