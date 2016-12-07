<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.HashMap" %><%@ page import="java.util.Iterator" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
action.seeIntroduction(request);
String result=(String) request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
HashMap hm = (HashMap) request.getAttribute("hm");
%>
<card title="看乐友们的个性介绍">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
Iterator it = hm.values().iterator();
int i = 1 ;
while(it.hasNext()){
	UserBean user=(UserBean)it.next();
	if(user==null){continue;}%>
	<%=i%>.
	<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getSelfIntroduction())%></a><br/>
<%i++;}%>
<a href="/friend/seeIntroduction.jsp">看不上，换一批</a><br/>

<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>