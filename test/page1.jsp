<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.test.*"%><%
response.setHeader("Cache-Control","no-cache");
int[] questionIds=new int[]{1,2,3,4};
String questionName="question";

TestAction action=new TestAction(request);
boolean validate=true;
String info=null;
if(request.getParameter("answer")==null)
{
	if(action.isTestFinished()==true){
		//response.sendRedirect(("infoTested.jsp?isFinished=1"));
		BaseAction.sendRedirect("/test/infoTested.jsp?isFinished=1", response);
		return;
	}
	if(action.isTested()==true){
		//response.sendRedirect(("infoTested.jsp"));
		BaseAction.sendRedirect("/test/infoTested.jsp", response);
		return;
	}
}

else{

	for( int i=0;i<questionIds.length;i++){
		//System.out.println("==="+questionName+questionIds[i]);
		//System.out.println(questionName+questionIds[i]+"==="+request.getParameter(questionName+questionIds[i]));
		if(request.getParameter(questionName+questionIds[i]).equals(""))
		{
			validate=false;
			info="请选择第"+questionIds[i]+"题答案";
			break;
		}
		
		
		
		request.getSession().setAttribute(questionName+questionIds[i],request.getParameter(questionName+questionIds[i]));
	}
	
	//request.getSession().setAttribute("curQuestionPage","1");
%>
<%if(validate==true){%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="main" title="载入中...">
<onevent type="ontimer">
<go href="page2.jsp"/> 
</onevent>
<timer value="1"/>
<p mode="nowrap" align="left">载入中...</p>
</card>
</wml>
<%return;}%>
<%
	//response.sendRedirect(("page2.jsp"));

	
}
/*
if(request.getSession().getAttribute("curQuestionPage")!=null){
	int curQuestionPage=Integer.parseInt((String)request.getSession().getAttribute("curQuestionPage"));
	if(curQuestionPage==6)
		//response.sendRedirect(("info.jsp"));
	    BaseAction.sendRedirect("/test/info.jsp", response);
	else
	{
		curQuestionPage++;
		String url="page"+curQuestionPage+".jsp";
		//response.sendRedirect((url));
		BaseAction.sendRedirect("/test/page"+curQuestionPage+".jsp", response);
		return;
	}
}
*/


Vector vec=null;
Vector vecAnswerList=null;
QuestionBean question=null;
AnswerBean answer=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="个人用户问卷">
<p align="left">
乐酷提醒：所有问卷都要通过逻辑筛选，如果您胡乱填写，就不能获得经验值哦...多谢您的支持！<br/>
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
    <go href="page1.jsp?answer=yes" method="post">
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