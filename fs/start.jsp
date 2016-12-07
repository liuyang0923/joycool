<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.FSAction" %><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 300),
};
%><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
net.joycool.wap.bean.UserBean loginUser = action.getLoginUser();
if(loginUser!=null&&net.joycool.wap.util.ForbidUtil.isForbid("game",loginUser.getId())){
	response.sendRedirect("/enter/not.jsp");
	return;
}
if(request.getParameter("type")!=null){
	Tiny2Action action2 = new Tiny2Action(request, response);
	if(action2.checkGame(9)) return;
	if(action2.getGame() == null){
		action2.startGame(games[0], 9);
		return;
	}
}
action.start();
if(action.isResult("redirect")) {
int[] count = net.joycool.wap.util.CountMaps.countMap3.getCount(action.getLoginUser().getId());
count[0]++;
BaseAction.sendRedirect("/fs/index.jsp",response);
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FSAction.title%>" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
切换游戏模式会丢失之前的游戏进度！<br/>
<a href="start.jsp?type=0">标准模式(40天)</a><br/>
<a href="start.jsp?type=1">短篇模式(30天)</a><br/>
<a href="start.jsp?type=2">超短篇模式(25天)</a><br/>
<a href="/fs/help.jsp">返回浮生记首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>