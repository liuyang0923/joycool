<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
Tiny2Game1 tg = null;
if(game.isStatusPlay()) {
	tg = (Tiny2Game1)game;
	int option = action.getParameterInt("o");	// 调用方id
	
	tg.answer(option);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="小游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tg.isLastWin()){%>
恭喜答对了!<br/>
<%}else{%>
很遗憾，回答错误!<br/>
<%}%>
<%if(action.isGameOver()){%>
<a href="<%=(action.getReturnURLWml())%>">游戏完成</a><br/>
<%}else{%>
还需要答对<%=tg.getCount()-tg.getCorrect()%>题<br/>
<a href="1q.jsp">继续</a><br/>
<br/>
<a href="giveup.jsp">放弃</a><br/>
<%}%>
<%@include file="footer.jsp"%>
</p>
</card>
</wml>