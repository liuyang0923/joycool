<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<logic:present name="result" scope="request">
    <logic:equal name="result" value="failure">
<%
int forumId = ((Integer)request.getAttribute("forumId")).intValue();
int parent = ((Integer)request.getAttribute("parent")).intValue();
%>
<card title="发表话题">
<p align="left"><%=BaseAction.getTop(request, response)%>
发表话题<br/>
-----------<br/>
    <bean:write name="tip" filter="false"/><br/>
	<anchor title="back"><prev/>返回发表页面</anchor><br/>
<%
if(parent != 0){
%>
<a href="forumMessage.jsp?id=<%=parent%>">返回主贴</a><br/>
<%
}
%>
<a href="forumIndex.jsp?id=<%=forumId%>"><%= (forumId!=15)?"返回论坛首页":"返回客服中心" %></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>

    <logic:equal name="result" value="success">
<%
int forumId = ((Integer)request.getAttribute("forumId")).intValue();
int parent = ((Integer)request.getAttribute("parent")).intValue();
String jumpUrl = "forumIndex.jsp?id=" + forumId;
if(parent != 0){
    jumpUrl = "forumMessage.jsp?id=" + parent;
}
%>
<card title="<%= (forumId!=15)?"发表话题":"提交成功" %>" ontimer="<%=response.encodeURL(jumpUrl)%>">
<timer value="50"/>
<p align="left"><%=BaseAction.getTop(request, response)%>
<%= (forumId!=15)?"发表话题":"提交成功" %><br/>
-----------<br/>
发表成功！5秒后自动跳转！<br/>
<% if(forumId!=15){ %>
建议您发消息给<a href="http://wap.joycool.net/user/sendMessage.jsp?backTo=http://wap.joycool.net/forum/forumIndex.jsp?id=13&amp;toUserId=431">管理员</a>留下联系方式，以便刊登您的作品时与您联系！<br/>
（您如果留过联系方式，请返回论坛）<br/>
<% } %>
<%
if(parent != 0){
%>
<a href="forumMessage.jsp?id=<%=parent%>">返回主贴</a><br/>
<%
}
%>
<a href="forumIndex.jsp?id=<%=forumId%>"><%= (forumId!=15)?"返回论坛首页":"返回客服中心" %></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
</logic:present>

<logic:notPresent name="result" scope="request">
<card title="发表话题">
<p align="left"><%=BaseAction.getTop(request, response)%>
<%
int forumId = Integer.parseInt(request.getParameter("forumId"));
int parent = Integer.parseInt(request.getParameter("parent"));
%>
发表话题<br/>
-----------<br/>
<%
//发表回复，不需要填标题
if(parent == 0){
%>
标题：<br/><input name="title"  maxlength="100" value="v"/><br/>
<%
}
%>
内容：<br/><input name="content"  maxlength="1000" value="v"/><br/>
<br/>
    <anchor title="确定">发表
    <go href="Post.do?forumId=<%=forumId %>&amp;parent=<%=parent%>" method="post">
<%
//发表回复，不需要填标题
if(parent != 0){
%>    
    <postfield name="title" value="re:"/>
<%
}
else {
%>
	<postfield name="title" value="$title"/>
<%
}
%>	
	<postfield name="content" value="$content"/>
    </go>
    </anchor>
<br/>
<br/>
<%
if(parent != 0){
%>
<a href="forumMessage.jsp?id=<%=parent%>">返回主贴</a><br/>
<%
}
%>
<a href="forumIndex.jsp?id=<%=forumId%>"><%= (forumId!=15)?"返回论坛首页":"返回客服中心" %></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:notPresent>
</wml>