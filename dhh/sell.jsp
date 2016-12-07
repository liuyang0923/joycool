<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.dhh.*" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.sell();
DhhUserBean dhhUser = action.getDhhUser();
UserBagBean product = (UserBagBean)request.getAttribute("product");
if(product==null){
response.sendRedirect(("market.jsp"));
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
你有<%=product.getProductname()%>
<%=product.getNumber()%>个<br/>
成本价：<%=product.getBuyrate()%><br/>
出售价：<%=action.getProductPrice(product)%><br/>
卖<input name="count" format="*N" maxlength="10000" value="10"/>个<br/>
<anchor title="确定">确定
  <go href="sellResult.jsp" method="get">
    <postfield name="count" value="$count"/>
    <postfield name="productId" value="<%=product.getProductid()%>"/>
  </go>
</anchor><br/>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>