<%@ page language="java" pageEncoding="utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.service.impl.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	UserServiceImpl userService = new UserServiceImpl();
	response.setHeader("Cache-Control","no-cache");
	int uid = StringUtil.toId(request.getParameter("uid"));
	
	if(uid == 0) {
		uid = StringUtil.toId(request.getParameter("userId"));
		if(uid == 0) {
			response.sendRedirect("/beacon/bFri/myInfo.jsp");
			return;
		}
	}
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	ServiceMaster serviceMaster = ServiceMaster.getInstance();
	ServiceSlave serviceSlave = ServiceSlave.getInstance();
	
	BeanMaster master = serviceMaster.getMasterByUid(uid);
	BeanSlave slave = serviceSlave.getSlaveBySlaveUid(uid);
	
	ServiceTrend serviceTrend = ServiceTrend.getInstance();
	List list = serviceTrend.getGameTrendByType(uid, BeanTrend.TYPE_BUY_FRIEND, 0, 5);
	int total = serviceSlave.getSlavesCountByUid(uid);
	String gender = UserInfoUtil.getUser(uid).getGenderText();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="朋友买卖"><p><%=BaseAction.getTop(request, response)%>
【<%=StringUtil.toWml(master.getNickName()) %>】<br/>
<%if(slave != null) {%>
<%=gender%>现在是<%if(loginUser.getId() != slave.getMasterUid()) {%><a href="<%=("info.jsp?uid="+slave.getMasterUid()) %>"><%=StringUtil.toWml(slave.getMasterNickName()) %></a><%}else{%>我<%}%>的<%=slave.getSlaveTypeString() %>[<%=StringUtil.toWml(slave.getSlaveAlias()) %>]<br/>
<%} else {%><%=gender%>目前为自由身<br/><%}%>

身价:<%=master.getPrice() %>|现金:<%=master.getMoney() %><br/>
<%if(total > 0) {%><a href="uS.jsp?uid=<%=uid %>"><%=gender %>的奴隶</a><%} else {%><%=gender %>的奴隶<%} %>(<%=total%>)<br/>
<%if(userService.getUserFriend(loginUser.getId(), uid) == null 
			|| userService.getUserFriend(uid, loginUser.getId()) == null) {%>
*<%=UserInfoUtil.getUser(uid).getGenderText() %>还没有加你为好友，无法购买*<br/>
<a href="/chat/post.jsp?roomId=0&amp;toUserId=<%=uid%>">&gt;&gt;给<%=gender%>留言</a><br/>
<%} else {
	if(slave==null || loginUser.getId() != slave.getMasterUid()) {
%>
<a href="<%=("bFri.jsp?uid="+ master.getUid() + "&amp;n=b") %>">&gt;&gt;我要购买<%=UserInfoUtil.getUser(uid).getGenderText() %></a><br/>
<%} else if(loginUser.getId() == slave.getMasterUid()){%>
<a href="<%=("beFree.jsp?uid=" + slave.getSlaveUid())%>">&gt;&gt;还他自由</a><br/>
<%}}%>
--<%=gender %>的最近动态--<%if(list.size()==0){%><br/>(暂无)<%}%><br/>
<%	if(list.size() > 0) {
	for(int i = 0; i < list.size(); i++) {
		BeanTrend trend = (BeanTrend)list.get(i);
%>*<%=trend.getContentNoUserLink(loginUser.getId(), response).replace("home2.jsp","info.jsp") %><br/><%}}%>
<%if(slave==null || loginUser.getId() != slave.getMasterUid()) { %>
<a href="myFris.jsp">返回购买奴隶</a><br/>
<%} else {%>
<a href="mySlaves.jsp">返回我的奴隶</a><br/>
<%} %>
<a href="myInfo.jsp">返回朋友买卖首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>

