<%@include file="/checkMobile.jsp"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.punch.*" %><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 300),
};
%><%
response.setHeader("Cache-Control","no-cache");
PunchAction action = new PunchAction(request);

	Tiny2Action action2 = new Tiny2Action(request, response);
	if(action2.checkGame()) return;
	if(action2.getGame() == null){
		if(net.joycool.wap.util.RandomUtil.nextInt(150) == 0){
			action2.startGame(games[0]);
			return;
		}
	}

action.punch();
int[] num = action.getPunchUser().getNum();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="打小强">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="view.jsp">继续打&gt;&gt;</a><br/>
你已经打死了<%=num[0]%>个小强和<%=num[1]%>个老鼠<br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>