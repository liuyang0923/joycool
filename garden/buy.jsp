<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	//int uid = gardenAction.getLoginUser().getId();
	int a = gardenAction.getParameterInt("a");
	if(a == 1)
		gardenAction.buy();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(a!=1) {
	int id = gardenAction.getParameterInt("id");
	GardenSeedBean bean = GardenAction.gardenService.getSeedBean(id);
	if(bean == null) {
		out.clearBuffer();
		response.sendRedirect("index.jsp");
		return;
	}
%>
购买【<%=bean.getName() %>】<br/>
价格:<%=bean.getPrice() %><br/>
快捷购买:<br/>
<a href="buy.jsp?id=<%=id%>&amp;a=1&amp;amount=1">1个</a>|<a href="buy.jsp?id=<%=id%>&amp;a=1&amp;amount=2">2个</a>|<a href="buy.jsp?id=<%=id%>&amp;a=1&amp;amount=3">3个</a>|<a href="buy.jsp?id=<%=id%>&amp;a=1&amp;amount=4">4个</a>|<a href="buy.jsp?id=<%=id%>&amp;a=1&amp;amount=5">5个</a><br/>
输入购买数量:<br/>
<input name="amount" format="*N" maxlength="2"/>
<anchor title="购买">购买<go href="buy.jsp" method="post">
<postfield name="id" value="<%=id %>"/>
<postfield name="a" value="1"/>
<postfield name="amount" value="$amount" />
</go></anchor><br/>
<%} else { %>
<%if(gardenAction.isResult("null")) {%>
没有该种子<br/>
<%} else if(gardenAction.isResult("lack")) {%>
金币不足,不能购买<br/>
<%} else if(gardenAction.isResult("success")) {%>
成功购买<%=request.getAttribute("amount")%>个<a href="getSeed.jsp?id=<%=request.getParameter("id") %>"><%=request.getAttribute("name") %></a><br/>
<%} else if(gardenAction.isResult("nograde")) {%>
等级不够,需要等级<%=request.getAttribute("level") %>级<br/>
<%} else if(gardenAction.isResult("nozero")){%>
购买数量不能为0<br/>
<%} else if(gardenAction.isResult("max")){
%>
购买数量不能大于99<br/>
<%
}}%><a href="shop.jsp?id=<%=request.getParameter("tid") %>&amp;p=<%=request.getParameter("p") %>">返回商店</a><br/>
<a href="myGarden.jsp">返回我的农场</a><br/>
<a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>