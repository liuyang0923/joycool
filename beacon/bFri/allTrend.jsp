<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.bean.PagingBean" %><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
	response.setHeader("Cache-Control","no-cache");
	//买卖好友所有动态显示
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	ServiceTrend serviceTrend = ServiceTrend.getInstance();
	
	CustomAction customAction = new CustomAction(request);
	
	
	int totalCount = serviceTrend.getCountGameTrendByType(user.getId(), BeanTrend.TYPE_BUY_FRIEND);
	
	PagingBean paging = new PagingBean(customAction, totalCount, 10, "p");
	
	List trendList = serviceTrend.getGameTrendByType(user.getId(), BeanTrend.TYPE_BUY_FRIEND, paging.getStartIndex(), paging.getCountPerPage());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="我的游戏动态"><p><%=BaseAction.getTop(request, response)%>
【我的游戏动态】<br/>
<%for(int i = 0;i < trendList.size(); i ++) {
		BeanTrend trend = (BeanTrend)trendList.get(i);
%>* <%=trend.getBuyFriendContent(user.getId(), response) %><br/><%} %>
<%=paging.shuzifenye("allTrend.jsp", false, "|", response)%>
<a href="myInfo.jsp">返回朋友买卖</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>