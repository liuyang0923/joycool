<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.framework.JoycoolSessionListener,net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
if (1==1) {
	BaseAction.sendRedirect("/user/login.jsp", response);
	return;
}
//liuyi 2006-12-20 注册登录修改 start
String mobile = (String) session.getAttribute("userMobile");
if (mobile != null) {
	int id = 0;
	Integer userId = (Integer) OsCacheUtil.get(mobile,
			OsCacheUtil.USER_ID_GROUP,
			OsCacheUtil.USER_ID_FLUSH_PERIOD);
	if (userId == null) {
		String sql = "select id from user_info where mobile='"
				+ mobile + "'";
		id = SqlUtil.getIntResult(sql, Constants.DBShortName);
		OsCacheUtil.put(mobile, new Integer(id),
					OsCacheUtil.USER_ID_GROUP);
	}
	else{
		id = userId.intValue();
	}
	if(id>0){ //老手机号
		//response.sendRedirect(("/user/login.jsp"));
		BaseAction.sendRedirect("/user/login.jsp", response);
		return;
	}
	else{ //新手机号
		//response.sendRedirect(("/jcadmin/autoRegister.jsp"));
		BaseAction.sendRedirect("/jcadmin/autoRegister.jsp", response);
		return;
	}
}	
//liuyi 2006-12-20 注册登录修改 end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head><meta http-equiv="Cache-Control" content="max-age=0" forua="true"/></head>
<card title="乐酷免费注册">
<p align="left">
<%//=BaseAction.getTop(request, response)%>
您尚未登陆，只能阅读资讯、电子书及旁观注册用户的聊天、游戏对战；<br/>
登陆后即可发言聊天、交友、在线ｐｋ赌博、访问他人的乐客家园、点评他人的照片日记……全面玩转乐酷社区！零点前注册，还可获得10万乐币大礼包！<br/>
<a href="<%= ("/user/login.jsp?backTo=http://wap.joycool.net") %>" title="进入">老用户登陆</a><br/>
<a href="<%= ("/jcadmin/autoRegisterNoMobile.jsp?backTo=http://wap.joycool.net") %>" title="进入">快速注册</a>(推荐新用户使用，为避免混乱老用户慎用)<br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>