<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	int uid = StringUtil.toId(request.getParameter("uid"));
	
	if(uid == 0) {
		response.sendRedirect("/beacon/bFri/myInfo.jsp");
	}
	String action = request.getParameter("action");
	boolean flag = false;
	if(action != null && action.equals("free")){
		flag = true;
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="还TA自由"><p><%=BaseAction.getTop(request, response)%>
<%if(!flag) {
	ServiceSlave serviceSlave = ServiceSlave.getInstance();
	BeanSlave slave = serviceSlave.getSlaveBySlaveUid(uid);
%>
<a href="<%=("info.jsp?uid="+slave.getSlaveUid()) %>"><%=StringUtil.toWml(slave.getSlaveNickName()) %></a>眼巴巴的看着你，渴望你能给他自由。不过还他自由之后，你将损失￥<%=slave.getBuyPrice()%>，是否还他自由?<br/>
<a href="<%=("beFree.jsp?uid=" + uid + "&amp;action=free")%>">确定</a>|<a href="<%=("mySlaves.jsp")%>">返回</a>
<%} else { 
	ServiceSlave serviceSlave = ServiceSlave.getInstance();
	ServiceMaster serviceMaster = ServiceMaster.getInstance();
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	//BeanSlave slave = serviceSlave.getSlaveBySlaveUid(uid);
	serviceSlave.deleteSlave(loginUser.getId(), uid);
	UserBean userBean = UserInfoUtil.getUser(uid);
	//奴隶数减1
	serviceMaster.increaseMoneyByUid(loginUser.getId(), 0, true);
	serviceSlave.deletePunish(uid);
%>你给了<%=StringUtil.toWml(userBean.getNickName()) %>自由，<%=StringUtil.toWml(userBean.getNickName()) %>感动的痛哭流涕，你们依依不舍的相互告别<br/>
<a href="<%=("mySlaves.jsp")%>">返回我的奴隶</a><%}%><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>

