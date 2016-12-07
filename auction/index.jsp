<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBagBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.AuctionAction"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("loginUser")==null){
response.sendRedirect("/user/login.jsp");
return;
}
AuctionAction action = new AuctionAction(request);
action.index(request);
String result =(String)request.getAttribute("result");
String url=("/user/userBag.jsp");
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
if(dummyProduct==null||dummyProduct.isFlag(0)&&dummyProduct.getBrush()==0){%>
<card title="拍卖系统" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
物品无法拍卖(3秒后跳转我的行囊!)<br/>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p> 
</card>

<%}else{
String userBagId=(String)request.getAttribute("userBagId");
UserBagBean userBag = UserBagCacheUtil.getUserBagCache(StringUtil.toInt(userBagId));
%>
<card title="拍卖系统">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您决定拍卖<%=dummyProduct.getName()%><%=userBag.getTimeString(dummyProduct)%>,拍卖大厅将收取您物品成交价的5%作为手续费。<br/>
起价:<br/>
<input name="startPice" format="*N" maxlength="20" value="100"/>乐币<br/>
一口价<br/>
<input name="bitePrice" format="*N" maxlength="20" value="200"/>乐币<br/>
    <anchor title="确定">确定
    <go href="sale.jsp" method="post">
    <postfield name="startPice" value="$startPice"/>
    <postfield name="userBagId" value="<%=userBagId%>"/>
	<postfield name="bitePrice" value="$bitePrice"/>
    </go>
    </anchor><br/>
<a href="/user/userBagUse.jsp?userBagId=<%=userBagId%>">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}}%>
</wml>