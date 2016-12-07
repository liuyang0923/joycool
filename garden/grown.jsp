<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	int uid = gardenAction.getLoginUser().getId();
	int p = gardenAction.getParameterInt("p");
	int start = p * 5;
	List list = GardenAction.gardenService.getUserSeeds(" uid = " + uid + " and type = 3 and count > 0 limit " + start + ",6");
	int count = list.size() > 5?5:list.size();
	int p1 = gardenAction.getParameterInt("p1");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(count == 0) {%>
你还没有化肥,让你的作物成熟的更快<br/>
<a href="shop.jsp?id=3">&gt;&gt;购买化肥</a><br/>
<%}else{ %>
赶快选择你要使用的化肥吧 让你的作物成熟的更快<br/>
<%for(int i = 0; i < count; i++) {
	GardenUserSeedBean userSeedBean = (GardenUserSeedBean)list.get(i);
	GardenSeedBean seedBean = (GardenSeedBean)GardenAction.gardenService.getSeedBean(userSeedBean.getSeedId());
%>
<a href="getSeed.jsp?id=<%=seedBean.getId() %>"><%=seedBean.getName()%></a>.<%=userSeedBean.getCount() %>个.<a href="doGrown.jsp?fid=<%=request.getParameter("id")%>&amp;id=<%=userSeedBean.getId() %>&amp;p=<%=p1 %>">选择</a><br/>
<%}} %>
<a href="myGarden.jsp?p=<%=p1 %>">返回我的农场</a><br/>
<a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>