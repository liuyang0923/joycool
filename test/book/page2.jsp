<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.util.StringUtil" %><%@ include file="../testAction.jsp" %><%
response.setHeader("Cache-Control","no-cache");
int tempInt=0;//题号编号需要减小
String questionName="question";
String info=null;
TestAction action=new TestAction(request);

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
//没有获得到第一题或者第二题答案跳转从新填写
if(null==session.getAttribute(questionName+1) || null==session.getAttribute(questionName+2)){
 //response.sendRedirect(("page1.jsp"));
 BaseAction.sendRedirect("/test/book/page1.jsp", response);
 return;
 }

boolean validate=true;
int[] questionIds=new int[]{3};////第四题回答为缺省为A选项
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
%><%if(validate==true){%><%=JCRoomChatAction.getTransferPage("page3.jsp")%><%return;}%><%}
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
String question2=(String)session.getAttribute(questionName+2);
question2=question2+";18";
String[] question3 =question2.split(";");
%>
第3题 下列这些产品中哪些平常购买不便？(多选)<br/>
<select  multiple="true" ivalue="0"  name="<%=questionName+questionIds[0]%>"> 
<%
 for(int j=0;j<question3.length;j++){
 int num=StringUtil.toInt(question3[j]);
 if(num!=18)
 num=num+7;
%><option   value="<%=num%>"><%=action.getAnswer(num).getAnswer()%></option> 
<%}%>
</select><br/>
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