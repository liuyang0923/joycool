<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.cache.util.HomeCacheUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.home.HomeUserBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
HomeAction home=new HomeAction(request); 
String inputUserId=request.getParameter("inputUserId");
String delUserId=request.getParameter("delUserId");
String nickname=request.getParameter("nickname");
IUserService userService = ServiceFactory.createUserService();
UserBean user=null;
String tip=null;
//根据用户ID设置推荐家园
if(inputUserId!=null)
{
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(StringUtil.toInt(inputUserId));
	if(homeUser==null) {
	tip="此用户家园不存在!";
	}else{
		HomeCacheUtil.updateHomeCacheById("mark=2","id="+homeUser.getId(),homeUser.getId());
		tip="此用户已经被成功设置为推荐家园!";
	}
}
//根据用户ID删除推荐家园
if(delUserId!=null)
{
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(StringUtil.toInt(delUserId));
	if(homeUser==null) {
	tip="此用户家园不存在!";
	}else{
		HomeCacheUtil.updateHomeCacheById("mark=0","id="+homeUser.getId(),homeUser.getId());
		tip="此推荐用户家园已经被还原!";
	}
}
//根据用户昵称设置推荐家园
if(nickname!=null)
{
	user=userService.getUser("nickname ='"+nickname+"'");
	if(user==null){
		tip="没有查到与此相匹配的用户。";
	}else{
	    HomeUserBean homeUser = HomeCacheUtil.getHomeCache(user.getId());
		if(homeUser==null) {
			tip="此用户家园不存在!";
		}else{		
			HomeCacheUtil.updateHomeCacheById("mark=2","id="+homeUser.getId(),homeUser.getId());
			tip="此用户已经被成功设置为推荐家园!";
		}
	}
}

Vector commendStar = home.getHomeService().getHomeUserList("mark=2");
%>
<html >
<head>
</head>
<div align="center">
<H1 align="center">推荐家园后台</H1><hr>
<table border="1" align="center" >
<tr>
	<td>
		id
	</td>
	<td>
		用户ID
	</td>
	<td>
		用户名
	</td>
	<td>
		人气
	</td>
	<td>
		操作
	</td>
</tr>

<%
for(int i = 0; i < commendStar.size(); i ++){
%><tr><%
	HomeUserBean homeUser=(HomeUserBean)commendStar.get(i);
	UserBean user1=UserInfoUtil.getUser(homeUser.getUserId());
	if(user1==null)continue;
%>
<td><%=i+1%></td>
<td><%=user1.getId()%>
<td><%=StringUtil.toWml(user1.getNickName())%>
<td><%=homeUser.getTotalHits()%><br/>
<td><a href="commendStar.jsp?delUserId=<%=user1.getId()%>">删除</a>
</tr>
<%}%>

</table>
<font color="red"><%=tip!=null?tip:""%></font><br/>
根据ID指定推荐家园<br/>
<form name="form1" method="post" action="commendStar.jsp">
用户ID：<input type="text" name="inputUserId" onKeyPress="if(event.keyCode<48||event.keyCode>57)  event.returnValue=false;" /><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
<div align="center">
根据昵称指定推荐家园<br/>
<form name="form1" method="post" action="commendStar.jsp">
用户昵称：<input type="text" name="nickname" /><input type="submit" name="submit" value="确定"/>
</form>
<br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br/>
</div>

</html>