<!-- fanys-worldcup-2006-6-14-14 total-->
<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.wc.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/JavaScript" language="Javascript1.1" src="uc.js"></script> 
<title>Insert title here</title>
</head>
<%
if(request.getParameter("title")!=null){
	WorldCupAction action=new WorldCupAction(request);
	if(request.getParameter("id")!=null){
		action.updateQuestion();//更新一个博彩场次
	}else{
		action.addQuestion();//增加一个博彩场次
	}
	//response.sendRedirect(("index.jsp"));
	BaseAction.sendRedirect("/jcadmin/worldcup/index.jsp", response);
}
WcQuestionBean question=null;
String title="";
String date="today";
int hour=0;
int minute=0;

if(request.getParameter("id")!=null){
	WorldCupAction action=new WorldCupAction(request);
	question=action.getQuestion(request.getParameter("id"));
	title=question.getTitle();
	String starttime=question.getEndDatetime2();
	date=starttime.substring(0,10);
	hour=Integer.parseInt(starttime.substring(11,13));
	minute=Integer.parseInt(starttime.substring(14,16));
	//System.out.println("date="+date+"hour="+hour+"minute"+minute);
}

%>
<body>
<form name="form1" method="post" action="<%=("question.jsp") %>">
<table>
<tr><td>名称</td><td><input name="title" type="input" value="<%=title %>"></td></tr>
<tr>
<td>开始时间</td>
<td>
<script language="javascript">
  var date1 = new UncCalendar ("datetime", "<%=date%>");
  date1.display();
</script>
<select  name="hour">
<%
 for( int i=0;i<24;i++){
	 if(i==hour){
%>
<option selected><%=i%></option>
<%}else{ %>
<option ><%=i%></option>
<%} }%>
</select>时
<select  name="minute">
<% for(int i=0;i<60;i++){
 if(i==minute){
%>
<option selected><%=i%></option>
<%}else{ %>
<option ><%=i%></option>
<%} }%>
</select>分

</td>
</tr>
<%
if(request.getParameter("id")!=null){
%>
<tr><td><input type="hidden" name="id" value="<%=request.getParameter("id") %>"></td></tr>
<%} %>
<tr><td><input type="submit" value="确定" onclick="return checkform()" ></td><td><input type="reset" value="重设"></td></tr>
</table>
</form>
<a href="index.jsp">返回</a>
</body>
</html>