<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.top.TopAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.bean.pk.PKUserBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action=new PKAction(request);
action.pkTop();
Vector userList=(Vector)request.getAttribute("userList");
String count=(String)request.getAttribute("count");
PKUserBean pkUser=(PKUserBean)request.getAttribute("pkUser");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="侠客秘境杀人榜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
侠客秘境杀人榜<br/>
----------<br/>
<%if(pkUser!=null){%>
您昨日战绩:<%=pkUser.getOldKCount()%>条人命，排名<%=count%>位<br/>
<%}%>
<%
UserBean user=null;
PKUserBean pkUserTop=null;
for(int i=0;i<userList.size();i++){
pkUserTop=(PKUserBean)userList.get(i);
user=UserInfoUtil.getUser(pkUserTop.getUserId());
if(user==null){
continue;
}
%>
<%=i+1+" "%>
<%if(pkUserTop.getUserId()!=loginUser.getId()){%>
<a href="/user/ViewUserInfo.do?userId=<%=pkUserTop.getUserId()%>">
<%=StringUtil.toWml(user.getNickName())%></a>
<%}else{%>您自己<%}%>
(
<%if(pkUserTop.getOldKCount()>0){%>
<%=pkUserTop.getOldKCount()%>条人命
<%
}else{%>
没有杀过人
<%}%>
)<br/>
<%}%><br/>

<a href="/pk/index.jsp">马上开始游戏</a><br/>
<a href="/pk/help.jsp">返回侠客秘境</a><br/>
<a href="/top/index.jsp">返回排行榜首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>