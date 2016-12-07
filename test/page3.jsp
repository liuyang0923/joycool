<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.test.*"%><%
response.setHeader("Cache-Control","no-cache");

String questionName="question";
String info=null;
boolean validate=true;
int[] questionIds=new int[]{11,12};
if(request.getParameter("answer")!=null){
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
	//request.getSession().setAttribute("curQuestionPage","3");
%><%if(validate==true){%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="main" title="载入中...">
<onevent type="ontimer">
<go href="page4.jsp"/> 
</onevent>
<timer value="1"/>
<p mode="nowrap" align="left">载入中...</p>
</card>
</wml>
<%return;}%>
<%
	
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
假设一下，在wap上给每个人搭建一个“小店”，里面可以发布想买/卖的物品或服务的信息，并展示其详细特征。
你可以方便、安全的管理自己店中的物品，并跟留过言的网友进行交流。更可以轻松的在别人的“小店”里查找、浏览感兴趣的东东，留言或直接跟店主沟通。
<br/>
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
ivalue="<%=ivalue%>"<%}%> > 
<% if(question.getMultiple()==0){%><option value="">请选择答案</option><%}%>
<%
 for(int j=0;j<vecAnswerList.size();j++){
  answer=(AnswerBean)vecAnswerList.get(j);
%><option  value="<%=answer.getId()%>"><%=answer.getAnswer()%></option> <%	 
if(j==vecAnswerList.size()-1){%></select><br/><%}}}%>
<anchor>下一页
    <go href="page3.jsp?answer=yes" method="post">
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