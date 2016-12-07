<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	int uid = StringUtil.toId(request.getParameter("uid"));
	
	if(uid == 0) {
		response.sendRedirect("/beacon/bFri/myInfo.jsp");
		return;
	}
	
	String action = request.getParameter("action");
	boolean flag = false;
	if(action != null && action.equals("discount")){
		flag = true;
	}
	
	ServiceSlave serviceSlave = ServiceSlave.getInstance();
	BeanSlave slave = serviceSlave.getSlaveBySlaveUid(uid);
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="打折处理"><p><%=BaseAction.getTop(request, response)%>
<%if(slave == null) {%>
该奴隶为自由身<br/><a href="<%=("mySlaves.jsp")%>">返回我的奴隶</a><br/>
<%} else if(slave.getPrice() <= 600) {%>
<%=StringUtil.toWml(slave.getSlaveNickName()) %>身价没有超过600元不能被打折<br/>
<a href="<%=("mySlaves.jsp")%>">返回我的奴隶</a><br/>
<%}else{%>
<%if(!flag) {
	int discount = (int)(slave.getPrice() * 0.5);%>
<a href="<%=("/home/home2.jsp?userId="+slave.getSlaveUid()) %>"><%=StringUtil.toWml(slave.getSlaveNickName()) %></a>现在身价是￥<%=slave.getPrice() %>打折处理之后，他的身价降至<%=discount%>，你也将损失<%=slave.getPrice() - discount %>，是否打折?<br/>
<a href="<%=("discount.jsp?uid=" + uid + "&amp;action=discount")%>">确定</a>|<a href="<%=("mySlaves.jsp")%>">返回</a>
<%} else {
	ServiceMaster serviceMaster = ServiceMaster.getInstance();
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	
	if(slave == null) {
%>
打折失败,您的奴隶已经赎身<br/>
<%
	} else if(slave.getMasterUid() != loginUser.getId()) {
%>
打折失败,您的奴隶已经被买走<br/>
<%
	}else if(serviceSlave.isPunish(uid)) {
%>
该奴隶正在受惩罚中，不能打折处理<br/>
<%
	}else{
		serviceSlave.deletePunish(uid);
		//serviceSlave.deleteSlave(loginUser.getId(), uid);
			
		UserBean userBean = UserInfoUtil.getUser(uid);
			//奴隶数减1,返回50%的金钱
		//int price = (int)(slave.getBuyPrice() * 0.5);
		serviceMaster.decreasePricePercentByUid(uid, 0.5f);
		//serviceMaster.increaseMoneyByUid(loginUser.getId(), price, true);
%>
<%=StringUtil.toWml(userBean.getNickName()) %>已经压在手里卖不出去了，你打五折处理他<br/>
<%}}%><a href="<%=("mySlaves.jsp")%>">返回我的奴隶</a><br/><%}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>

