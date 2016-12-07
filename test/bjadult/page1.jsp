<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.test.*"%><%
response.setHeader("Cache-Control","no-cache");
int[] questionIds=new int[]{1};
String questionName="question";
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
boolean validate=true;
String info=null;
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
	String redirectUrl="page2.jsp";
	int answer1=Integer.parseInt((String)request.getSession().getAttribute(questionName+1));
	if(answer1==1)
		redirectUrl="info.jsp";
%>
<%if(validate==true){%><%=JCRoomChatAction.getTransferPage(redirectUrl)%><%return;}%><%}
Vector vec=null;
Vector vecAnswerList=null;
QuestionBean question=null;
AnswerBean answer=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="成人用品调查问卷">
<p align="left">
说明：认真填写此问卷，即可领取中秋大礼：最高500点经验值！<br/>
（介绍）在wap上有这样一个成人用品店，店里有各种成人用品（避孕用品、仿真器具、刺激性欲延时用品、保健护理用品、情趣用品等），货品齐全，价格比普通购买方式低廉，质量保证，同时配有详细功能介绍和应用案例（图文结合）。在这个wap小店上就能方便地进行订购，并能很隐秘地邮寄或送货上门。<br/>
--------------------------<br/>
<%if (info!=null){%><%=info%><br/><%}%>
<%
for(int i=0;i<questionIds.length;i++){
	vec=action.getQuestionAndAnswer(questionIds[i]);
	question=(QuestionBean)vec.get(0);
	vecAnswerList=(Vector)vec.get(1);
%>
第<%=question.getQuestionId() %>题 <%=question.getQuestion() %><br/>
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