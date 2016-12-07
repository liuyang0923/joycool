<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%																																																																																																																																																																																														
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	//int uid = gardenAction.getLoginUser().getId();
	
	int id = gardenAction.getParameterInt("id");
	
	GardenSeedBean bean = GardenAction.gardenService.getSeedBean(id);
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(bean != null) {	
	if(bean.getType()==1){
%>
=<%=bean.getName() %>=<br/>
<img src="img/<%=bean.getId()%>.gif" alt="a" /><br/>
价格:<%=bean.getPrice() %><br/>
类型:<%=bean.getQuarter() %>季作物<br/>
成熟时间:<%=bean.getQuarterTime(1) %>小时<br/>
<%if(bean.getQuarter() > 1) {%>再成熟时间:<%=bean.getQuarterTime(2) %><br/><%} %>
预计产量:<%=bean.getCount()%>个<br/>
单个果实售价:<%=bean.getValue() %>金币<br/>
预计所得收入:<%=bean.getValue()*bean.getCount()*bean.getQuarter() %>金币<br/>
收获经验:<%=bean.getExp() %><br/>
种植等级:<%=bean.getLevel()>99?"??":bean.getLevel()%><br/>
<%if(bean.getInfo()!=null&&bean.getInfo().length()>5) {%>
作物信息:<%=bean.getInfo() %><br/>
<%} %>
<%} else if(bean.getType()==3){%>
=<%=bean.getName() %>=<br/>
<img src="img/<%=bean.getId()%>.gif" alt="a" /><br/>
价格:<%=bean.getPrice() %><br/>
说明:<%=bean.getInfo().replace("%1",GardenAction.interval(bean.getValue())) %><br/>
<%}} else { %>没有该作物<br/><%} %>
<a href="shop.jsp?p=<%=request.getParameter("p") %>&amp;id=<%=gardenAction.getParameterInt("tid") %>">返回</a>.<a href="buy.jsp?id=<%=request.getParameter("id") %>&amp;p=<%=gardenAction.getParameterInt("p") %>&amp;tid=<%=gardenAction.getParameterInt("tid") %>">购买</a><br/><a href="myGarden.jsp">返回我的农场</a><br/><a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/><%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>