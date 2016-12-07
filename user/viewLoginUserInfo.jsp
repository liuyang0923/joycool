<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="用户信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您的登陆ID:<%= loginUser.getId() %><br/>
昵称:<%=StringUtil.toWml(loginUser.getNickName())%><br/>
自我介绍:<%=StringUtil.toWml(loginUser.getSelfIntroduction())%><br/>
要<a href="/user/userInfo.jsp">修改</a>吗？<br/>
<a href="/">返回首页</a><br/>
<a href="<%= ("/user/logout.jsp") %>" title="进入">重新登陆</a><br/>
<a href="<%= ("/enter/index.jsp") %>" title="进入">存书签，以后直接登陆乐酷</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>