<%@ page pageEncoding="utf-8"%><%
EmperorGameBean vsGame =(EmperorGameBean)action.getVsGame();
if(vsGame==null){
	response.sendRedirect("/fm/index.jsp");
	return;
}
EmperorUserBean vsUser=(EmperorUserBean)action.getVsUser();
%>