<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.UserFriendBean" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.List" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.careerList(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
Vector userFriendList = (Vector)request.getAttribute("userFriendList");
Vector jyFriendList = (Vector)request.getAttribute("jyFriendList");
int moreuserFriendList=StringUtil.toInt((String)request.getAttribute("moreuserFriendList"));
int moreuserJyList=StringUtil.toInt((String)request.getAttribute("moreuserJyList"));
FriendMarriageBean marriage=(FriendMarriageBean )request.getAttribute("marriage");
IUserService userService = ServiceFactory.createUserService();;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷生涯记录">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=loginUser.getNickName()%>的<br/>
乐酷生涯记录<br/>
使用过的昵称<br/>
<a href="/home/careerList.jsp">之前昵称</a><br/>
好友记录<br/>
<%for(int i=0;i<userFriendList.size()&&i<2;i++)
{
 UserBean friend =(UserBean)userFriendList.get(i);%>
<%=StringUtil.toWml(friend.getNickName())%>
<%UserFriendBean userfriend=userService.getUserFriend(loginUser.getId(),friend.getId());%>
<%=userfriend.getCreateDatetime().substring(0,11)%><br/>
<%}
if(moreuserFriendList>2){%>
<a href="/home/morecareerList.jsp?friend=1">更多好友》</a><br/>
<%}%>
婚姻记录<br/>
<%if(marriage!=null)
{UserBean friend=null;
if(marriage.getToId()==loginUser.getId()){
 friend=UserInfoUtil.getUser(marriage.getFromId());
%>
<%=StringUtil.toWml(friend.getNickName())%>
<%}else{
 friend=UserInfoUtil.getUser(marriage.getToId());
%>
<%=StringUtil.toWml(friend.getNickName())%>
<%}%>
<%=marriage.getMarriageDatetime().substring(0,11)%><br/>
<%}%>
结拜记录<br/>
<%for(int i=0;i<jyFriendList.size()&&i<2;i++)
{
 UserBean friend =(UserBean)jyFriendList.get(i);%>
<%=StringUtil.toWml(friend.getNickName())%>
<%UserFriendBean userfriend=userService.getUserFriend(loginUser.getId(),friend.getId());%>
<%=userfriend.getCreateDatetime().substring(0,11)%><br/>
<%}
if(moreuserJyList>2){%>
<a href="/home/morecareerList.jsp?jy=1">更多记录》</a><br/>
<%}%>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>