<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.servlet.*"%><%@ page import="net.joycool.wap.framework.*"%><%
if(request.getParameter("clear") != null){
	WGameThread.lotteryFlag = true;
	//response.sendRedirect("lottery.jsp");
	BaseAction.sendRedirect("/jcadmin/lottery.jsp", response);
}
%>
<p><a href="lottery.jsp?clear=1">开奖</a></p>