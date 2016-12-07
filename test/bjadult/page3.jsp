<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.test.*"%><%
response.setHeader("Cache-Control","no-cache");
TestAction action=new TestAction(request);
if(action.getLoginUser().getCityno()!=55){
	//response.sendRedirect(("infoNonBj.jsp"));
	BaseAction.sendRedirect("/test/bjadult/infoNonBj.jsp", response);
	return;
}
if(action.isTestFinished()==true){
	//response.sendRedirect(("infoTested.jsp?isFinished=1"));
	BaseAction.sendRedirect("/test/bjadult/infoTested.jsp?isFinished=1", response);
	return;
}
if(action.isTested()==true){
	//response.sendRedirect(("infoTested.jsp"));
	BaseAction.sendRedirect("/test/bjadult/infoTested.jsp", response);
	return;
}
int tempInt=0;//题号编号需要减小
String questionName="question";
String info=null;
boolean validate=true;
if(null==session.getAttribute(questionName+1)){
 //response.sendRedirect(("page1.jsp"));
 BaseAction.sendRedirect("/test/bjadult/page1.jsp", response);
 return;
 }
if(null==session.getAttribute(questionName+2)){
 //response.sendRedirect(("page2.jsp"));
 BaseAction.sendRedirect("/test/bjadult/page2.jsp", response);
 return;
 }
int answer2=Integer.parseInt((String)session.getAttribute(questionName+2));

int[] questionIds=new int[]{3,4,5,6,7,8,9,10};
if(answer2==6||answer2==8){
	questionIds=new int[]{18,19,20,21,22,23,24};
	tempInt=15;
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
	}
%><%if(validate==true){%><%=JCRoomChatAction.getTransferPage("page4.jsp")%><%return;}%><%}

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
%>
第<%=question.getQuestionId()-tempInt%>题 <%=question.getQuestion() %><br/>
<select <% if(question.getMultiple()==1){%> multiple="true" <%}%> name="<%=questionName+questionIds[i]%>"
<% if(request.getParameter(questionName+questionIds[i])!=null){
	
	String ivalue="0";
	String temp=null;
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