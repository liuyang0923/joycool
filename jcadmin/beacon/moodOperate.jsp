<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=20; 
	public static MoodService service =new MoodService();
%>
<html>
	<%
		MoodAction action=new MoodAction(request);
		String f=action.getParameterString("f");
		int id=action.getParameterInt("id");
		int uid=action.getParameterInt("uid");
		//删除心情
		if ("mood".equals(f)){
			boolean result=service.deleteMood(id,uid);
			if (result){
				action.innerRedirect("mood.jsp",response);
			}else{
				%><font color=red>删除失败</font><br/><a href="mood.jsp">返回</a><%
			}
		}
		//删除最新心情
		if ("lastMood".equals(f)){
			boolean result=service.deleteMood(uid);
			if (result){
				action.innerRedirect("moodLast.jsp",response);
			}else{
				%><font color=red>删除失败</font><br/><a href="mood.jsp">返回</a><%
			}
		}		
		//删除留言
		if ("reply".equals(f) || "allReply".equals(f)){
			int moodId=service.getReplyById(id).getMoodId();
			boolean result=service.deleteReply(id);
			//将留言次数减1
			result=service.decreaseReplyCount(moodId);
			if (result){
				if ("allReply".equals(f)){
					response.sendRedirect("moodAllReply.jsp?");
				}else{
					response.sendRedirect("moodReply.jsp?id=" + moodId);
				}
			}else{
				if ("allReply".equals(f)){
					%><font color=red>删除失败</font><br/><a href="moodAllReply.jsp">返回</a><%
				}else{
					%><font color=red>删除失败</font><br/><a href="mood.jsp">返回</a><%
				}
			}
		}
	%>
</html>