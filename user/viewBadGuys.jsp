<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%
response.setHeader("Cache-Control","no-cache");

CustomAction action = new CustomAction(request);
UserBean loginUser = action.getLoginUser();
List userList = SqlUtil.getIntList("select badguy_id from user_blacklist where user_id = " + loginUser.getId());

//String backTo = (String) request.getAttribute("backTo");
int i, count;
UserBean user;
count = userList.size();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="黑名单">
<p align="left">
<%=BaseAction.getTop(request, response)%>
我的黑名单(<%=count%>)<br/>
----------<br/>
<%
PagingBean paging = new PagingBean(action, count, 15, "p");
for(i = paging.getStartIndex(); i < paging.getEndIndex(); i ++){
	Integer iid = (Integer)userList.get(i);
	user = UserInfoUtil.getUser(iid.intValue());
	if(user==null)continue;
%>
<%=(i + 1)%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><a href="ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>
<br/>
<%}%>
<%=paging.shuzifenye("viewBadGuys.jsp", false, "|", response)%>
<br/>
<%--<a href="friendIndex.jsp" title="进入">好友管理</a><br/>--%>
<a href="/user/ViewFriends.do">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>