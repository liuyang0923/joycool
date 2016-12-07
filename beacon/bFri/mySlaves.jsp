<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	ServiceSlave serviceSlave = ServiceSlave.getInstance();
	int start = 0;
	int cur = StringUtil.toId(request.getParameter("pageIndex"));
	int limit = 5;
	start = cur * limit;
	int total = serviceSlave.getSlavesCountByUid(loginUser.getId());
	List list = serviceSlave.getSlavesByUid(loginUser.getId(), start, limit);

	int totalPageCount = PageUtil.getPageCount(limit, total);
	String fenye = PageUtil.shuzifenye(totalPageCount, cur, "mySlaves.jsp", false, "|", response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="我的奴隶"><p><%=BaseAction.getTop(request, response)%>
【我的奴隶】<br/>
<%for(int i = 0; i < list.size(); i++) {
	BeanSlave slave = (BeanSlave)(list.get(i));%>
<a href="info.jsp?uid=<%=slave.getSlaveUid() %>"><%=StringUtil.toWml(slave.getSlaveNickName())%></a>[<%=StringUtil.toWml(slave.getSlaveAlias()) %>]<br/>
身价:<%=slave.getPrice() %>|现金:<%=slave.getMoney() %><br/>
<a href="<%=("appease.jsp?uid=" + slave.getSlaveUid() + "&amp;rank=" + slave.getRank())%>">安抚</a>|<a href="<%=("punish.jsp?uid=" + slave.getSlaveUid() + "&amp;rank=" + slave.getRank())%>">惩罚</a><br/>----------<br/>
<%}%><%if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="myInfo.jsp">返回朋友买卖首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>

