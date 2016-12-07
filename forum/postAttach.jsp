<%@ page contentType="application/vnd.wap.xhtml+xml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发表话题</title>
</head>
<p align="left"><%=BaseAction.getTop(request, response)%>
发表话题<br/>
------------------<br/>
<logic:present name="result" scope="request">
<%
int forumId = ((Integer)request.getAttribute("forumId")).intValue();
int parent = ((Integer)request.getAttribute("parent")).intValue();
%>
    <logic:equal name="result" value="failure">
<p align="left"><%=BaseAction.getTop(request, response)%>
操作失败：<br/>
<bean:write name="tip" filter="false"/><br/>
<br/>
<%
if(parent != 0){
%>
<a href="forumMessage.jsp?id=<%=parent%>">返回主贴</a><br/>
<%
}
%>
<a href="forumIndex.jsp?id=<%=forumId%>">返回论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</p>
    </logic:equal>
    <logic:equal name="result" value="success">
<p align="left"><%=BaseAction.getTop(request, response)%>
发表成功！<br/>
建议您发消息给<a href="http://wap.joycool.net/user/sendMessage.jsp?backTo=http://wap.joycool.net/forum/forumIndex.jsp?id=13&amp;toUserId=431">管理员</a>留下联系方式，以便刊登您的作品时与您联系！<br/>
（您如果留过联系方式，请返回论坛）<br/>
<br/>
<%
if(parent != 0){
%>
<a href="forumMessage.jsp?id=<%=parent%>">返回主贴</a><br/>
<%
}
%>
<a href="forumIndex.jsp?id=<%=forumId%>">返回论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
    </logic:equal>
</logic:present>
<logic:notPresent name="result" scope="request">
<%
int forumId = Integer.parseInt(request.getParameter("forumId"));
int parent = Integer.parseInt(request.getParameter("parent"));
%>
<form name="attForm" ENCTYPE="multipart/form-data" method="post" action="<%=response.encodeURL("PostAttach.do?forumId="+forumId+"&amp;parent="+parent)%>">
标题*:<br/><input name="title" value=""/><br/>
内容*:<br/><input name="content" value=""/><br/>
文件*:<br/><input type="file" name="attachment"/><br/>
<input type="submit" name="submit" value="发表"/><br/>
注：支持WAP2.0的手机才可以上传图片。
</form>
<br/>
<%
if(parent != 0){
%>
<a href="forumMessage.jsp?id=<%=parent%>">返回主贴</a><br/>
<%
}
%>
<a href="forumIndex.jsp?id=<%=forumId%>">返回论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</logic:notPresent>
</p>
</html>