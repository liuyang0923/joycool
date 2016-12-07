<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.List, net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.cache.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.top.TopAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.charitarian.CharitarianBean" %><%
response.setHeader("Cache-Control","no-cache");
//zhul2006-09-12 限制未登录用户进入start
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
//zhul2006-09-12 限制未登录用户进入end
TopAction topAction=new TopAction(request);
topAction.charitatianTop(request);
Vector charitarianList=(Vector)request.getAttribute("charitarianList");
String count=(String)request.getAttribute("count");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="慈善排行榜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
慈善排行榜<br/>
----------<br/>
<%if(charitarianList==null){%>

服务器繁忙,请稍后再试<br/>
<a href="charitarianTop.jsp">重试一次</a><br/>

<%}else{%>

您目前排名:<%=count%><br/>
<%
CharitarianBean charitarian = null;
UserBean user=null;
for(int i=0;i<charitarianList.size();i++){
	Integer userId =(Integer)charitarianList.get(i);
    user=UserInfoUtil.getUser(userId.intValue());
if(user==null){
continue;
}
%>
<%=i+1+" "%>
<%if(user.getId()!=loginUser.getId()){%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>">
<%
String nickname=StringUtil.toWml(user.getNickName());
if(nickname.equals(""))
nickname="乐客"+user.getId();%>
<%=nickname%>
</a><%}else{%>您自己<%}%>(<%=user.getUs2().getCharitarian()%>点)<br/>
<%}%><br/><br/>
<%}%>
<%@include file="bottom.jsp"%><br/>
</p>
</card>
</wml>