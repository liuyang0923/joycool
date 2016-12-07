<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	int uid = gardenAction.getLoginUser().getId();
	int p = gardenAction.getParameterInt("p");
	int start = p * 5;
	List list = GardenAction.gardenService.getUserSeeds(" uid = " + uid + " and count > 0 limit " + start + ",6");
	int count = list.size() > 5?5:list.size();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
包裹里的物品<br/>
<%for(int i = 0; i < count; i++) {
	GardenUserSeedBean userSeedBean = (GardenUserSeedBean)list.get(i);
	GardenSeedBean seedBean = (GardenSeedBean)GardenAction.gardenService.getSeedBean(userSeedBean.getSeedId());
%>
<a href="getSeed.jsp?id=<%=seedBean.getId() %>"><%=seedBean.getName()%></a>.<%=userSeedBean.getCount() %>个<br/>
<%} %>
<%
boolean flag = false;
if(list.size() > 5) {flag=true;%><a href="bag.jsp?p=<%=p+1%>">下一页</a><%}%>
<%if(p > 0) {flag=true;%><a href="bag.jsp?p=<%=p-1%>">上一页</a><%}%><%if(flag) {%><br/><%} %>
<a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>