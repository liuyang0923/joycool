<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	
	gardenAction.sell();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(gardenAction.isResult("null")) {%>
你不能执行此操作<br/>
<%} else if(gardenAction.isResult("success")) {%>
成功卖出获得<%=request.getAttribute("price")%>金<br/>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%}%><a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>

<%=BaseAction.getBottomShort(request, response)%></p></card></wml>