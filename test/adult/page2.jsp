<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.test.*"%><%
response.setHeader("Cache-Control","no-cache");
int tempInt=0;//题号编号需要减小
String questionName="question";
String info=null;
TestAction action=new TestAction(request);
if(action.isTestFinished()==true){
	//response.sendRedirect(("infoTested.jsp?isFinished=1"));
	BaseAction.sendRedirect("/test/adult/infoTested.jsp?isFinished=1", response);
	return;
}
if(action.isTested()==true){
	//response.sendRedirect(("infoTested.jsp"));
	BaseAction.sendRedirect("/test/adult/infoTested.jsp", response);
	return;
}

boolean validate=true;
int answer1=Integer.parseInt((String)request.getSession().getAttribute(questionName+1));
int[] questionIds=new int[]{2};////第四题回答为缺省为A选项
if(answer1==1){
	questionIds=new int[]{3};
	tempInt=1;
}
String redirectUrl=null;
if(request.getParameter("answer")!=null){
	
	String tempName=null;
	for( int i=0;i<questionIds.length;i++){
		tempName=questionName+questionIds[i];
		if(request.getParameter(tempName).equals(""))
		{
			validate=false;
			info="请选择第"+(questionIds[i]-tempInt)+"题答案";
			break;
		}
		session.setAttribute(tempName,request.getParameter(tempName));
	if(answer1==1){
		redirectUrl="info.jsp";
		int count=33;
		for(int j=4;j<=count;j++){
			tempName=questionName+j;
			if(null!=session.getAttribute(tempName))
				session.removeAttribute(tempName);
		}
		tempName=questionName+2;
		if(null!=session.getAttribute(tempName))
			session.removeAttribute(tempName);
	}
	else{
		redirectUrl="page3.jsp";
		tempName=questionName+3;
		if(null!=session.getAttribute(tempName))
			session.removeAttribute(tempName);
	}
}
%><%if(validate==true){%><%=JCRoomChatAction.getTransferPage(redirectUrl)%><%return;}%><%}
Vector vec=null;
Vector vecAnswerList=null;
QuestionBean question=null;
AnswerBean answer=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="成人用品调查问卷">
<p align="left">
<%if (info!=null){%><%=info%><br/><%}%>
<%
for(int i=0;i<questionIds.length;i++){
	vec=action.getQuestionAndAnswer(questionIds[i]);
	question=(QuestionBean)vec.get(0);
	vecAnswerList=(Vector)vec.get(1);
	answer=(AnswerBean)vecAnswerList.get(0);
%>
第<%=question.getQuestionId()-tempInt%>题<%=question.getQuestion() %><br/>
<select <% if(question.getMultiple()==1){%> multiple="true" ivalue="0" <%}%> name="<%=questionName+questionIds[i]%>"
<% if(request.getParameter(questionName+questionIds[i])!=null){
	
	String ivalue="0";
	String temp=null;
	String[] tempStr=null;
	temp=request.getParameter(questionName+questionIds[i]);
	if(!temp.equals("")){
			for(int j=0;j<vecAnswerList.size();j++)
			{
				answer=(AnswerBean)vecAnswerList.get(j);
				if(answer.getId()==Integer.parseInt(temp)){
					ivalue=(j+1)+"";
			}
		  }
	}
%><% if(question.getMultiple()==0){%>ivalue="<%=ivalue%>"<%}}%> > 
<% if(question.getMultiple()==0){%><option value="">请选择答案</option><%}%>
<%
 for(int j=0;j<vecAnswerList.size();j++){
  answer=(AnswerBean)vecAnswerList.get(j);
%><option   value="<%=answer.getId()%>"><%=answer.getAnswer()%></option> <%	 
if(j==vecAnswerList.size()-1){%></select><br/><%}}}%>
<anchor>下一页
    <go href="page2.jsp?answer=1" method="post">
    <%
    String questionId="";
    for(int i=0;i<questionIds.length;i++){
    	questionId=questionName+questionIds[i];
    %><postfield name="<%=questionId%>" value="<%="$"+questionId%>" /><%}%></go>
</anchor><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>