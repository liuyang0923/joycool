<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session="false"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.util.*"%><%!
static Tiny2Game[] games = {
new Tiny2Game1(3,3),
new Tiny2Game2(20, 60),
};
%><%
response.setHeader("Cache-Control","no-cache");
Tiny2Action action = new Tiny2Action(request, response);
if(action.checkGame(0)) return;
Tiny2Game game = action.getGame();
if(game==null){
	int id = action.getParameterInt("g");
	if(id > 0 && id < 999) {
		action.startGame(games[id-1], 0);
		return;
	}
}else{
	action.tip("tip", "游戏结束，结果为" + game.getResult());
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="小游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
1.<a href="index.jsp?g=999">返回</a><br/>
<%}else{%>
<%for(int i=0;i<games.length;i++){
Tiny2Game g = games[i];%>
<%=i+1%><a href="index.jsp?g=<%=(i+1)%>"><%=g.getName()%></a><br/>
<%}%>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>