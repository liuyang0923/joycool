<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.mental.*"%>
<% response.setHeader("Cache-Control","no-cache");
   MentalAction action = new MentalAction(request);
   MentalUser mentalUser = action.getCurrentUser();
   if (mentalUser != null && mentalUser.getQueNow() >=8){
	   action.sendRedirect("more.jsp",response);
	   return;
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友心理测试"><p>
<%=BaseAction.getTop(request, response)%>
想知道一个最真实的自己吗,想知道怎样的TA才是你命中真爱吗?测试过后,答案即将揭晓......<br/>
<a href="question.jsp"><%=mentalUser==null || mentalUser.getQueNow() == 0 ? "开始" : "继续" %>测试</a><br/>
请以第一直觉为准.答过的题目不可修改.<br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>