<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%! static int NUMBER_OF_PAGE = 10;%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.chat();
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="队伍聊天">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<% GroupBean group = action.getUserGroup();
PagingBean paging = new PagingBean(action, group.getChatCount(),NUMBER_OF_PAGE,"p");
if(group != null){%>
<input name="gchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="chat.jsp" method="post">
    <postfield name="content" value="$gchat"/>
</go></anchor>|
<a href="<%=url%>">刷新</a>|<a href="../map.jsp">返回场景</a><br/>
<%=group.getChatString(paging.getStartIndex()-1, NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("chat.jsp", false, "|", response)%>
<%}else{%>
你还没有加入任何组<br/>
<%}%>

<a href="info.jsp">组队成员</a>|<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>