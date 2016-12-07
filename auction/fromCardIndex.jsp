<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%
response.setHeader("Cache-Control","no-cache");
AuctionAction action = new AuctionAction(request);
action.fromCardIndex(request);
String result =(String)request.getAttribute("result");
String url=("/job/card/buyCard.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="拍卖系统" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转我的行囊!)<br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p> 
</card>
<%}else{
DummyProductBean dummyProduct=(DummyProductBean)request.getAttribute("dummyProduct");
%>
<card title="拍卖系统">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您决定拍卖<%=dummyProduct.getName()%>,拍卖大厅将收取您物品起价的10%作为手续费。<br/>
起价:<br/>
<input name="startPice" format="*N" maxlength="10" value="100"/>乐币<br/>
一口价<br/>
<input name="bitePrice" format="*N" maxlength="10" value="200"/>乐币<br/>
    <anchor title="确定">确定
    <go href="fromCardResult.jsp" method="post">
    <postfield name="productId" value="<%=dummyProduct.getId()%>"/>
    <postfield name="startPice" value="$startPice"/>
	<postfield name="bitePrice" value="$bitePrice"/>
    </go>
    </anchor><br/>
<a href="/job/card/buyCard.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>