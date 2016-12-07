<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	ServiceMaster serviceMaster = ServiceMaster.getInstance();
	ServiceSlave serviceSlave = ServiceSlave.getInstance();
	
	ServiceTrend serviceTrend = ServiceTrend.getInstance();
	BeanMaster master = new BeanMaster();
	BeanSlave slave = null;
	List list = new ArrayList();
	int total = 0;
	if(loginUser != null){
		master = serviceMaster.getMasterByUid(loginUser.getId());
		slave = serviceSlave.getSlaveBySlaveUid(loginUser.getId());
		list = serviceTrend.getGameTrendByType(loginUser.getId(), BeanTrend.TYPE_BUY_FRIEND, 0, 5);
		total = serviceSlave.getSlavesCountByUid(loginUser.getId());
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="朋友买卖"><p><%=BaseAction.getTop(request, response)%>
朋友=奴隶or你=奴隶？<br/>
【我目前的状态】<br/>
<%if(slave != null) {%>
我目前是<a href="<%=("info.jsp?uid="+slave.getMasterUid()) %>"><%=StringUtil.toWml(slave.getMasterNickName()) %></a>的<%=slave.getSlaveTypeString() %><br/>
外号是:<%=StringUtil.toWml(slave.getSlaveAlias()) %><br/><%} else {%>我目前是自由身<br/><%}%>
我的身价:<%=master.getPrice() %><br/>
我的现金:<%=master.getMoney() %><%if(master.getMoney()<1000&&master.isSalaryTime()){%>|<a href="salary.jsp">领取今日补助</a><%}%><br/>
<a href="<%=("mySlaves.jsp") %>">我的奴隶</a>(<%=total %>)<br/>
<%if(slave != null) {%>
<a href="<%=("/chat/post.jsp?toUserId=" + slave.getMasterUid()) %>">和主人对话</a>|<a href="<%=("ransom.jsp") %>">赎身</a><br/>
<%} %>
【购买奴隶的途径】<br/>
1.<a href="myFris.jsp">买下我在乐酷的好友</a><br/>
让你的好友成你的奴隶<br/>
2.<a href="i.jsp">邀请朋友当我的奴隶</a><br/>
既可赚钱又能得到奴隶<br/>
【我的游戏动态】<%if(list.size() == 5) {%><a href="<%=("allTrend.jsp") %>">更多</a><%}else if(list.size()==0){%><br/>(暂无)<%}%><br/>
<%	if(list.size() > 0) {
	for(int i = 0; i < list.size(); i++) {
		BeanTrend trend = (BeanTrend)list.get(i);
%>*<%=trend.getBuyFriendContent(loginUser.getId(), response) %><br/><%}}%>
【<a href="h.jsp">游戏帮助和介绍</a>】<br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>

