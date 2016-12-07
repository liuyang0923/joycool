<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IHomeService" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%!
static int ROW_COUNT = 10;%><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);

	String orderBy = request.getParameter("orderBy");
	if (orderBy == null) {
		orderBy = "id";
	}
	String sql = null;
	List diaryList;
	int p = action.getParameterInt("p");
	PagingBean paging = new PagingBean(action, (p+5)*ROW_COUNT, ROW_COUNT, "p");
	if ("hits".equals(orderBy)) {// 按人气
		sql = "cat_id=0 and del=0 order by hits desc";
		diaryList = action.getHomeService().getHomeDiaryList(
			sql + " limit " + paging.getStartIndex() + ", " + paging.getCountPerPage());
	} else if ("mark".equals(orderBy)) {// 按推荐
		diaryList = action.getHomeService().getHomeDiaryTopList2("order by a.id desc limit " + paging.getStartIndex() + ", " + paging.getCountPerPage());
	} else {// 按时间
		sql = "cat_id=0 and del=0 order by id desc";
		diaryList = action.getHomeService().getHomeDiaryList(
			sql + " limit " + paging.getStartIndex() + ", " + paging.getCountPerPage());
	}

UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="网友日记">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%--<img src="../img/diary.gif" alt="乐客日记"/><br/>--%>
<%
if ("id".equals(orderBy)) {%>
按时间|<a href="/home/newDiaryTop.jsp?orderBy=hits">按人气</a>|<a href="/home/newDiaryTop.jsp?orderBy=mark">精华区</a><br/>
<%} else if ("hits".equals(orderBy)) {%>
<a href="/home/newDiaryTop.jsp?orderBy=id">按时间</a>|按人气|<a href="/home/newDiaryTop.jsp?orderBy=mark">精华区</a><br/>
<%}else {%>
<a href="/home/newDiaryTop.jsp?orderBy=id">按时间</a>|<a href="/home/newDiaryTop.jsp?orderBy=hits">按人气</a>|精华区<br/>
<%}
HomeDiaryBean homeDiary=null;
for(int i=0;i<diaryList.size();i++){
homeDiary=(HomeDiaryBean)diaryList.get(i);
%>
<%=i+1%>.<a href="/home/homeDiary.jsp?userId=<%=homeDiary.getUserId()%>&amp;diaryId=<%=homeDiary.getId()%>&amp;orderBy=<%=orderBy%>"><%=StringUtil.toWml(homeDiary.getTitel())%></a>(阅<%=homeDiary.getHits()%> | 评<%=homeDiary.getReviewCount()%>)<br/>
<%}
%><%=paging.shuzifenye("newDiaryTop.jsp?orderBy="+orderBy,true,"|",response)%>
<a href="/home/viewAllHome.jsp">返回家园之星</a><br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>    
<%if(loginUser!=null && loginUser.getHome()==1){%>
<a href="/home/home.jsp">返回我的家园</a><br/>
<%}else{%>
<a href="/home/inputRegisterInfo.jsp">我也要建立家园</a><br/> 
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>