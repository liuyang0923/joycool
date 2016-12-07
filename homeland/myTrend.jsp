<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.util.*, net.joycool.wap.bean.home.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
	response.setHeader("Cache-Control","no-cache");
	CustomAction customAction = new CustomAction(request);	
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);	
	int uid = loginUser.getId();
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(uid);
	List list = homeUser.getTrend();	
	PagingBean paging = new PagingBean(customAction, list.size(), 10, "p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="我的动态">
	<p>
	<%=BaseAction.getTop(request, response)%>
	<a href="<%=("/homeland/myPage.jsp") %>">家园</a><br/>
	【我的动态】<br/>
	<%
		for(int i = paging.getStartIndex();i < paging.getEndIndex(); i ++) {
			BeanTrend trend = (BeanTrend)list.get(i);
	%>
			*<%=trend.getContent((loginUser == null?0:loginUser.getId()), response)%><br/>
	<%} %>
	<%=paging.shuzifenye("myTrend.jsp", false, "|", response)%>
	<a href="<%=("/homeland/myPage.jsp") %>">回家</a>
	<br/>
	<%=BaseAction.getBottomShort(request, response)%>
	</p>
	</card>
</wml>