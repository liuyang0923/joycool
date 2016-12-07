<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.team.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=20; 
	public static QuestionService service =new QuestionService();
%>
<html>
	<%	QuestionAction action =new QuestionAction(request);
		String type=action.getParameterString("t");
		int id=action.getParameterInt("id");
		int setId=action.getParameterInt("setId");
		//删除一套题
		if ("del".equals(type)){
			boolean result=SqlUtil.executeUpdate("delete from question_set where id= " + id,3);
				    result=SqlUtil.executeUpdate("delete from question where set_id= " + id,3);
			if (result){
				action.innerRedirect("question.jsp",response);
			}else{
				%><font color=red>删除失败</font><br/><a href="question.jsp">返回</a><%
			}
		}
		//删除其中一题
		if ("dellist".equals(type)){
			boolean result=SqlUtil.executeUpdate("delete from question where id= " + id,3);
			if (result){
				action.innerRedirect("quesList.jsp?id=" + setId ,response);
			}else{
				%><font color=red>删除失败</font><br/><a href="question.jsp">返回</a><%
			}
		}		
	%>
</html>