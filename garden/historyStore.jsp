<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenAction gardenAction = new GardenAction(request);
	int uid = gardenAction.getParameterInt("uid");
	boolean flag = false;
	if(uid==0) {
		uid = gardenAction.getLoginUser().getId();
		flag = true;
	}
	
	
	int cur = gardenAction.getParameterInt("p");
	int start = cur * 5;
	
	//int limit = 11;
	List list = GardenAction.gardenService.getHistoryStoreList(" uid = " + uid);
	int count = (list.size() - start)>10?10:((list.size() - start)>10?10:(list.size() - start));
	//PagingBean paging = new PagingBean(gardenAction, list.size(), 10, "p");
	int all = 0;
	int allCount = 0;
	for(int i = 0;i<list.size();i++){
		GardenStoreBean bean = (GardenStoreBean)list.get(i);
		GardenSeedBean seedBean = GardenAction.gardenService.getSeedBean(bean.getSeedId());
		allCount += bean.getCount();
		if(bean.getCount() > 0){
			all += seedBean.getValue()*bean.getCount();
		}
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%=flag?"我":UserInfoUtil.getUser(uid).getNickNameWml()%>的成果<br/>
<%if(count > 0) {
for(int i = start,j=0; j < count; j++) {
	GardenStoreBean bean = (GardenStoreBean)list.get(i+j);
	GardenSeedBean seedBean = GardenAction.gardenService.getSeedBean(bean.getSeedId());
	if(bean.getCount() > 0){
%><%=seedBean.getName()+bean.getCount()+"个." %>价格<%=seedBean.getValue()*bean.getCount()%><br/>
<%}} %>
所有物品总价值:<%=all%><br/>
<%if((list.size() - start) > 10) {%><a href="<%=flag?"historyStore.jsp?p="+(cur+1):"historyStore.jsp?uid="+uid+"&amp;p="+(cur+1)%>">下一页</a><%}%>
<%if(cur > 0) {%><a href="<%=flag?"historyStore.jsp?p="+(cur-1):"historyStore.jsp?uid="+uid+"&amp;p="+(cur-1)%>">上一页</a><%}%><%if((list.size() - start)>10 || cur>0) {%><br/><%} %>
<%}else { %>
还没有任何成果<br/>
<%} %><%=flag?"":"<a href=\"garden.jsp?uid="+uid+"\">返回"+UserInfoUtil.getUser(uid).getNickNameWml()+"的农场</a><br/>" %>
<a href="myGarden.jsp">返回我的农场</a><br/>
<a href="store.jsp">仓库</a>|<a href="shop.jsp">商店</a>|<a href="friend.jsp">好友</a>|<a href="bag.jsp">包裹</a><br/>
<%
if(GardenAction.gardenService.isContainStat(uid)) {
	GardenAction.gardenService.updateStat(uid,allCount,all);
}else {
	GardenAction.gardenService.addStat(uid,UserInfoUtil.getUser(uid).getNickName(),allCount,all);
}%><%=BaseAction.getBottomShort(request, response)%></p></card></wml>