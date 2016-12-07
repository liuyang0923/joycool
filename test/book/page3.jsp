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
int tempInt=0;//题号编号需要减小
String questionName="question";
String info=null;
boolean validate=true;
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
//int answer2=Integer.parseInt((String)session.getAttribute(questionName+3));

int[] questionIds=new int[]{4,5,6,7};
//if(answer2==6||answer2==8){
//	questionIds=new int[]{18,19,20,21,22,23,24};
//	tempInt=15;
//}
String redirectUrl=null;
if(request.getParameter("answer")!=null){
	String tempName=null;
	redirectUrl="page4.jsp";
	for( int i=0;i<questionIds.length;i++){
		tempName=questionName+questionIds[i];
		if(request.getParameter(tempName).equals(""))
		{
			validate=false;
			info="请选择第"+(questionIds[i])+"题答案";
			break;
		}
		if(request.getParameter(tempName).equals("38")){
		redirectUrl="page6.jsp";
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
<card title="书籍和音像制品调研">
<p align="left">
<%if (info!=null){%><%=info%><br/><%}%>
<%
for(int i=0;i<questionIds.length;i++){
	vec=action.getQuestionAndAnswer(questionIds[i]);
	question=(QuestionBean)vec.get(0);
	vecAnswerList=(Vector)vec.get(1);
%>
<%if(question.getQuestionId()==7){%>
如果在wap上有一个图书音像店，种类齐全，产品有图文结合介绍，价格优惠。<br/>
<%}%>
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
    <go href="page3.jsp?answer=1" method="post">
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