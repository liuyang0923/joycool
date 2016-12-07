<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.cache.util.HomeCacheUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.home.HomeUserBean" %><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("./login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
HomeAction home=new HomeAction(request); 
String inputUserId=request.getParameter("inputUserId");
String nickname=request.getParameter("nickname");
IUserService userService = ServiceFactory.createUserService();
UserBean user=null;
String tip=null;
//根据用户ID设置今日之星
if(inputUserId!=null)
{
	// macq_2006-12-20_增加家园的缓存_start
	HomeUserBean homeUser = HomeCacheUtil.getHomeCache(StringUtil.toInt(inputUserId));
	//HomeUserBean homeUser=home.getHomeService().getHomeUser("user_id="+inputUserId);
	// macq_2006-12-20_增加家园的缓存_end
	if(homeUser==null) {
	tip="此用户家园不存在!";
	}else{
		// macq_2006-12-20_增加家园的缓存_start
		HomeCacheUtil.HOME_STAR = homeUser.getUserId();
		//home.getHomeService().updateHomeUser("mark=0","mark=1");
//		HomeCacheUtil.updateHomeCacheById("mark=1","id="+homeUser.getId(),homeUser.getId());
		//home.getHomeService().updateHomeUser("mark=1","id="+homeUser.getId());
		// macq_2006-12-20_增加家园的缓存_end
		tip="此用户已经被成功设置为今日之星!";
	}
}
//根据用户昵称设置今日之星
if(nickname!=null)
{
	user=userService.getUser("nickname ='"+nickname+"'");
	if(user==null){
		tip="没有查到与此相匹配的用户。";
	}else{
		// macq_2006-12-20_增加家园的缓存_start
	    HomeUserBean homeUser = HomeCacheUtil.getHomeCache(user.getId());
		//HomeUserBean homeUser=home.getHomeService().getHomeUser("user_id="+user.getId());
		// macq_2006-12-20_增加家园的缓存_end
		if(homeUser==null) {
			tip="此用户家园不存在!";
		}else{		
		    // macq_2006-12-20_增加家园的缓存_start
		    HomeCacheUtil.updateHomeCahce("mark=0", "mark=1");
			//home.getHomeService().updateHomeUser("mark=0","mark=1");
			HomeCacheUtil.updateHomeCacheById("mark=1","id="+homeUser.getId(),homeUser.getId());
			//home.getHomeService().updateHomeUser("mark=1","id="+homeUser.getId());
		    // macq_2006-12-20_增加家园的缓存_end
			tip="此用户已经被成功设置为今日之星!";
		}
	}
}
%>
<html >
<head>
</head>
<div align="center">
<font color="red"><%=tip!=null?tip:""%></font><br/>
根据ID指定今日之星<br/>
<form name="form1" method="post" action="todayStar.jsp">
用户ID：<input type="text" name="inputUserId" onKeyPress="if(event.keyCode<48||event.keyCode>57)  event.returnValue=false;" /><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
<div align="center">
昵称指定今日之星<br/>
<form name="form1" method="post" action="todayStar.jsp">
用户昵称：<input type="text" name="nickname" /><input type="submit" name="submit" value="确定"/>
</form>
<br/>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</div>

</html>