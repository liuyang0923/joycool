<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	
	gardenAction.grown();
	int uid = gardenAction.getParameterInt("uid");
	int p1 = gardenAction.getParameterInt("p1");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>" ontimer="<%=response.encodeURL("/garden/myGarden.jsp?p="+p1)%>">
<timer value="30"/>
<p>
<%=BaseAction.getTop(request, response)%>
<%if(gardenAction.isResult("null")) {%>
该阶段不能施肥<br/>
<%} else if(gardenAction.isResult("lack")) {%>
没有该化肥<br/>
<a href="shop.jsp">&gt;&gt;去购买作物</a>
<%} else if(gardenAction.isResult("success")) {%>
施肥成功,缩短成熟时间<%=GardenAction.interval(((Integer)(request.getAttribute("time"))).intValue()) %><br/>
<%} else if(gardenAction.isResult("has")) {%>
一个阶段只能施肥一次<br/>
<%} else if(gardenAction.isResult("nolevel")) {%>
你的等级不够,需要等级<%=request.getAttribute("level") %><br/>
<%} %>
<a href="myGarden.jsp?p=<%=p1 %>">返回我的农场</a><br/>
<a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>