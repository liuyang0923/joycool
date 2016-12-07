<%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%
if(action.getRichUser().getWorldId()<0){
	response.sendRedirect(("index.jsp"));
	return;
}%>