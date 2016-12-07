<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.shop(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml> 
<%if(result.equals("failure")){
String url=("tongList.jsp");
%>
<card title="帮会商店" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
%>
<card title="帮会商店">
<p align="left">
<%--<img src="../img/tong/hockshop.gif" alt=""/><br/>--%>
<%=BaseAction.getTop(request, response)%>
<a href="shop2.jsp?tongId=<%=tong.getId()%>">开发商店</a><br/>
欢迎来到道具商店,帮主和副帮主可以点击以下道具领取到行囊中:<br/>
<%if(tong.getShop()>=TongAction.TONG_SHOP_AGGRESSION){
	//if(tong.getUserId()==loginUser.getId() || tong.getUserIdA()==loginUser.getId() || tong.getUserIdB()==loginUser.getId()){%>
	<a href="shopInfo.jsp?productId=15&amp;tongId=<%=tong.getId()%>">轰天炮(1000)</a><br/>
	<%//}
}else{%>
	轰天炮(1000)<br/>
<%}%>
<%if(tong.getShop()>=TongAction.TONG_SHOP_ASSAULT_TIME){
	//if(tong.getUserId()==loginUser.getId() || tong.getUserIdA()==loginUser.getId() || tong.getUserIdB()==loginUser.getId()){%>
	<a href="shopInfo.jsp?productId=25&amp;tongId=<%=tong.getId()%>">攻城战鼓(9000)</a><br/>
	<%//}
}else{%>
	攻城战鼓(9000)<br/>
<%}%>
<%if(tong.getShop()>=TongAction.TONG_SHOP_RECOVERY){
	//if(tong.getUserId()==loginUser.getId() || tong.getUserIdA()==loginUser.getId() || tong.getUserIdB()==loginUser.getId()){%>
	<a href="shopInfo.jsp?productId=16&amp;tongId=<%=tong.getId()%>">防护膜(600)</a><br/>
	<%//}
}else{%>
	防护膜(600)<br/>
<%}%>
<%if(tong.getShop()>=TongAction.TONG_SHOP_RECOVERY_TIME){
	//if(tong.getUserId()==loginUser.getId() || tong.getUserIdA()==loginUser.getId() || tong.getUserIdB()==loginUser.getId()){%>
	<a href="shopInfo.jsp?productId=26&amp;tongId=<%=tong.getId()%>">守城战鼓(6000)</a><br/>
	<%//}
}else{%>
	守城战鼓(6000)<br/>
<%}%>
==========<br/>
商店开发度<%=tong.getShop()%><br/>
<a href="tongCityShopRecord.jsp?tongId=<%=tong.getId()%>">商店开发记录</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>
</p>
</card>
<%}%>
</wml>