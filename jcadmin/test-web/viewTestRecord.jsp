<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.test.*"%><%@ page import="net.wxsj.bean.test.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.util.*,net.wxsj.util.db.*,net.joycool.wap.util.UserInfoUtil"%><%@ page import="java.util.ArrayList"%><%
response.setHeader("Cache-Control","no-cache");
if(request.getParameter("delete") != null){
	int delete = StringUtil.toInt(request.getParameter("delete"));
	int testId = StringUtil.toInt(request.getParameter("id"));
	DbOperation dbOp = new DbOperation();
	dbOp.init();
	dbOp.executeUpdate("delete from new_test_record where user_id = " + delete + " and test_id = " + testId);
	dbOp.release();
	response.sendRedirect("viewTestRecord.jsp?id=" + testId);
	return;
}

WebTestAction action = new WebTestAction();
action.getRecordListByTestId(request, response);
%>
<html>
<head>
<title>调查问卷</title>
</head>
<body>
<p align="left">
调查问卷用户答案<br/>
--------------<br/>
<%
int questionCount = ((Integer)request.getAttribute("questionCount")).intValue();
ArrayList recordList = (ArrayList) request.getAttribute("recordList");
TestRecordBean record = null;
TestRecordBean preRecord = null;
int totalCount = 0;
%>
<table border="1">
	<tr>
	    <td>操作</td>
		<td>用户ID</td>
		<td>用户所在地</td>
<%
for(int j = 0; j < questionCount; j++){
%>
		<td>问题<%= j + 1 %></td>
<%
}
int questionIndex = 0;
UserBean user = null;
for(int i = 0; i < recordList.size(); i ++){
	record = (TestRecordBean) recordList.get(i);
	user = UserInfoUtil.getUser(record.getUserId());
	questionIndex ++;
	if(questionIndex > questionCount){
		questionIndex = questionIndex % questionCount;
	}
	if(preRecord == null || preRecord.getUserId() != record.getUserId()){
		questionIndex = 1;
		totalCount ++;
%>
	</tr>
	<tr>
	    <td><a href="viewTestRecord.jsp?delete=<%=record.getUserId()%>&id=<%=record.getTestId()%>" onclick="return confirm('确认删除？')">删除</a></td>
		<td><%= record.getUserId() %></td>
		<td><%= user.getCityname() %></td>
<%
	}
	while(questionIndex < record.getQuestionCode()){
%>
		<td>&nbsp;</td>
<%
		questionIndex ++;
	}
	if(record.getQuestionCode() == questionIndex){
%>
		<td><%= record.getAnswerCode() %></td>
<%
	}
	preRecord = record;
%>
<%
}
%>
</tr>
</table>
到目前为止一共有<strong><%=totalCount%></strong>个用户接受了调查。<br>
</body>
</html>