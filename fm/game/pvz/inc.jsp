<%@ page pageEncoding="utf-8"%><%
PVZGameBean vsGame =(PVZGameBean)action.getVsGame();
if(vsGame==null){
	response.sendRedirect("/fm/game/vs/vsgame.jsp");
	return;
}
PVZUserBean vsUser=(PVZUserBean)action.getVsUser();
//boolean isPanlt=true;
//if(vsUser.isZombie()){
//isPanlt=false;
//}
%>