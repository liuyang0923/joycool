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
<a href="ForumMessage.do?id=<%=parent%>">返回主贴</a><br/>
<%
}
%>
<a href="ForumIndex.do?id=<%=forumId%>">返回版面</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>

    <logic:equal name="result" value="success">
<%
int forumId = ((Integer)request.getAttribute("forumId")).intValue();
int parent = ((Integer)request.getAttribute("parent")).intValue();
%>
<card title="发表话题">
<p align="left"><%=BaseAction.getTop(request, response)%>
发表话题<br/>
-----------<br/>
发表成功！<br/>
建议您发消息给<a href="http://wap.joycool.net/user/sendMessage.jsp?backTo=http://wap.joycool.net/forum/ForumIndex.do?id=13&amp;toUserId=431">管理员</a>留下联系方式，以便刊登您的作品时与您联系！<br/>
（您如果留过联系方式，请返回论坛）<br/>
<%
if(parent != 0){
%>
<a href="ForumMessage.do?id=<%=parent%>">返回主贴</a><br/>
<%
}
%>
<a href="ForumIndex.do?id=<%=forumId%>">返回版面</a><br/>
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
标题：<br/><input name="title"  maxlength="100" value="<%if(parent != 0){%>re:<%}else{%>v<%}%>"/><br/>
内容：<br/><input name="content"  maxlength="1000" value="v"/><br/>
<br/>
    <anchor title="确定">发表
    <go href="Post.do?forumId=<%=forumId %>&amp;parent=<%=parent%>" method="post">
	<postfield name="title" value="$title"/>
	<postfield name="content" value="$content"/>
    </go>
    </anchor>
<br/>
<br/>
<%
if(parent != 0){
%>
<a href="ForumMessage.do?id=<%=parent%>">返回主贴</a><br/>
<%
}
%>
<a href="ForumIndex.do?id=<%=forumId%>">返回版面</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:notPresent>
</wml>