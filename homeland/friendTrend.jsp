<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*"%><%
	response.setHeader("Cache-Control","no-cache");
	CustomAction customAction = new CustomAction(request);
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	int uid = user.getId();
	ServiceTrend serviceTrend = ServiceTrend.getInstance();
	int totalCount = serviceTrend.getCountFriendTrendByUid(uid);	
	PagingBean paging = new PagingBean(customAction, totalCount, 10, "p");
	List trendList = serviceTrend.getFriendTrendIdByUid(uid, paging.getStartIndex(), 10);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="好友动态">
	<p>
	<%=BaseAction.getTop(request, response)%>
	===好友动态===<br/>
	<%
		for(int i = 0;i < trendList.size(); i ++) {
			BeanTrend trend = ServiceTrend.getInstance().getTrendById(((Integer)trendList.get(i)));
	%>
			*<%=trend.getContent(uid, response) %><br/>
			
	<%} %>
	<%=paging.shuzifenye("friendTrend.jsp", false, "|", response)%>
	<a href="home.jsp">返回我的家园</a><br/>
	<%=BaseAction.getBottomShort(request, response)%>
	</p>	
	</card>
</wml>