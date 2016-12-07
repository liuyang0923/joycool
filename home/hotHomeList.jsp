<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
//dao,action
IHomeService homeService =ServiceFactory.createHomeService();
IUserService userService =ServiceFactory.createUserService();
HomeAction home=new HomeAction(request);      
home.hotHomeList(request);
//人气排行
int perPage=StringUtil.toInt((String)request.getAttribute("NUM_PER_PAGE"));
int totalCount=StringUtil.toInt((String)request.getAttribute("totalCount"));
int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
Vector hitsHome=(Vector)request.getAttribute("hitsHome");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="人气家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
int count = hitsHome.size();
for(int i = 0; i < count; i ++){
	HomeUserBean homeUser=(HomeUserBean)hitsHome.get(i);
	UserBean user=UserInfoUtil.getUser(homeUser.getUserId());
%>
<%=perPage*pageIndex + i+1%>.<%if(user==null){%><%=homeUser.getUserId()%><%}else{%>
<a href="home.jsp?userId=<%=homeUser.getUserId()%>" ><%=(loginUser!=null&&loginUser.getId()==user.getId())?"我的家园":StringUtil.toWml(user.getNickName())%></a><%}%>(<%=homeUser.getHits()%>访)<br/>
<%}%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "hotHomeList.jsp", false, " ", response)%><%if(totalPage>1){%><br/><%}%>
用户ID：<input name="userId" format="*N"  maxlength="20" value=""/> <anchor>搜索家园
<go href="searchHome.jsp" method="post">
    <postfield name="userId" value="$(userId)"/>
</go></anchor><br/>
<a href="viewNewHome.jsp">新成立的家园</a><br/>       

<%if(loginUser!=null && loginUser.getHome()==1){%>
<a href="home.jsp">返回我的家园</a><br/>
<%}else{%>
<a href="inputRegisterInfo.jsp">我也要建立家园</a><br/> 
<%}%>
<a href="viewAllHome.jsp">*家园之星*</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>