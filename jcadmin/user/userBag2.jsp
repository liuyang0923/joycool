<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.service.*" %><%@ page import="net.joycool.wap.service.factory.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.service.impl.DummyServiceImpl" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.service.impl.UserbagItemServiceImpl" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.item.UserItemLogBean" %><%
response.setHeader("Cache-Control","no-cache");
int id = StringUtil.toInt(request.getParameter("id"));
UserBagBean userBag=UserBagCacheUtil.getUserBagCache(id);
UserBean user = null;
DummyProductBean item=null;
if(userBag!=null){
if(userBag.getUserId()!=0)
	user = UserInfoUtil.getUser(userBag.getUserId());
if(userBag.getProductId()!=0)
	item=UserBagCacheUtil.getItem(userBag.getProductId());
}
if(group.isFlag(0)&&userBag!=null && request.getParameter("day") != null){
	int day = StringUtil.toInt(request.getParameter("day"));
	userBag.setEndTime(userBag.getEndTime() + day * 86400000l);
	SqlUtil.executeUpdate("update user_bag set end_datetime=date_add(end_datetime, interval "+day+" day) where id=" + id);
	response.sendRedirect("userBag2.jsp?id=" + id);
	return;
}
%>
<html>
<div align="center">
<H1 align="center">用户行囊管理后台</H1>
<p align="center" >
</p>
<table border="1" align="center" >
<tr>
	<td>id</td>
	<td>物品</td>
	<td>数量</td>
	<td>所属</td>
	<td>制造者</td>
	<td>期限</td>
</tr>
<tr><%if(userBag!=null){%>
<td><%=userBag.getId()%></td>
<td><%if(item!=null){%><%=item.getName()%><%=userBag.getTimeString(item)%><%}%></td>
<td><%=userBag.getTime()%></td>
<td><%if(user!=null){%><a href="queryUserInfo.jsp?id=<%=userBag.getUserId()%>"><%=user.getNickNameWml()%></a><%}%></td>
<td><a href="queryUserInfo.jsp?id=<%=userBag.getCreatorId()%>"><%=userBag.getCreatorId()%></a></td>
<td><%=DateUtil.formatTimeInterval(userBag.getTimeLeft())%></td>
</tr><%}%>
</table>
<%if(group.isFlag(0)){%>
<form action="userBag2.jsp?id=<%=id%>" method="post">
<input type=text name="day" value="1">
<input type=submit value="增加天数">
</form><br/><%}%>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
</html>