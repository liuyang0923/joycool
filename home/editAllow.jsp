<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.home.HomeUserBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.editAllow();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if(!action.isResult("edit")){
%>
<card title="设置保密">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="home.jsp">返回我的家园</a><br/>
<a href="/user/userInfo.jsp">修改基本资料</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else{
HomeUserBean homeUser=(HomeUserBean)request.getAttribute("homeUser");
%>
<card title="设置保密">
<p align="left">
<%=BaseAction.getTop(request, response)%>
我的家园设置为:<br/>
<select name="allow" value="<%=homeUser.getAllow()%>">
    <option value="0">所有人都可以访问</option>
    <option value="1">只有我的好友可以访问</option>
    <option value="2">所有人都不可以访问</option>
</select><br/>
访问密码:(留空为没有密码)(暂时无效)<br/>
<input name="password"  maxlength="10" value=""/><br/>
<anchor title="确定">确认设置
    <go href="editAllow.jsp" method="post">
	<postfield name="allow" value="$allow"/>
	<postfield name="password" value="$password"/>
    </go>
</anchor><br/>
<a href="home.jsp">返回我的家园</a><br/>
<a href="/user/userInfo.jsp">修改基本资料</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>