<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.user.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.bean.friend.FriendRingBean" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser==null){
	//response.sendRedirect("/user/login.jsp");
	BaseAction.sendRedirect("/user/login.jsp", response);
	return;
}
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String flag = (String)session.getAttribute("redbag");
if(flag==null){
	UserInfoUtil.updateUserStatus("game_point=game_point+100000", "user_id=" + loginUser.getId(), loginUser.getId(), 0, "10万红包");
	session.setAttribute("redbag", "true");
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="10万红包相送！">
<p align="left">
庆祝成功登录乐酷！感谢您对乐酷的支持，10万红包送你。请牢记自己的id<%= loginUser.getId() %>与密码<%=Encoder.decrypt(loginUser.getPassword())%>！惊喜不断哦～（<a href="/jcadmin/tempLogin.jsp">找回我的老ID</a>）<br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>