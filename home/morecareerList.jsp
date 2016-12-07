<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.UserFriendBean" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.morecareerList(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
Vector userFriendList = (Vector)request.getAttribute("userFriendList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
IUserService userService = ServiceFactory.createUserService();;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷生涯记录">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=loginUser.getNickName()%>的<br/>
乐酷生涯记录<br/>
<%for(int i=0;i<userFriendList.size();i++)
{
 UserBean friend =(UserBean)userFriendList.get(i);%>
<%=StringUtil.toWml(friend.getNickName())%>
<%UserFriendBean userfriend=userService.getUserFriend(loginUser.getId(),friend.getId());%>
<%=userfriend.getCreateDatetime().substring(0,11)%><br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
 }
%>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>