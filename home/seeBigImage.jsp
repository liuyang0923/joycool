<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.cache.util.HomeCacheUtil" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String homeId=request.getParameter("homeId");
if(homeId==null || StringUtil.toInt(homeId)==-1){
//response.sendRedirect(("/home/home.jsp"));
BaseAction.sendRedirect("/home/home.jsp", response);
return;
}
String type=request.getParameter("type");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(type!=null){%>
<card title="我的家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String imageFile = action.getImageUrl(Constants.HOME_IMAGE_BIG_TYPE, loginUser.getId(),StringUtil.toInt(homeId));
%>
<img src="<%=(imageFile)%>" alt="家的图片"/><br/>
<a href="/home/myStore.jsp">买家具</a><br/>
<a href="/home/homeManage.jsp">管理家园</a><br/>
<a href="/home/home.jsp">我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
String userId=request.getParameter("userId");
if(userId==null){
out.clearBuffer();
response.sendRedirect("viewAllHome.jsp");
return;
}
%>
<card title="我的家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String imageFile = action.getImageUrl(Constants.HOME_IMAGE_BIG_TYPE,StringUtil.toInt(userId),StringUtil.toInt(homeId));
%>
<img src="<%=(imageFile)%>" alt="家的图片"/><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<%
UserBean user = UserInfoUtil.getUser(StringUtil.toInt(userId));
%>
<a href="home.jsp?userId=<%=userId%>" >返回<%= StringUtil.toWml(user.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>