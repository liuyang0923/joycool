<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ include file="../testAction.jsp" %><%
response.setHeader("Cache-Control","no-cache");
TestAction action=new TestAction(request);
//是否为北京用户(这次调查不用)
//if(action.getLoginUser().getCityno()!=55){
//	response.sendRedirect(("infoNonBj.jsp"));
//	return;
//}
if(action.isTestFinished()==true){
	//response.sendRedirect(("infoTested.jsp?isFinished=1"));
	BaseAction.sendRedirect("/test/book/infoTested.jsp?isFinished=1", response);
	return;
}
if(action.isTested()==true){
	//response.sendRedirect(("infoTested.jsp"));
	BaseAction.sendRedirect("/test/book/infoTested.jsp", response);
	return;
}

String questionName="question";
if(null==session.getAttribute(questionName+1) || null==session.getAttribute(questionName+2)){
 //response.sendRedirect(("page1.jsp"));
 BaseAction.sendRedirect("/test/book/page1.jsp", response);
 return;
 }
if(null==session.getAttribute(questionName+3)){
 //response.sendRedirect(("page2.jsp"));
 BaseAction.sendRedirect("/test/book/page2.jsp", response);
 return;
 }
if(null==session.getAttribute(questionName+4) || null==session.getAttribute(questionName+5) || null==session.getAttribute(questionName+6) || null==session.getAttribute(questionName+7)){
 //response.sendRedirect(("page3.jsp"));
 BaseAction.sendRedirect("/test/book/page3.jsp", response);
 return;
 }
int[] questionIds=new int[]{18};
String info=null;
boolean validate=true;
if(request.getParameter("answer")!=null){
	String tempName=null;
	String redirectUrl="page8.jsp";
	for( int i=0;i<questionIds.length;i++){
		tempName=questionName+questionIds[i];
		if(request.getParameter(tempName).equals(""))
		{
			validate=false;
			info="请选择第"+(questionIds[i])+"题答案";
			break;
		}
		//判断是否做电子数码问卷
        if(request.getParameter(tempName).contains("96")){
		redirectUrl="page7.jsp";
		}
		session.setAttribute(tempName,request.getParameter(tempName));
	}
%><%if(validate==true){%><%=JCRoomChatAction.getTransferPage(redirectUrl)%><%return;}%><%}
Vector vec=null;
Vector vecAnswerList=null;
QuestionBean question=null;
AnswerBean answer=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="个人用户问卷">
<p align="left">
<%if (info!=null){%><%=info%><br/><%}%>
<%
for(int i=0;i<questionIds.length;i++){
	vec=action.getQuestionAndAnswer(questionIds[i]);
	question=(QuestionBean)vec.get(0);
	vecAnswerList=(Vector)vec.get(1);
%>
第<%=question.getQuestionId()%>题 <%=question.getQuestion() %><%= (question.getMultiple()==1)?"(多选)":"" %><br/>
<select <% if(question.getMultiple()==1){%> multiple="true" <%}%> name="<%=questionName+questionIds[i]%>"
<% if(request.getParameter(questionName+questionIds[i])!=null){
	
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
ivalue="<%=ivalue%>"<%}%> > 
<% if(question.getMultiple()==0){%><option value="">请选择答案</option><%}%>
<%
 for(int j=0;j<vecAnswerList.size();j++){
  answer=(AnswerBean)vecAnswerList.get(j);
%><option  value="<%=answer.getId()%>"><%=answer.getAnswer()%></option> <%	 
if(j==vecAnswerList.size()-1){%></select><br/><%}}}%>
<anchor>下一页
    <go href="page6.jsp?answer=1" method="post">
    <%
    String questionId="";
    for(int i=0;i<questionIds.length;i++){
    	questionId=questionName+questionIds[i];
    %>
    <postfield name="<%=questionId%>" value="<%="$"+questionId%>"/>
    <%}
    %>
    </go>
</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>