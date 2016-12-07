<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.SqlUtil"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.cache.util.HomeCacheUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.home.HomePhotoBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
HomeAction home=new HomeAction(request); 
String inputUserId=request.getParameter("inputUserId");
String delId=request.getParameter("delId");
String nickname=request.getParameter("nickname");
IUserService userService = ServiceFactory.createUserService();
IHomeService homeService = ServiceFactory.createHomeService();
Vector homeUser =null;
UserBean user=null;
String tip=null;
HomePhotoBean homeUsers=null;
int userId=0;
//根据用户ID删除推荐家园
if(delId!=null)
{
	     homeService.deleteHomePhotoReview("photo_id="+StringUtil.toInt(delId));
		HomeCacheUtil.deleteHomePhoneCache("id="+StringUtil.toInt(delId),userId);
		tip="此用户家园照片已经被删除!";
	
}
if(inputUserId!=null)
{
	userId = StringUtil.toInt(inputUserId);
	if(request.getParameter("next")!=null)
		userId = SqlUtil.getIntResult("select user_id from jc_home_user where user_id>"+userId+" and photo_count>0");
	 homeUser = HomeCacheUtil.getHomePhoneListCache(userId);
	if(homeUser==null) {
	tip="此用户照片不存在!";
	}
}
//根据用户昵称设置推荐家园
if(nickname!=null)
{
	user=userService.getUser("nickname ='"+nickname+"'");
	if(user==null){
		tip="没有查到与此相匹配的用户。";
	}else{
	     homeUser = HomeCacheUtil.getHomePhoneListCache(user.getId());
		if(homeUser==null) {
			tip="此用户家园照片不存在!";
		}
	}
}
%>
<html>
<div align="center">
<H1 align="center">家园照片管理后台</H1>
<a href="viewHomePhone.jsp?next=1&amp;inputUserId=<%=userId%>">下一个</a><br/>
<table border="1" align="center" >
<tr>
	<td>
		id
	</td>
	<td>
		用户ID
	</td>
	<td>
		
	</td>
	<td>
		照片名称
	</td>
	<td>
		操作
	</td>
</tr>
<%
if(homeUser!=null){
for(int i = 0; i < homeUser.size(); i ++){
%><tr><%
	homeUsers=(HomePhotoBean)homeUser.get(i);
	UserBean user1=UserInfoUtil.getUser(homeUsers.getUserId());
	if(user1==null)continue;
%>
<td><%=i+1%></td>
<td><%=user1.getId()%></td>
<td><img src="/rep/home/myalbum/<%=homeUsers.getAttach()%>" alt=""/></td>
<td><%=homeUsers.getTitle()%></td>
<td><a href="viewHomePhone.jsp?delId=<%=homeUsers.getId()%>&amp;inputUserId=<%=user1.getId()%>">删除</a></td>
</tr>
<%}%>
<%}%>
</table>
<font color="red"><%=tip!=null?tip:""%></font><br/>
根据ID指定家园照片<br/>
<form name="form1" method="post" action="viewHomePhone.jsp">
用户ID：<input type="text" name="inputUserId"/><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
<div align="center">
根据昵称指定家园照片<br/>
<form name="form1" method="post" action="viewHomePhone.jsp">
用户昵称：<input type="text" name="nickname" /><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
</html>