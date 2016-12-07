<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	String action = request.getParameter("action");
	boolean flag = false;
	if(action != null && action.equals("ransom")){
		flag = true;
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="赎身"><p><%=BaseAction.getTop(request, response)%>
<%if(!flag) {
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	ServiceMaster serviceMaster = ServiceMaster.getInstance();
	BeanMaster master = serviceMaster.getMasterByUid(loginUser.getId());%>
你的身价是<%=master.getPrice() %>，你有<%=master.getMoney() %>的现金，你是否要赎身?<br/>	
<a href="<%=("ransom.jsp?action=ransom")%>">确定</a> <a href="<%=("myInfo.jsp")%>">返回朋友买卖</a><br/>
<%} else {
	boolean success = false;
	ServiceSlave serviceSlave = ServiceSlave.getInstance();
	ServiceMaster serviceMaster = ServiceMaster.getInstance();
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	BeanMaster master = serviceMaster.getMasterByUid(loginUser.getId());
	if(master.getMoney() >= master.getPrice()) {
		success = true;
	}
	
	BeanSlave slave = serviceSlave.getSlaveBySlaveUid(loginUser.getId());
	
	if(slave != null) {
	if(success) {
		serviceSlave.deletePunish(slave.getSlaveUid());
		//删除奴隶关系
		serviceSlave.deleteSlave(slave.getMasterUid(), slave.getSlaveUid());
		//奴隶主加钱，并且数量减1
		serviceMaster.increaseMoneyByUid(slave.getMasterUid(), master.getPrice(), true);
		
		//身价上涨10%
		int priceOffSet = (int)(master.getPrice() * 0.1f);
		//奴隶减钱,
		serviceMaster.ransom(slave.getSlaveUid(),priceOffSet, master.getPrice());
	}
	if(success) {%>你已经重获自由!身价上涨10%,并且1天之内任何人都不能买你了^_^<br/>
<%} else {%>金钱不够<br/><%}} else {%>您已经是自由身<br/><%}%><a href="<%=("myInfo.jsp")%>">返回朋友买卖</a><br/>
<%}%><%=BaseAction.getBottomShort(request, response)%></p></card></wml>