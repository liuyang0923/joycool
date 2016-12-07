<%@ page pageEncoding="utf-8"%><%
FruitGameBean vsGame =(FruitGameBean)action.getVsGame();
if(vsGame==null){
	response.sendRedirect("/fm/game/vs/vsgame.jsp");
	return;
}
FruitUserBean vsUser=(FruitUserBean)action.getVsUser();
%>