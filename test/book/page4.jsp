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
if(null==session.getAttribute(questionName+4) || null==session.getAttribute(questionName+5) || null==session.getAttribute(questionName+6) || null==session.getAttribute(questionName+7)){
 //response.sendRedirect(("page3.jsp"));
 BaseAction.sendRedirect("/test/book/page3.jsp", response);
 return;
 }
Vector questionIds=new Vector();
for(int i = 8; i<15 ;i++){
questionIds.add(new Integer(i));
}
String answer2=((String)session.getAttribute(questionName+2));
if(!answer2.contains("4")){
questionIds.remove(new Integer(8));
}if(!answer2.contains("5")){
questionIds.remove(new Integer(9));
}if(!answer2.contains("6")){
questionIds.remove(new Integer(10));
}if(!answer2.contains("7")){
questionIds.remove(new Integer(11));
}if(!answer2.contains("8")){
questionIds.remove(new Integer(12));
}if(!answer2.contains("9")){
questionIds.remove(new Integer(13));
}if(!answer2.contains("10")){
questionIds.remove(new Integer(14));
}

if(request.getParameter("answer")!=null){
	String tempName=null;
	String redirectUrl="page5.jsp";
	for( int i=0;i<questionIds.size();i++){
		tempName=questionName+((Integer)questionIds.get(i)).toString();
		if(request.getParameter(tempName).equals("")){
			validate=false;
			info="请选择第"+(questionIds.get(i))+"题答案";
			break;
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
System.out.println(questionIds.size());
for(int i=0;i<questionIds.size();i++){
	vec=action.getQuestionAndAnswer(Integer.parseInt(questionIds.get(i)+""));
	question=(QuestionBean)vec.get(0);
	vecAnswerList=(Vector)vec.get(1);
	
%>
第<%=questionIds.get(i)%>题 <%=question.getQuestion() %><%= (question.getMultiple()==1)?"(多选)":"" %><br/>
<select <% if(question.getMultiple()==1){%> multiple="true" ivalue="0" <%}%> name="<%=questionName+(Integer)questionIds.get(i)%>"
<% if(request.getParameter(questionName+questionIds.get(i))!=null){
	
	String ivalue="0";
	String temp=null;
	temp=request.getParameter(questionName+questionIds.get(i));
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
<% if(question.getMultiple()==0){%>ivalue="<%=ivalue%>"<%}}%> > 
<% if(question.getMultiple()==0){%><option value="">请选择答案</option><%}%>
<%
 for(int j=0;j<vecAnswerList.size();j++){
  answer=(AnswerBean)vecAnswerList.get(j);
%><option  value="<%=answer.getId()%>"><%=answer.getAnswer()%></option> <%	 
if(j==vecAnswerList.size()-1){%></select><br/><%}}}%>
<anchor>下一页
    <go href="page4.jsp?answer=1" method="post">
    <%
    String questionId="";
    for(int i=0;i<questionIds.size();i++){
    	questionId=questionName+questionIds.get(i);
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