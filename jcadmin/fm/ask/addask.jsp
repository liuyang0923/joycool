<%@ page contentType="text/html;charset=utf-8" language="java" %><%

%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>添加题目</title>
<link href="/jcadmin/farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<form action="asklist.jsp" method="post">
输入题目<br/>
<input type="text" name="question" /><br/>
输入答案<br/>
A:<input type="text" name="answer1"/><br/>
B:<input type="text" name="answer2"/><br/>
C:<input type="text" name="answer3"/><br/>
D:<input type="text" name="answer4"/>
<input type="hidden" name="cmd" value="add"/><br/>
正确答案:<input type="radio" name="rightanswers" value="1" checked />A
<input type="radio" name="rightanswers" value="2" />B
<input type="radio" name="rightanswers" value="3" />C
<input type="radio" name="rightanswers" value="4" />D<br/>
<input type="submit" value="确定" /><br/>
</form>
<a href="asklist.jsp">返回上一页</a><br/>
</body>
</html>