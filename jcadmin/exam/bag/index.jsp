<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<html>
	<head>
		<title>备战考试</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
用户输入管理:<br/>
┗<a href="userPublic.jsp">按UID查询用户的书包</a><br/>
┗<a href="allUser.jsp">全部用户的书包</a><br/>
┗<a href="allNote.jsp">全部留言</a><br/>
---------------------------<br/>
题库管理：<br/>
┗<a href="add.jsp">增加</a><br/>
┗<a href="modify.jsp">修改</a><br/>
┗<a href="stat.jsp">统计</a>(正式服上最好不要用这个功能。因为速度非常慢。)<br/>
	</body>
</html>