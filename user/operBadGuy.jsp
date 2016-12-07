<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
String returnUrl = (String)session.getAttribute("pagebeforeclick"); 
if(returnUrl==null){
	returnUrl = "ViewFriends.do";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的黑名单" ontimer="<%=response.encodeURL(returnUrl.replace("&", "&amp;"))%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
我的黑名单<br/>
---------<br/>
<%
if (request.getAttribute("manager") == null){
%>
操作成功！<br/>
<%
}else{
%>
该用户是管理员，你无权加入！<br/>
<%
}
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%
String toUserId = (String) request.getAttribute("friendId");
%>
<a href="/user/ViewUserInfo.do?userId=<%=toUserId%>">返回用户信息</a><br/>
<a href="/user/ViewFriends.do">返回好友列表</a><br/>
<a href="/chat/hall.jsp?roomId=0">进入聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>