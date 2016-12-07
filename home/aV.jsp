<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*"%><%
	response.setHeader("Cache-Control","no-cache");
	CustomAction customAction = new CustomAction(request);
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	int uid = user.getId();
	ServiceVisit serviceVisit = ServiceVisit.getInstance();
	int totalCount = serviceVisit.getCountVisitByToUid(uid);

	PagingBean paging = new PagingBean(customAction, totalCount, 10, "p");
	
	List visitList = serviceVisit.getVisitByToUid(uid, paging.getStartIndex(), 10);
	//String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, "aV.jsp", false, "|", response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="所有访客">
	<p>
	<%=BaseAction.getTop(request, response)%>
	所有访客<br/>
	<%
		for(int i = 0;i < visitList.size(); i ++) {
			BeanVisit visit = (BeanVisit)visitList.get(i);
	%>
			<a href="<%=("/home/home2.jsp?uid=" + visit.getFromUid()) %>"><%=StringUtil.toWml(visit.getFromNickName()) %></a> <%=visit.getVisitTime() %><br/>			
	<%} %>
	<%=paging.shuzifenye("aV.jsp", false, "|", response)%>
	<a href="<%=("home.jsp") %>">返回</a><br/>
	<%=BaseAction.getBottomShort(request, response)%>
	</p>	
	</card>
</wml>