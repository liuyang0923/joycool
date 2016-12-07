<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.invites();
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
UserBean loginUser = action.getLoginUser();
TeamBean team = (TeamBean)request.getAttribute("team");%>
ID:<input name="userId" format="*N" maxlength="9"/>
<a href="invite.jsp?id=$userId">邀请此ID</a><br/>
<%
List friends = UserInfoUtil.getUserOnlineFriendsList(loginUser.getId());
PagingBean paging = new PagingBean(action, friends.size(), 10, "p");
for(int i = paging.getStartIndex();i<paging.getEndIndex();i++){
UserBean user = (UserBean)friends.get(i);
%><%=user.getNickNameWml()%>-<a href="invite.jsp?id=<%=user.getId()%>">邀请</a><br/>
<%}%>
<%=paging.shuzifenye("invites.jsp", false, "|", response)%>

<%}%>
<a href="info.jsp">返回</a><br/>
</p>
</card>
</wml>