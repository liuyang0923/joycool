<%@include file="/farm/antiAuto.jsp"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.Tong2Action"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 300),
};
%><%
response.setHeader("Cache-Control","no-cache");

Tiny2Action action2 = new Tiny2Action(request, response);
if(action2.checkGame(4)) return;
if(action2.getGame() == null){
	if(net.joycool.wap.util.RandomUtil.nextInt(2) == 0){
		action2.startGame(games[0], 4);
		return;
	}
}else{
	request.setAttribute("extra-shop", new Integer(4));
}

Tong2Action action = new Tong2Action(request);
Tong2Action.Tong2User t2u = action.getUser2();
if(t2u.same&&!action.hasParam("s")){
	response.sendRedirect("shop2.jsp?tongId="+request.getParameter("tongId"));
	return;
}
action.shopEmpolder(request);
String result =(String)request.getAttribute("result");
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
<%}else if(result.equals("refrush")){
TongBean tong =(TongBean)request.getAttribute("tong");
out.clearBuffer();
response.sendRedirect("shop2.jsp?tongId="+tong.getId());
return;
}else if(result.equals("priceError")){
TongBean tong =(TongBean)request.getAttribute("tong");
String url=("tong.jsp?tongId="+tong.getId());
%>
<card title="帮会商店" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
String url=("shop2.jsp?tongId="+tong.getId());
%>
<card title="帮会商店" ontimer="<%=response.encodeURL(url)%>">
<timer value="10"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>
<a href="shop2.jsp?tongId=<%=tong.getId()%>">直接跳转</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>
</p>
</card>
<%}%>
</wml>
