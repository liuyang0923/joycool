<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.top.TopAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.top.MoneyTopBean" %><%
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
topAction.gamePointTop(request);
Vector moneyList=(Vector)request.getAttribute("moneyList");
String count=(String)request.getAttribute("count");
if(count.length()>3)
	count = "无";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="财富排行榜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
财富排行榜<br/>
----------<br/>
您目前排名:<%=count%><br/>
<%
if(moneyList.size()>0){
MoneyTopBean money=null;
UserBean user=null;
for(int i=0;i<moneyList.size();i++){
money=(MoneyTopBean)moneyList.get(i);
user=topAction.getUser(money.getUserId());
if(user==null){
continue;
}
%>
<%=i+1+" "%>
<%if(true || money.getUserId()!=loginUser.getId()){%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%>
<a href="/user/ViewUserInfo.do?userId=<%=money.getUserId()%>">
<%
String nickname=StringUtil.toWml(user.getNickName());
if(nickname.equals(""))
nickname="乐客"+user.getId();%>
<%=nickname%>
</a><%}else{%>您自己<%}%>(<%=StringUtil.bigNumberFormat(money.getMoneyTotal())%>乐币)<br/>
<%}}else{%>
暂时不提供财富排名服务!<br/>
<%}%><br/><br/>
<%@include file="bottom.jsp"%><br/>
</p>
</card>
</wml>