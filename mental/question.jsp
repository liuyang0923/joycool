<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.mental.*"%>
<% response.setHeader("Cache-Control","no-cache");
   MentalAction action = new MentalAction(request);
   int answerId = action.getParameterInt("a");
   int questionId = action.getParameterInt("qid");
   MentalQuestion question = null;
   String tip ="";
   if (action.getLoginUser() == null){
	   tip = "请先登陆后再测试.";
   } else {
	   if (answerId > 0){
		   // 答题
		   question = action.writeUserAnswer(answerId,questionId);
		   if (question != null){
			   response.sendRedirect("question.jsp");
			   return;
		   }
	   } else {
		   question = action.getCurrentQuestion("INTERVAL 1 MONTH");	//输入2次测试间隔时间(第一次进入本页面时调用)
	   }
	   if (question == null){
	  		tip = (String)request.getAttribute("tip");
	   		if ("over".equals(tip)){
	   			// 答完了所有的题
	   			action.sendRedirect("more.jsp",response);
	   			return;	
	   		}
	   } else {
		   questionId = question.getQueId();
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友心理测试"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=question.getQueId()%>.<%=StringUtil.toWml(question.getQuestion())%><br/>
<%String tmp[];
tmp = question.getAnswer().split("\r\n");
if (question.getQueId() == 1){
	for (int i = 0; i < tmp.length ; i++){
		%><a href="question.jsp?a=<%=i+1%>&amp;qid=<%=questionId%>"><%=tmp[i] %></a>&#160;<%if((i+1) % 4 == 0){%><br/><%}%><%
	}
} else {
	for (int i = 0; i < tmp.length ; i++){
		%><a href="question.jsp?a=<%=i+1%>&amp;qid=<%=questionId%>"><%=tmp[i] %></a><br/><%
	}
}
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>