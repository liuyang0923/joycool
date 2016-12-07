<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
UserAction action = new UserAction(request);
action.searchUser();
int pageIndex = action.getParameterInt("p");
int start = pageIndex*10+1;
String prefixUrl = (String) request.getAttribute("prefixUrl");
List userList = (List) request.getAttribute("userList");
int count = userList.size();
if(count > 10) count = 10;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="搜索用户">
<p align="left">
<%=BaseAction.getTop(request, response)%>
=搜索用户(第<%=pageIndex+1%>页)=<br/>
<%
for(int i = 0; i < count; i ++){
	UserBean user = (UserBean) userList.get(i);
	if(user.getId()>100){
%><%=i+start%>.<a href="ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>(<%=user.getId()%>)<br/>
<%}}%>
<%if(userList.size()>10&&pageIndex<9){%><a href="<%=(prefixUrl+"&amp;p=" + (pageIndex + 1))%>">下一页</a><%}else{%>下一页<%}%>
|<%if(pageIndex > 0){%><a href="<%=(prefixUrl+"&amp;p=" + (pageIndex - 1))%>">上一页</a><%}else{%>上一页<%}%>
<br/>
<a href="searchUser.jsp">返回上一级</a><br/>
<a href="../friend/search/searchCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>