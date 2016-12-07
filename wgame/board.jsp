<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.action.wgame.BoardAction"%><%
response.setHeader("Cache-Control","no-cache");
BoardAction action = new BoardAction();
action.getBoard(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

Vector userList = (Vector) request.getAttribute("userList");
UserStatusBean userStatus = (UserStatusBean) request.getAttribute("userStatus");
String prefixUrl = (String) request.getAttribute("prefixUrl");
int order = ((Integer)request.getAttribute("order")).intValue();
int totalPageCount = ((Integer)request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer)request.getAttribute("pageIndex")).intValue();

int count, i;
UserStatusBean us = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="财富排行">
<p align="left">
<%=BaseAction.getTop(request, response)%>
财富排行<br/>
您现有<%=userStatus.getGamePoint()%>个乐币，排在第<%=(order + 1)%>位！<br/>
-------------------<br/>
<%
count = userList.size();
for(i = 0; i < count; i ++){
	us = (UserStatusBean) userList.get(i);
%>
<%=(pageIndex * 10 + (i + 1))%>.<%if(loginUser.getId() == us.getUser().getId()){%>您自己<%}else{%><a href="http://wap.joycool.net/user/ViewUserInfo.do?backTo=<%=PageUtil.getBackTo(request) %>&amp;userId=<%=us.getUser().getId()%>"><%= us.getUser().getUserName()%></a><%}%>:<%=us.getGamePoint()%><br/>
<%
}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), false, "|", response);
%>
<%if(!fenye.equals("")){%><%=fenye%><br/><%}%>
<br/>
<a href="index.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>