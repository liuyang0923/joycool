<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*"%><%
	response.setHeader("Cache-Control","no-cache");
	CustomAction customAction = new CustomAction(request);
	int uid = Integer.parseInt(request.getParameter("uid"));
	UserBean user = UserInfoUtil.getUser(uid);
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(uid);
	List list = homeUser.getTrend();
	PagingBean paging = new PagingBean(customAction, list.size(), 10, "p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="<%=user.getNickName()%>的动态">
	<p>
	<%=BaseAction.getTop(request, response)%>
	<a href="<%=("home2.jsp?uid="+uid) %>"><%=user.getNickName()%>的家园</a>|<a href="<%=("home.jsp") %>">回家</a><br/>
	【<%=user.getNickName()%>的动态】<br/>
	<%
		for(int i = paging.getStartIndex();i < paging.getEndIndex(); i ++) {
			BeanTrend trend = ServiceTrend.getInstance().getTrendById(((Integer)homeUser.getTrend().get(i)));
	%>
			*<%=trend.getContent((loginUser == null?0:loginUser.getId()), response)%><br/>
	<%} %>
	<%=paging.shuzifenye("userTrend.jsp?uid="+uid, true, "|", response)%>
	<a href="<%=("home.jsp") %>">回家</a>
	<br/>
	<%=BaseAction.getBottomShort(request, response)%>
	</p>
	</card>
</wml>