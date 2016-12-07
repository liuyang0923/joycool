<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	
	gardenAction.feed();
	//int uid = gardenAction.getParameterInt("uid");
	int p1 = gardenAction.getParameterInt("p1");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>" ontimer="<%=response.encodeURL("/garden/myGarden.jsp?p="+p1)%>">
<timer value="30"/>
<p>
<%=BaseAction.getTop(request, response)%>
<%if(gardenAction.isResult("null")) {%>
你不能实现此操作<br/>
<%} else if(gardenAction.isResult("lack")) {%>
没有该作物<br/>
<a href="shop.jsp">&gt;&gt;去购买作物</a><br/>
<%} else if(gardenAction.isResult("success")) {%>
种植成功<%=request.getAttribute("exp")!=null?",经验增加"+request.getAttribute("exp")+"点":"" %><%=request.getAttribute("level")!=null?",恭喜你升级到"+request.getAttribute("level")+"级":"" %><br/>
<%} else if(gardenAction.isResult("has")) {%>
该地已经种植了作物<br/>
<%} else if(gardenAction.isResult("nolevel")) {%>
你的等级不够,需要等级<%=request.getAttribute("level") %><br/>
<%} %>
<a href="myGarden.jsp?p=<%=p1 %>">返回我的农场</a><br/>
<a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>