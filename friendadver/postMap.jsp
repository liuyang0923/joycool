<%@ page contentType="application/vnd.wap.xhtml+xml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
if(true){
	response.sendRedirect("postWord.jsp");
	return;
}
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String friendAdverId=(String)request.getParameter("id");
if(friendAdverId==null) friendAdverId=(String)request.getAttribute("id");
String backTo=request.getParameter("backTo");
if(backTo==null||backTo.equals("")) backTo="/friendadver/friendAdverIndex.jsp";
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发表广告</title>
</head>
<p align="left"><%=BaseAction.getTop(request, response)%>
回帖<br/>
------------------<br/>
<logic:present name="result" scope="request">
<p align="left">
回帖成功！<br/>
<a href="<%=("friendAdverMessage.jsp?id="+(String)request.getAttribute("id"))%>">返回主帖</a><br/>
<a href="friendAdverIndex.jsp">返回交友广告</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</logic:present>
<logic:notPresent name="result" scope="request">
<logic:present name="tip">
<font color="red"><bean:write name="tip"/></font><br/>
</logic:present>
<form name="attForm" ENCTYPE="multipart/form-data" method="post" action="<%=response.encodeURL("/friendadver/PostMap.do")%>">
<input type="hidden" name="title" value="<%=friendAdverId%>"/>
内容:<br/><input name="content" value="<logic:present name="content"><bean:write name="content"/></logic:present>"/><br/>
图片*:<br/><input type="file" name="attachment" value=""/><br/>
<input type="submit" name="submit" value="发表"/><br/>
注：支持WAP2.0的手机才可以上传图片。
</form>
<br/>
<a href="<%=(backTo.replace("&","&amp;"))%>">返回主帖</a><br/>
<a href="friendAdverIndex.jsp">返回交友广告</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</logic:notPresent>
</p>
</html>