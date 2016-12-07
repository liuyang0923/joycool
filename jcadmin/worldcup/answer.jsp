<!-- fanys-worldcup-2006-6-14-14 total-->
<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.wc.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
WorldCupAction  action=new WorldCupAction(request);
String title="";
String money="";
String questionId=request.getParameter("id");
String answerId=request.getParameter("answerId");;
if(request.getParameter("title")!=null){
	if(answerId!=null)
		action.updateAnswer();//修改一个结果
	else
		action.addAnswer();//增加一个结果
	//response.sendRedirect(("viewQuestion.jsp?id="+questionId));
	BaseAction.sendRedirect("/jcadmin/worldcup/viewQuestion.jsp?id="+questionId, response);
}
if(answerId!=null){
	WcAnswerBean answer=action.getAnswer(request.getParameter("answerId"));
	title=answer.getTitle();
	money=answer.getMoney()+"";
}
	
%>
<script language="JavaScript">

 function checkform(){
	if(document.form1.title.value == ''){
		alert("标题不能为空！");
		document.form1.title.focus();
		return false;
	}
	if(checknum(document.form1.money.value)==false){
		document.form1.money.value='';
		document.form1.money.focus();
		return false;
	}
	return true;
} 
//校验是否为数字
　　function checknum(p)
　　{
	　　if (p == "")
	　　{
			alert("赔率输入不能为空！");
 	　	　   return false;
	　　}
	　	var l = p.length;
	　　	var count=0;
	　　	for(var i=0; i<l; i++)
	　　{
		　　var digit = p.charAt(i);
		　　if(digit == "." )
		　　{
		　　	++count;
		　	　if(count>1)
		　　		{
					alert ("赔率输入类型必须为数字！");
			　　		return false;
		　　		}
		　　}
		　　else if(digit < "0" || digit > "9")
		　　{
				alert ("赔率输入类型必须为数字！");
		　　		return false;
		　　}
	　　}
	　　return true;
　　}



</script>

<form name="form1" method="post" action="answer.jsp?id=<%=questionId%>">
<table>
<tr><td>标题</td><td><input type="input" name="title" value="<%=title%>"></td></tr>
<tr><td>赔率</td><td><input type="input" name="money" value="<%=money%>"></td></tr>
<%
 if(answerId!=null){
%>
<tr><td><input name="answerId" type="hidden" value="<%=answerId%>"></td></tr>
<%} %>
<tr><td><input type="submit" value="确定" onclick="return checkform()" ></td><td><input type="reset" value="重设"></td></tr>
</table>
</form>
<a href="viewQuestion.jsp?id=<%=questionId%>">
返回
</a>
</body>
</html>