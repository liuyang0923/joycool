<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	int id = gardenAction.getParameterInt("id");
	if(id==0)
		id = 1;
	List types = GardenAction.gardenService.getSeedTypes();
	List seeds = GardenAction.gardenService.getSeeds(id);
	
	int p = gardenAction.getParameterInt("p");
	PagingBean paging = new PagingBean(gardenAction, seeds.size(), 10, "p");
	int uid = gardenAction.getLoginUser().getId();
	GardenUserBean bean = GardenUtil.getUserBean(uid);
	//gardenAction.expand();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
欢迎光临！本商店所售种子、化肥价格公道、童叟无欺，敬请放心选购。<br/>
分类:<%for(int i = 0; i < types.size(); i ++) {
	Object[] type = (Object[])types.get(i);
	if(((Integer)type[0]).intValue() != 4) {
	if(i > 0) {%>.<%} 
if(id == ((Integer)type[0]).intValue()) {%><%=((Integer)type[0]).intValue()==3?"化肥":type[1]%><%} else {%><a href="shop.jsp?id=<%=type[0]%>"><%=((Integer)type[0]).intValue()==3?"化肥":type[1]%></a><%} %><%} }%><br/>
<%if(seeds.size()>0) {%>
你有现金<%=bean.getGold() %><br/>
<%for(int i = paging.getStartIndex(); i < paging.getEndIndex(); i ++) {
	GardenSeedBean seed = (GardenSeedBean)GardenAction.gardenService.getSeedBean(((Integer)seeds.get(i)).intValue());%>
<a href="getSeed.jsp?id=<%=seed.getId() %>&amp;p=<%=p %>&amp;tid=<%=gardenAction.getParameterInt("id") %>"><%=seed.getName()%></a>.<a href="buy.jsp?id=<%=seed.getId() %>&amp;p=<%=gardenAction.getParameterInt("p") %>&amp;tid=<%=gardenAction.getParameterInt("id") %>">购买</a><br/>
-单价:<%=seed.getPrice() %><%if(id == 1) {%>-预计收入:<%=seed.getValue()*seed.getCount()*seed.getQuarter() %><%} %><br/>
<%} %>
<%=paging.shuzifenye("shop.jsp", false, "|", response)%>
<%} else {%>
暂未开放<br/>
<%} %>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>