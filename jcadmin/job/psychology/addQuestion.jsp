<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.IJobService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.framework.*"%><%response.setHeader("Cache-Control","no-cache");%>
<%
String msg=null;
String title=null;
String content=null;
if(null!=request.getParameter("add")){
	IJobService jobService=ServiceFactory.createJobService();
	PsychologyBean psychology=null;
	title=request.getParameter("title").trim();
	content=request.getParameter("content").trim();
	if(title.equals("")){
		msg="标题不能为空!";
	}
	else if(content.equals("")){
		msg="正文不能为空!";
	}else{
		psychology=jobService.getPsychology("title='"+title+"' or content='"+content+"' ");
		if(null==psychology){
			psychology=new PsychologyBean();
			psychology.setTitle(title);
			psychology.setContent(content);
				
			jobService.addPsychology(psychology);
			psychology=jobService.getPsychology("title='"+title+"'");
			//response.sendRedirect("question.jsp?id="+psychology.getId());
			BaseAction.sendRedirect("/jcadmin/job/psychology/question.jsp?id="+psychology.getId(), response);
			return;
		}else{
			msg="该题目已经存在";
		}
	}

}
%>
<html>
<head>
</head>
<body>
<form method="post" action="addQuestion.jsp?add=1">
增加题目<br/>
<%if(null!=msg){ %><font color="red"><%=msg %></font><%} %>
<table width="800" border="1">
<tr><td width="30">操作项</td><td width="750">内容</td></tr>
<tr><td>标题</td><td><input name="title" type="text" value="<%if(null!=title&&null!=msg){%><%=title%><%}%>" size="100%"></td></tr>
<tr><td>正文</td><td><textarea name="content" cols="80" rols="4"   width="100%"><%if(null!=content&&null!=msg){%><%=content%><%}%></textarea></td></tr>
</table>
<input type="submit" name="submit" value="添加">
</form>
<a href="index.jsp">返回心理测试题目管理首页</a><br/>
</body>
</html>