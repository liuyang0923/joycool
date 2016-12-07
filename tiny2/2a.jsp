<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
Tiny2Game2 tg = (Tiny2Game2)game;
int lock = 0;
if(game.isStatusPlay()) {
	int option = action.getParameterInt("o");	// 调用方id
	lock = tg.lockTime();
	if(lock <= 0){
		tg.answer(option);
	}
}
if(tg.isLastWin()){
	response.sendRedirect(action.getReturnURL());
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(tg.isLastWin()){%>
<card title="小游戏" ontimer="<%=response.encodeURL(action.getReturnURLWml())%>">
<timer value="10"/>
<%}else{%>
<card title="小游戏">
<%}%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(lock > 0){%>
错误次数过多，请稍后再试!<br/>
<a href="2q.jsp">返回</a><br/>
<%}else if(tg.isLastWin()){%>
恭喜答对了!<br/>
<a href="<%=(action.getReturnURLWml())%>">返回</a><br/>
<%}else{%>
很遗憾，回答错误!<br/>
<a href="2q.jsp">继续</a><br/>
<%}%>
<%@include file="footer.jsp"%>
</p>
</card>
</wml>