<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*,net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%
	response.setHeader("Cache-Control","no-cache");
	GardenService gardenService = GardenService.getInstance();
	GardenAction gardenAction = new GardenAction(request);
	int uid = gardenAction.getLoginUser().getId();
	GardenUserBean bean = GardenUtil.getUserBean(uid);
	//System.out.println(bean == null);
	if(bean == null) {
		CacheManage.noGardenCache.srm(uid);
		bean = gardenService.addGardenUser(uid);
		//默认两块田地
		
		for(int i = 0;i<GardenUtil.defaultCount;i++) {
			gardenService.addGardenField(uid);
			
		}
		GardenSeedBean seedBean = gardenService.getSeedBean(1);
		List list = gardenService.getUserFields(uid);
		for(int j = 0;j<2;j++){
			GardenFieldBean fieldBean = (GardenFieldBean)list.get(j);
			List lists = gardenService.getUserFields(uid);
			String condition = "seed_id = 1, result_start_time = 0, quarter = 1 where id = " + fieldBean.getId();
			gardenService.updateField(condition);
		}
		//for(int j = 2;j<4;j++){
			GardenFieldBean fieldBean = (GardenFieldBean)list.get(2);
			List lists = gardenService.getUserFields(uid);
			int hours = seedBean.getQuarterTime(1) - 1;
			int startTime = (int)(System.currentTimeMillis() / 1000) - hours * 3600;
			String condition = "seed_id = 1,grass=1, result_start_time = "+startTime+", quarter = 1 where id = " + fieldBean.getId();
			gardenService.updateField(condition);
			fieldBean = (GardenFieldBean)list.get(3);
			condition = "seed_id = 1,bug=1, result_start_time = "+startTime+", quarter = 1 where id = " + fieldBean.getId();
			gardenService.updateField(condition);
		//}
		GardenSeedBean seedBean2 = gardenService.getSeedBean(100);
		GardenSeedBean seedBean3 = gardenService.getSeedBean(1);
		gardenService.addGardenUserSeed(uid, seedBean2.getId(), 2,seedBean2.getType());
		gardenService.addGardenUserSeed(uid, seedBean3.getId(), 2,seedBean3.getType());
	}
	
	int limit = 10;
	int p = gardenAction.getParameterInt("p");
	int start = p*limit;
	
	List list = gardenService.getFields(" where uid = " + uid + " limit "+start+",11");
	int msgCount = bean.getMsgCount();	
	
	int count = list.size()>limit?limit:list.size();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
我的农场:<br/>
等级<%=GardenUtil.getLevel(bean.getExp()) %>-经验值:<%=GardenUtil.getCurrentExp(bean.getExp()) %>/<%=GardenUtil.getNeedExp(bean.getExp()) %><br/>
现金<%=bean.getGold() %>-<a href="info.jsp">消息<%=msgCount>0?""+msgCount:"" %></a>-<a href="historyStore.jsp">成果</a><br/>
【农田状态】<br/>
<%
int now = GardenUtil.getNowSec();
for(int i = 0; i < count; i ++) {
	GardenFieldBean fieldBean = (GardenFieldBean)list.get(i);
	
%><%if(fieldBean.getSeedId() == 0) {%>
-土地:空 <a href="feed.jsp?id=<%=fieldBean.getId() %>&amp;p1=<%=p %>">播种</a>
<%} else if(fieldBean.isGrown()){
	GardenSeedBean seedBean = gardenService.getSeedBean(fieldBean.getSeedId());
%>-<%=seedBean.getName()%>[成熟]果实<%=seedBean.getCount()-fieldBean.getBug()-fieldBean.getStealCount()-fieldBean.getGrass()%>/<%=seedBean.getCount()-fieldBean.getBug() %>
<%if(seedBean.getQuarter() > 1) {%>
第<%=fieldBean.getQuarter()%>季/共<%=seedBean.getQuarter() %>季<%} %>
<a href="pick.jsp?id=<%=fieldBean.getId() %>&amp;p1=<%=p %>">采摘</a>
<%} else {
	GardenSeedBean seedBean = gardenService.getSeedBean(fieldBean.getSeedId());
%>
-<%=seedBean.getName()%>[<%=fieldBean.getGrownStateStr() %>]剩<%=GardenAction.interval(fieldBean.getCurStateTimeLeft()) %><%=GardenSeedBean.getGrownStr(fieldBean.getNextGrownState(),fieldBean.getSeedId()) %>
<%if(seedBean.getQuarter() > 1) {%>
第<%=fieldBean.getQuarter()%>季/共<%=seedBean.getQuarter() %>季<%} %>.<a href="grown.jsp?id=<%=fieldBean.getId() %>&amp;p1=<%=p %>">施肥</a>
<%if(fieldBean.getGrass()>0||fieldBean.getBug()>0){%><br/><%}%><%if(fieldBean.getGrass() > 0) {%><a href="degrass.jsp?id=<%=fieldBean.getId() %>&amp;p1=<%=p %>">除草</a>(<%=fieldBean.getGrass() %>)
<%}if(fieldBean.getBug() > 0) {%><a href="debug.jsp?id=<%=fieldBean.getId() %>&amp;p1=<%=p %>">杀虫</a>(<%=fieldBean.getBug() %>)
<%}} %><br/><%} %>
<%if(bean.getFieldCount()<GardenUtil.condition.length+GardenUtil.defaultCount) {
	//if(GardenUtil.getLevel(bean.getExp()) >= (GardenUtil.condition[bean.getFieldCount() - GardenUtil.defaultCount][1])) {%>
<a href="expand.jsp?a=1">+扩充土地</a><br/>
<%} %>
<%
boolean flag = false;
if(list.size() > 10) {flag=true;%><a href="myGarden.jsp?p=<%=p+1%>">下一页</a><%}%>
<%if(p > 0) {flag=true;%><a href="myGarden.jsp?p=<%=p-1%>">上一页</a><%}%><%if(flag) {%><br/><%} %>
<a href="store.jsp">仓库</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a><br/><a href="friend.jsp">好友的农场</a><br/><a href="invite.jsp">邀请好友开通</a><br/>
<a href="s.jsp">返回农场首页</a><br/>
<a href="island.jsp">返回采集岛</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>