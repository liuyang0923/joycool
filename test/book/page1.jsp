<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.test.QuestionBean" %><%@ page import="net.joycool.wap.test.AnswerBean" %><%@ include file="../testAction.jsp" %><%
response.setHeader("Cache-Control","no-cache");
//该页显示的问题列表
int markQuestion=StringUtil.toInt(request.getParameter("markQuestion"));
if(markQuestion>0){
session.setAttribute("markQuestion","1");
}
int[] questionIds=new int[]{1,2};
String questionName="question";
TestAction action=new TestAction(request);
//调查问卷是否已经结束
if(action.isTestFinished()){
	//response.sendRedirect(("infoTested.jsp?isFinished=1"));
	BaseAction.sendRedirect("/test/book/infoTested.jsp?isFinished=1", response);
	return;
}
//是否已经填写过调查问卷
if(action.isTested()==true){
	//response.sendRedirect(("infoTested.jsp"));
	BaseAction.sendRedirect("/test/book/infoTested.jsp", response);
	return;
}
boolean validate=true;
String info=null;
//提交处理，如果成功则跳转到下一个页面，否则在当前页面显示错误信息
if(request.getParameter("answer")!=null)
{
	String tempName=null;
	for( int i=0;i<questionIds.length;i++){
		tempName=questionName+questionIds[i];
		if(request.getParameter(tempName).equals(""))
		{
			validate=false;
			info="请选择第"+questionIds[i]+"题答案";
			break;
		}
		session.setAttribute(tempName,request.getParameter(tempName));
	}
   if(request.getParameter(questionName+2)!=null){
		String answer2=((String)request.getParameter(questionName+2));
		String[] answers=answer2.split(";");
		if(answers.length<3){
			info="第2题至少选3项";
			validate=false;
		}
	}
	String redirectUrl="page2.jsp";
%>
<%if(validate==true){%><%=JCRoomChatAction.getTransferPage(redirectUrl)%><%return;}%><%}
Vector vec=null;
Vector vecAnswerList=null;
QuestionBean question=null;
AnswerBean answer=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="书籍和音像制品调研">
<p align="left">
为了更好的为乐酷用户提供wap购物服务,特设立此次调查,以便准备更丰富的商品供您选择。只要您认真参与调查,调查结束时您即可获赠20元代金券在乐酷购物使用。<br/>
--------------------------<br/>
<%if (info!=null){%><%=info%><br/><%}%>
<%
for(int i=0;i<questionIds.length;i++){
	vec=action.getQuestionAndAnswer(questionIds[i]);
	question=(QuestionBean)vec.get(0);
	vecAnswerList=(Vector)vec.get(1);
%>
第<%=question.getQuestionId() %>题 <%=question.getQuestion() %><% 
if(question.getId()!=2){
	%><%= (question.getMultiple()==1)?"(多选)":"" %><%
}else{
	%>(多选，至少选3项)<%
}
%><br/>
<select <% if(question.getMultiple()==1){%> multiple="true" <%}%> name="<%=questionName+questionIds[i]%>"
<% 
//这个主要为了在用户填写错误时，把已经作了答的问题的选项选中
if(request.getParameter(questionName+questionIds[i])!=null){
	String ivalue="0";
	String temp=null;
	temp=request.getParameter(questionName+questionIds[i]);
	if(!(question.getMultiple()==1||temp.equals(""))){
		for(int j=0;j<vecAnswerList.size();j++)
		{
			answer=(AnswerBean)vecAnswerList.get(j);
			if(answer.getId()==Integer.parseInt(temp)){
				ivalue=(j+1)+"";
			}
		}
	}
	%>
ivalue="<%=ivalue%>"<%}%>> 
<% if(question.getMultiple()==0){%><option value="">请选择答案</option><%}%>
<%
 for(int j=0;j<vecAnswerList.size();j++){
  answer=(AnswerBean)vecAnswerList.get(j);
%><option  value="<%=answer.getId()%>"><%=answer.getAnswer()%></option> <%	 
if(j==vecAnswerList.size()-1){%></select><br/><%}}}%>
<anchor>下一页
<go href="page1.jsp?answer=1" method="post">
<%
String questionId="";
for(int i=0;i<questionIds.length;i++){
    questionId=questionName+questionIds[i];
%>
<postfield name="<%=questionId%>" value="<%="$"+questionId%>"/><%}%></go>
</anchor><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>