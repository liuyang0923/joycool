<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%
String mobile=request.getParameter("mobile");
String message=request.getParameter("message");

if(mobile!=null&&mobile.length()==11&&message!=null&&message.length()>1){  
	SmsUtil.sendSms(message, mobile);
	BaseAction.sendRedirect("sendSms.jsp", response);
	return;
}
%><html>
<head>
<title>缓存管理</title>
</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>

<form action="sendSms.jsp" method="post">
<input type="text" name="mobile" value="13">
<input type="text" name="message">
<input type="submit" value="发送">
</form>
</body>
</html>