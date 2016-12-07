<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String backTo=request.getParameter("backTo");
if(backTo==null||backTo.equals("")) backTo="/friend/friendInfo.jsp";
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
</head>
<body>
<%=BaseAction.getTop(request, response)%>
增加个人形象照片<br/>
------------------------<br/>
<logic:present name="result" scope="request">
<p align="left">
增加成功！<br/>
<a href="friendInfo.jsp">返回交友用户信息</a><br/>
<a href="friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</logic:present>
<logic:notPresent name="result" scope="request">
<logic:present name="tip">
<font color="red"><bean:write name="tip"/></font><br/>
</logic:present>
<form name="attForm" enctype="multipart/form-data" method="post" action="<%=response.encodeURL("/friend/UploadPhoto.do")%>">
图片*:128*128以内<br/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="file" name="attachment" value=""/><br/>
<br/><input type="submit" name="submit" value="发表"/><br/>
注：支持WAP2.0的手机才可以上传图片。
</form>
<a href="friendInfo.jsp">返回交友用户信息</a><br/>
<a href="friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</logic:notPresent>
</body>
</html>