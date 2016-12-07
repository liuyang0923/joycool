<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*" %><%
response.setHeader("Cache-Control","no-cache");
GuessAction action = new GuessAction(request);
action.doGuess();
if(!action.getGuessUser().isGameOver()){
action.sendRedirect("guess.jsp", response);
return;
}
String url=("guess.jsp");
String[] winTip = {
"你真是太有才了！",
"你也太厉害了吧，偶像！",
"21世纪最缺的人才呀！",
"恭喜你发财了发财了！",
"你好聪明呀！",
"神猜手呀！",
"中奖见面分一半噢！",
"恭喜你答对了！",
};
int turn = action.getGuessUser().getTurn();
if(turn > 7)
	turn = 7;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=GuessAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.getGuessUser().isWin()){%>
第<%=action.getGuessUser().getTurn() + 1%>次就猜对啦！<br/>
<%=winTip[turn]%><br/>
<%}else{%>
游戏结束<br/>
很遗憾，你没有在规定的次数内完成！<br/>
<%}%>
<%@include file="info.jsp"%>
<a href="reset.jsp?reset=">重新玩猜数字</a><br/>
<a href="<%=( "/lswjs/gameIndex.jsp")%>">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>