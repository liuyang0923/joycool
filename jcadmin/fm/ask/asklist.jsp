<%@ page contentType="text/html;charset=utf-8" language="java" %><%@ page import="java.util.*,jc.family.game.ask.*,net.joycool.wap.util.*" %><%
AskAction action=new AskAction(request,response);
String cmd=request.getParameter("cmd");
if(cmd!=null&&cmd.equals("add")){
action.addAskBean();
}
if(cmd!=null&&cmd.equals("edit")){
action.editAskBean();
}
if(cmd!=null&&cmd.equals("del")){
action.deleteAskBean();
}
if(cmd!=null&&cmd.equals("delall")){
action.deleteAllAsk();
}
List list=action.getAskBeanList();
String paging=(String)request.getAttribute("pages");
Integer count=(Integer)request.getAttribute("count");
int p =action.getParameterInt("p");
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>显示所有</title>
<link href="/jcadmin/farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<a href="addask.jsp">添加新问题</a>&#160;<a href="asklist.jsp?cmd=delall" onclick="return confirm('确定删除问答题库吗?')">删除所有</a><%
if(count!=null&&count.intValue()<30){%><div style="color:#CC0000">题数不足30道,不能开赛~</div><%}%>
<table border="1">
	<tr>
		<td>序号</td>
		<td>题目</td>
		<td>答案</td>
		<td>正确答案</td>
		<td>操作</td>
	</tr><%
	if(list==null||list.size()==0){%>
	<tr><td colspan="5">没有家族问答题库</td></tr>
	<%}
	for(int i=0;i<list.size();i++){
		AskBean ask=(AskBean)list.get(i);
	%><tr><td><%=30-(i+p*10)%>/30</td>
	<td><%=StringUtil.toWml(ask.getQuestion())%></td>
	<td>A:<%=StringUtil.toWml(ask.getAnswer1())%><br/>B:<%=StringUtil.toWml(ask.getAnswer2())%><br/>
	C:<%=StringUtil.toWml(ask.getAnswer3())%><br/>D:<%=StringUtil.toWml(ask.getAnswer4())%></td>
	<td><%=ask.getRightanswersStr()%></td>
	<td><a href="editask.jsp?askid=<%=ask.getId()%>">修改</a>|<a href="asklist.jsp?cmd=del&askid=<%=ask.getId()%>">删除</a></td></tr>
	<%}%>
</table>
<%=paging%><br/>
<a href="/jcadmin/fm/index.jsp">返回家族首页</a>
</body>
</html>
