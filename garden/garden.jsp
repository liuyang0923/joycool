<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.spec.garden.*"%><%!
static net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();
%><%
	response.setHeader("Cache-Control","no-cache");
	
	GardenService gardenService = GardenService.getInstance();
	GardenAction gardenAction = new GardenAction(request);
	int uid = gardenAction.getParameterInt("uid");
	if(uid == gardenAction.getLoginUser().getId()) {
		response.sendRedirect("myGarden.jsp");
		return;
	}
	UserSettingBean us = gardenAction.getLoginUser().getUserSetting();
	boolean showLogo = us == null || !us.isFlagHideLogo();
	GardenUserBean bean = gardenService.getGardenUser(uid);
	
	boolean isABadGuys=service.isUserBadGuy(uid,gardenAction.getLoginUser().getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=GardenUtil.GAME_NAME%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if(isABadGuys){%>
你已经被对方加入黑名单,无法进入对方的农场.<br/>
<%}else { %>
<%if(bean == null) {%>
该用户未开通农场<br/>
<a href="/user/ViewUserInfo.do?userId=<%=uid%>">&gt;&gt;查看资料</a><br/>
<%}else{
	UserFriendBean userFriend = GardenAction.userService.getUserFriend(gardenAction.getLoginUser().getId(),uid);
	if(userFriend !=null) {
		int limit = 10;
		int p = gardenAction.getParameterInt("p");
		int start = p*limit;
		
		List list = gardenService.getFields(" where uid = " + uid + " limit "+start+",11");
		int msgCount = GardenAction.gardenService.getMessageCount("uid="+uid+" and readed=0");
		int count = list.size()>limit?limit:list.size();
%>
<a href="/home/home2.jsp?userId=<%=uid %>"><%=UserInfoUtil.getUser(uid).getNickNameWml() %></a>的农场:<br/>
等级<%=GardenUtil.getLevel(bean.getExp()) %>-经验值:<%=GardenUtil.getCurrentExp(bean.getExp()) %>/<%=GardenUtil.getNeedExp(bean.getExp()) %><br/>
【农田状态】.<a href="historyStore.jsp?uid=<%=uid %>">成果</a><br/>
<%
int now = GardenUtil.getNowSec();
for(int i = 0; i < count; i ++) {
	GardenFieldBean fieldBean = (GardenFieldBean)list.get(i);
	
%><%if(fieldBean.getSeedId() == 0) {%>
-土地:空<%} else if(fieldBean.isGrown()){	GardenSeedBean seedBean = gardenService.getSeedBean(fieldBean.getSeedId());
%>-<%=seedBean.getName()%>.成熟.果实<%=seedBean.getCount()-fieldBean.getBug()-fieldBean.getStealCount()-fieldBean.getGrass() %>/<%=seedBean.getCount()-fieldBean.getBug() %>
<%
GardenSeedBean seedbBean = GardenAction.gardenService.getSeedBean(fieldBean.getSeedId());
boolean flag = false;
if(seedbBean!=null)
	flag = fieldBean.getResultStartTime() + seedbBean.getQuarterTime(1)*3600 + 60 < GardenUtil.getNowSec();
if(flag) {
%>
<%if(!fieldBean.containStealUser(gardenAction.getLoginUser().getId())) {%><a href="steal.jsp?id=<%=fieldBean.getId() %>&amp;uid=<%=uid %>&amp;p1=<%=p %>">摘取</a><%} else {%>已摘取<%} %>
<%} else { %>
摘取
<%} %><%} else {
	GardenSeedBean seedBean = gardenService.getSeedBean(fieldBean.getSeedId());
	
%>
-<%=fieldBean.getBug() > 0 ? (showLogo?"<img src=\"img/bug.gif\" alt=\"虫\"/>":"[虫]"):""%><%=fieldBean.getGrass() > 0 ?(showLogo?"<img src=\"img/grass.gif\" alt=\"草\" />":"[草]"):""%><%=seedBean.getName()%>.<%=fieldBean.getGrownStateStr() %>.剩<%=GardenAction.interval(fieldBean.getCurStateTimeLeft()) %><%=GardenSeedBean.getGrownStr(fieldBean.getNextGrownState(),fieldBean.getSeedId()) %><br/>
<a href="doGrass.jsp?id=<%=fieldBean.getId() %>&amp;uid=<%=uid %>&amp;p1=<%=p %>">放草</a><%=fieldBean.getGrass()>0?"(" +fieldBean.getGrass()+ ")":"" %>.<a href="doBug.jsp?id=<%=fieldBean.getId() %>&amp;uid=<%=uid %>&amp;p1=<%=p %>">放虫</a><%=fieldBean.getBug()>0?"(" +fieldBean.getBug()+ ")":"" %><%if(fieldBean.getGrass()>0||fieldBean.getBug()>0) {%>|<%}%><%if(fieldBean.getGrass()>0) {%><a href="degrass.jsp?id=<%=fieldBean.getId() %>&amp;uid=<%=uid %>&amp;p1=<%=p %>">除草</a><%}if(fieldBean.getGrass()>0&&fieldBean.getBug()>0){%>.<%} if(fieldBean.getBug()>0) {%><a href="debug.jsp?id=<%=fieldBean.getId() %>&amp;uid=<%=uid %>&amp;p1=<%=p %>">杀虫</a><%} %>
<%} %><br/>
<%}
boolean flag = false;
if(list.size() > 10) {flag=true;%><a href="garden.jsp?uid=<%=uid %>&amp;p=<%=p+1%>">下一页</a><%}%>
<%if(p > 0) {flag=true;%><a href="garden.jsp?uid=<%=uid %>&amp;p=<%=p-1%>">上一页</a><%}%><%if(flag) {%><br/><%} %>
<%}else {%>不是你好友，不能浏览该用户的农场<br/><a href="/user/ViewUserInfo.do?userId=<%=uid%>">&gt;&gt;查看资料并将其加为好友</a><br/><%}} %>
<%} %>
<a href="friend.jsp">其他好友农场</a><br/>
<a href="myGarden.jsp">返回我的农场</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>