<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.service.impl.*,net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	ServiceMaster serviceMaster = ServiceMaster.getInstance();
	UserServiceImpl userService = new UserServiceImpl();
	BeanMaster my = serviceMaster.getMasterByUid(loginUser.getId());
	int cur = StringUtil.toId(request.getParameter("pageIndex"));
	int limit = 10;
	int total = serviceMaster.getCountMasterOfMyFriend(loginUser.getId());
	List list = serviceMaster.getMastersOfMyFriend(loginUser.getId(), cur, limit);
	int totalPageCount = PageUtil.getPageCount(limit, total);
	String fenye = PageUtil.shuzifenye(totalPageCount, cur, "myFris.jsp", false, "|", response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买奴隶"><p><%=BaseAction.getTop(request, response)%>
【购买奴隶】<br/>
<%for(int i = 0; i < list.size(); i++) {
	BeanMaster master = (BeanMaster)(list.get(i));
	if(master==null||SecurityUtil.isAdmin(master.getUid())) continue;
	if(my.getMoney() >= master.getPrice() && userService.getUserFriend(master.getUid(), loginUser.getId()) != null){
%><a href="<%=("bFri.jsp?uid="+ master.getUid() + "&amp;n=b")%>">购买</a><%} else if(userService.getUserFriend(master.getUid(), loginUser.getId()) == null){ %>购买<%} else { %>资金不足<%} %>|￥<%=master.getPrice() %>|<a href="<%=("info.jsp?uid="+master.getUid() ) %>"><%=StringUtil.toWml(master.getNickName()) %></a><br/>
<%}%><%if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="myInfo.jsp">返回朋友买卖首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>

