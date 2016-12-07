<%@ page pageEncoding="utf-8"%><%
BoxGameBean vsGame =(BoxGameBean)action.getVsGame();
if(vsGame==null){
	response.sendRedirect("/fm/index.jsp");
	return;
}
BoxUserBean vsUser=(BoxUserBean)action.getVsUser();
%>