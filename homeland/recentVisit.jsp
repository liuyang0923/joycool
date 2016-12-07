<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
	response.setHeader("Cache-Control","no-cache");	
	
	CustomAction customAction = new CustomAction(request);
	
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	ServiceVisit serviceVisit = ServiceVisit.getInstance();
	
	int totalCount = serviceVisit.getCountVisitByToUid(loginUser.getId());
	
	PagingBean paging = new PagingBean(customAction, totalCount, 10, "p");
	
	List visitList = serviceVisit.getVisitByToUid(loginUser.getId(), paging.getStartIndex(), paging.getEndIndex());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="最近访客">
	<p>
	<%=BaseAction.getTop(request, response)%>
	【最近访客】<%=totalCount%>人=<br/>
	<%
		for(int i = 0; i < visitList.size(); i++) {
			BeanVisit visit = (BeanVisit)visitList.get(i);
	%>
			*<a href="<%=("/homeland/home.jsp?uid=" + visit.getFromUid()) %>"><%=StringUtil.toWml(visit.getFromNickName()) %> <%=visit.getVisitTime() %></a><br/>
	<%
		}
	%>
	<%=paging.shuzifenye("recentVisit.jsp", true, "|", response)%>
	<br/>
	<a href="<%=("/homeland/myPage.jsp") %>">返回我的家园</a>	
	<br/><%=BaseAction.getBottomShort(request, response)%>
	</p>
	</card>
</wml>