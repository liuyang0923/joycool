<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session="false"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.util.*"%><%!
static TinyGame[] games = {
new TinyGame1(3,3),
new TinyGame1(3,3,3),
new TinyGame2(3,3),
new TinyGame2(4,4),
new TinyGame3(3,8),
new TinyGame3(4),
new TinyGame4(6,4,4),
new TinyGame4(8,6,2),
new TinyGame5(2,4,8),
new TinyGame5(3,6,24),
new TinyGame6(3,4),
new TinyGame6(4,8),
new TinyGame6(5,12),
};
%><%
response.setHeader("Cache-Control","no-cache");
TinyAction action = new TinyAction(request, response);
action.index();
int id = action.getAttributeInt("id");
if(action.isResult("redirect")){
return;
}
if(id > 0 && id < 999) {
	action.chooseGame(games[id-1].copy());
	return;
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
TinyGame g = games[i];%>
<%=i+1%><a href="index.jsp?g=<%=(i+1)%>"><%=g.getName()%></a><br/>
<%}%>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>