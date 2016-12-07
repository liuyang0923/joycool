<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.PropAction"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%@ page import="java.util.Vector"%><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 1800)
};
%><%
response.setHeader("Cache-Control","no-cache");
PropAction action = new PropAction(request);
if(ForbidUtil.isForbid("game",action.getLoginUser().getId())){
	response.sendRedirect("/enter/not.jsp");
	return;
}
int dummyProductId = action.getParameterInt("dummyId");
int checkTime = action.getParameterInt("t");
DummyProductBean dummyProduct = action.getDummyService().getDummyProducts(dummyProductId);

Tiny2Action action2 = new Tiny2Action(request, response);
if(action2.checkGame()) return;

if(checkTime!=dummyProduct.getStartTime().getTime()/60000%10000){
	response.sendRedirect("propShow.jsp?dummyId="+dummyProductId);
	return;
}

if(dummyProduct.getBrush()>0){
	if (System.currentTimeMillis() - dummyProduct.getStartTime().getTime() >= dummyProduct.getBrush() * 60 * 1000) {
		if(action2.getGame() == null){
			action2.startGame(games[0]);
			return;
		}
	}else{
		response.sendRedirect("propShow.jsp?dummyId="+dummyProductId);
		return;
	}
}

action.propBuy(request);
String result =(String)request.getAttribute("result");
if(result==null)
	result="refrush";
String url = ("propShop.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="神秘商店"  ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转神秘商店!)<br/>
<a href="propShop.jsp">神秘商店</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
out.clearBuffer();
response.sendRedirect("propShop.jsp");
return;
}else{
%>
<card title="神秘商店"  ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=request.getAttribute("tip") %>(3秒后跳转神秘商店!)<br/>
<%=BaseAction.getTop(request, response)%>
<a href="propShop.jsp">神秘商店</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>