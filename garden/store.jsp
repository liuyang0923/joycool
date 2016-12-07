<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	int uid = gardenAction.getLoginUser().getId();
	
	
	int cur = gardenAction.getParameterInt("p");
	int start = cur * 10;
	int limit = 11;
	List list = GardenAction.gardenService.getStoreList(" uid = " + uid + " limit "+start + ","+limit);
	List list2 = GardenAction.gardenService.getStoreList(" uid = " + uid);
	int count = list.size() > 10 ? 10 : list.size();
	//System.out.println(list.size());
	int all = 0;
	for(int i = 0; i < list2.size(); i++) {
		GardenStoreBean bean = (GardenStoreBean)list2.get(i);
		GardenSeedBean seedBean = GardenAction.gardenService.getSeedBean(bean.getSeedId());
		if(bean.getCount() > 0){
			all += seedBean.getValue()*bean.getCount();
		}
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
我的仓库<br/>
<%if(count > 0) {%>
<a href="sell.jsp">全部卖出</a><br/>
所有物品总价值:<%=all%><br/>
<%
for(int i = 0; i < count; i++) {
	GardenStoreBean bean = (GardenStoreBean)list.get(i);
	GardenSeedBean seedBean = GardenAction.gardenService.getSeedBean(bean.getSeedId());
	if(bean.getCount() > 0){
%>
<%=seedBean.getName()+bean.getCount()+"个." %>价格<%=seedBean.getValue()*bean.getCount()%>.<a href="sell.jsp?id=<%=bean.getId()%>">卖出</a><br/>
<%}} %>
<%if(list.size() > 10) {%><a href="store.jsp?p=<%=cur+1%>">下一页</a><%}%>
<%if(cur > 0) {%><a href="store.jsp?p=<%=cur-1%>">上一页</a><%}%><%if(list.size()>10 || cur>0) {%><br/><%} %>
<%}else { %>
你的仓库现在是空的，再不抓紧种地就要饿死了<br/>
<%} %><a href="myGarden.jsp">返回我的农场</a><br/>
<a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>