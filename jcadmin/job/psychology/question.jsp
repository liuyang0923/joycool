<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.IJobService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%response.setHeader("Cache-Control","no-cache");%>
<%
int id=0;
PsychologyBean psychology=null;

Vector answerList=null;
IJobService jobService=ServiceFactory.createJobService();
if(null!=request.getParameter("id")){
	id=StringUtil.toInt(request.getParameter("id"));
}
if(null!=request.getParameter("deleteAnswer")){
	int answerId=StringUtil.toInt(request.getParameter("deleteAnswer"));
	jobService.deletePsychologyAnswer("id="+answerId);
}
if(null!=request.getParameter("update")){
	int answerCount=StringUtil.toInt(request.getParameter("update"));
	String title=request.getParameter("title").trim();
	String content=request.getParameter("content").trim();
	jobService.updatePsychology("title='"+title+"',content='"+content+"'","id="+id);
	String answer=null;
	String explanation=null;
	int answerId=0;
	for(int i=1;i<=answerCount;i++){
		answerId=StringUtil.toInt(request.getParameter("answer"+i+"Id"));
		answer=request.getParameter("answer"+i).trim();
		explanation=request.getParameter("explanation"+i).trim();
		jobService.updatePsychologyAnswer("answer='"+answer+"',"+"explanation='"+explanation+"'","id="+answerId);
	}
}
String newAnswer=null;
String newExplanation=null;
String msg=null;
if(null!=request.getParameter("addAnswer")){
	PsychologyAnswerBean tempAnswer=null;
	newAnswer=request.getParameter("newAnswer").trim();
	newExplanation=request.getParameter("newExplanation").trim();
	if(newAnswer.equals("")){
		msg="答案不能为空!";
	}else if(newExplanation.equals("")){
		msg="解释不能为空!";
	}else{
		tempAnswer=jobService.getPsychologyAnswer("(answer='"+newAnswer+"' or explanation='"+newExplanation+"') and psychology_id="+id);
		if(null==tempAnswer){
			tempAnswer=new PsychologyAnswerBean();
			tempAnswer.setAnswer(newAnswer);
			tempAnswer.setExplanation(newExplanation);
			tempAnswer.setPsychologyId(id);
			jobService.addPsychologyAnswer(tempAnswer);
		}else{
			msg="答案已经存在!";
		}
	}
}
PsychologyBean tempPsychology=null;
int nextPsychologyId=0;
int prePsychologyId=0;
tempPsychology=jobService.getPsychology("id>"+id+" order by id limit 0,1");
if(null!=tempPsychology)
	nextPsychologyId=tempPsychology.getId();
else{
	tempPsychology=jobService.getPsychology("1=1 order by id limit 0,1");
	nextPsychologyId=tempPsychology.getId();
}

tempPsychology=jobService.getPsychology("id<"+id+" order by id desc limit 0,1");
if(null!=tempPsychology)
	prePsychologyId=tempPsychology.getId();
else{
	tempPsychology=jobService.getPsychology("1=1 order by id desc limit 0,1");
	prePsychologyId=tempPsychology.getId();
}
	
psychology=jobService.getPsychology("id="+id);
answerList=jobService.getPsychologyAnswerList("psychology_id="+id);
%>
<html>
<head>
</head>
<body>
心理测试题目管理<br/>
<a href="addQuestion.jsp">增加题目</a><br/>

<form method="post" action="question.jsp?id=<%=id%>&update=<%=answerList.size()%>">
修改题目&nbsp;&nbsp;<a href="question.jsp?id=<%=prePsychologyId%>">上一题</a>&nbsp;&nbsp;<a href="question.jsp?id=<%=nextPsychologyId%>">下一题</a><br/> 
<table width="800" border="1">
<tr><td width="30">操作项</td><td width="650">内容</td><td width="30">操作</td></tr>
<tr><td>标题</td><td><input name="title" value="<%=psychology.getTitle() %> " size="100%"></td><td>&nbsp;</td></tr>
<tr><td>正文</td><td><textarea name="content"  cols="80" rows="2" ><%=psychology.getContent()%></textarea></td><td>&nbsp;</td></tr>
<%
PsychologyAnswerBean answer=null;
for(int i=0;i<answerList.size();i++){
	answer=(PsychologyAnswerBean)answerList.get(i);
%>
<tr><td>答案<%=i+1 %></td><td><input name="answer<%=i+1%>" value="<%=answer.getAnswer()%>"  size="100%">
<input type="hidden" name="answer<%=i+1%>Id" value="<%=answer.getId()%>" size="100%"></td>
<td><a href="question.jsp?id=<%=id%>&deleteAnswer=<%=answer.getId()%>">删除</a></td>
<tr><td>答案<%=i+1 %>解释</td><td><textarea cols="80" rows="2" name="explanation<%=i+1 %>"  ><%=answer.getExplanation()%></textarea>
<input type="hidden" name="answer<%=i+1%>Id" value="<%=answer.getId()%>"></td>
<td><a href="question.jsp?id=<%=id%>&deleteAnswer=<%=answer.getId()%>">删除</a></td>
<%
}
%>
</table>
<input type="submit" name="submit" value="保存"><br/>
</form>


<form method="post" action="question.jsp?id=<%=id%>&addAnswer=1">
添加答案<br/>
<%if(null!=msg){ %><font color="red"><%=msg %></font><br/><%} %>
<table border="1" width="800">
<tr><td width="50">操作项</td><td width="700">内容</td></tr>
<tr><td>答案</td><td><input  name="newAnswer" value="<%if(null!=newAnswer&&null!=msg){%><%=newAnswer%><%}%>" size="100%"></td></tr>
<tr><td>解释</td><td><textarea name="newExplanation" cols="90" rows="4" ><%if(null!=newExplanation&&null!=msg){%><%=newExplanation%><%}%></textarea></td></tr>
</table>
<input type="submit" value="添加"><br/>
</form>

<font color="red">说明：1.点击删除会删除答案和答案相对应的解释</font><br/>
<font color="red">说明：2.请不要将ABCDE等放在答案里面,例如"A地铁站;"填写成"地铁站"就行了</font><br/>
<br/>
<a href="addQuestion.jsp">增加题目</a><br/>
<a href="index.jsp">返回心理测试题目管理首页</a><br/>

</body>
</html>