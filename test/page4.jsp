<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.test.*"%><%
response.setHeader("Cache-Control","no-cache");

String questionName="question";
String info=null;
boolean validate=true;
int[] questionIds=new int[]{14,15,16,17};
int answer11=Integer.parseInt((String)request.getSession().getAttribute(questionName+11));
int answer12=Integer.parseInt((String)request.getSession().getAttribute(questionName+12));
if(answer11==52 && answer12==57){
	questionIds=new int[]{13};
}
for(int i=13;i<18;i++){
	//System.out.println(questionName+i);
if(session.getAttribute(questionName+i)!=null)
	session.removeAttribute(questionName+i);
}
if(request.getParameter("answer")!=null){
	if(request.getParameter("question14")!=null){
		String answer14=((String)request.getParameter("question14"));
		if(answer14.indexOf(";")>0){
			String[] answers=answer14.split(";");
			if(answers.length>3){
				info="第14题限选3项";
				validate=false;
			}
		}
	}
	if(validate==true){
		for( int i=0;i<questionIds.length;i++){
			//System.out.println(questionName+questionIds[i]+"==="+request.getParameter(questionName+questionIds[i]));
			if(request.getParameter(questionName+questionIds[i]).equals(""))
			{
				validate=false;
				info="请选择第"+questionIds[i]+"题答案";
				break;
			}
			request.getSession().setAttribute(questionName+questionIds[i],
					request.getParameter(questionName+questionIds[i]));
		}
		//request.getSession().setAttribute("curQuestionPage","4");
%><%if(validate==true){%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="main" title="载入中...">
<onevent type="ontimer">
<go href="page5.jsp"/> 
</onevent>
<timer value="1"/>
<p mode="nowrap" align="left">载入中...</p>
</card>
</wml>
<%return;}%>
<%
		
	}
}
TestAction action=new TestAction(request);
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
第<%=question.getQuestionId() %>题 <%=question.getQuestion() %><br/>
<select <% if(question.getMultiple()==1){%> multiple="true" ivalue="0" <%}%> name="<%=questionName+questionIds[i]%>"
<% if(request.getParameter(questionName+questionIds[i])!=null){
	
	String ivalue="0";
	String temp=null;
	temp=request.getParameter(questionName+questionIds[i]);
	String []tempStr=null;
	if(!temp.equals("")){
		if(temp.indexOf(";")>0)
		{
			/*
			tempStr=temp.split(";");
			for(int k=0;k<tempStr.length;k++){
				for(int j=0;j<vecAnswerList.size();j++)
				{
					answer=(AnswerBean)vecAnswerList.get(j);
					if(answer.getId()==Integer.parseInt(tempStr[k])){
						ivalue=(j+1)+";";
					}
				}
			}
			*///多选
			if(ivalue.endsWith(";"))
				ivalue=ivalue.substring(0,ivalue.length()-1);	
		}else{
			//单选
			for(int j=0;j<vecAnswerList.size();j++)
			{
				answer=(AnswerBean)vecAnswerList.get(j);
				if(answer.getId()==Integer.parseInt(temp)){
					ivalue=(j+1)+"";
			}
		  }
		}
	}
	%>
<% if(question.getMultiple()==0){%>
ivalue="<%=ivalue%>"<%}}%> > 
<% if(question.getMultiple()==0){%><option value="">请选择答案</option><%}%>
<%
 for(int j=0;j<vecAnswerList.size();j++){
  answer=(AnswerBean)vecAnswerList.get(j);
%><option  value="<%=answer.getId()%>"><%=answer.getAnswer()%></option> <%	 
if(j==vecAnswerList.size()-1){%></select><br/><%}}}%>
<anchor>下一页
    <go href="page4.jsp?answer=yes" method="post">
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