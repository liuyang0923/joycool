<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.top.TopAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser==null) 
{
	String reURL=request.getRequestURL().toString();
	String queryStr=request.getQueryString();
	session.setAttribute("loginReturnURL",queryStr==null?reURL:reURL+"?"+queryStr);

	//response.sendRedirect(("http://wap.joycool.net/user/login.jsp"));
	BaseAction.sendRedirect("/user/login.jsp", response);
	return;
}
TopAction topAction=new TopAction(request);
topAction.spiritTop(request);
Vector spiritList=(Vector)request.getAttribute("spiritList");
String count=(String)request.getAttribute("count");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="反倭榜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
反倭榜<br/>
----------<br/>
<%if(spiritList==null){%>

服务器繁忙,请稍后再试<br/>
<a href="spiritTop.jsp">重试一次</a><br/>

<%}else{%>

您目前排名:<%=count%><br/>
<%
UserBean user=null;
UserStatusBean userStatus=null;
for(int i=0;i<spiritList.size();i++){
userStatus=(UserStatusBean)spiritList.get(i);
user=userStatus.getUser();
if(user==null){
continue;
}
%>
<%=i+1+" "%>
<%if(userStatus.getUserId()!=loginUser.getId()){%>
<%if(userStatus!=null){%><%=userStatus.getHatShow()%><%}%>
<a href="/user/ViewUserInfo.do?userId=<%=userStatus.getUserId()%>">
<%
String nickname=StringUtil.toWml(user.getNickName());
if(nickname.equals(""))
nickname="乐客"+user.getId();%>
<%=nickname%>
</a><%}else{%>您自己<%}%>(<%=userStatus.getSpirit()%>点)<br/>
<%}%><br/><br/>
<%}%>
<%@include file="bottom.jsp"%><br/>
</p>
</card>
</wml>