<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.spec.AntiAction" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%
CustomAction action = new CustomAction(request, response);
int tongId = action.getParameterInt("tongId");
int fixId = action.getParameterInt("fixId");
TongBean tong = TongCacheUtil.getTong(tongId);
if(fixId > 0) {
	UserInfoUtil.updateUserTong(fixId, tongId);
	response.sendRedirect("tongUser.jsp?tongId=" + tongId);
}
List tongUserList = TongCacheUtil.getTongUserListById(tongId);
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3>帮会成员检查</h3>
<%if(tong!=null){%><%=tong.getTitle()%>(共<%=tongUserList.size()%>人)<br/><%}%>
<table cellpadding=2><tr><td>用户名</td><td>帮会</td>
<td>操作</td></tr>
<%
	for(int i=0;i<tongUserList.size();i++){
	int userId = ((Integer)tongUserList.get(i)).intValue();
	UserBean user=UserInfoUtil.getUser(userId);
	if(user==null) continue;
	UserStatusBean us = UserInfoUtil.getUserStatus(userId);
	if(us.getTong()==tongId)continue;
%>
<tr><td><a href="../user/queryUserInfo.jsp?id=<%=userId%>"><%=user.getNickNameWml()%></a></td>
<td><%=us.getTong()%></td>
<td><%if(us.getTong()!=tongId){%><a href="tongUser.jsp?tongId=<%=tongId%>&fixId=<%=userId%>">修复</a><%}%></td>
</tr>
<%}%>
</table><br/>
<a href="index.jsp">返回上一级</a>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
</body>
</html>