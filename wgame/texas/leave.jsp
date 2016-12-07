<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="net.joycool.wap.util.UserInfoUtil,java.util.List"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%
response.setHeader("Cache-Control","no-cache");
TexasAction action = new TexasAction(request);

TexasGame game = (TexasGame)session.getAttribute("texas");
if(game == null||action.getParameterInt("leave")==2) {
	response.sendRedirect("index.jsp");
	return;
}
int userId = action.getLoginUser().getId();
if(game.leave(userId, action.hasParam("c"))) {
	TexasUserBean tub =	TexasGame.getUserBean(userId);
	tub.setBoardId(-1);
	if(action.hasParam("a")) {
		response.sendRedirect("index.jsp");
	} else {
		response.sendRedirect("play.jsp");
	}
	return;
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=game.getBoardId() + 1%>号<%=game.getGameTypeName()%>桌">
<p align="left">
本局扑克还在进行中,站起相当于放弃本局和本局内下的所有赌注<br/>
<a href="leave.jsp?c=1<%if(action.hasParam("a")){%>&amp;a=1<%}%>">确定放弃并站起</a><br/>
<br/>
<a href="play.jsp">返回游戏</a><br/>
<a href="index.jsp">返回德州扑克大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>

</card>
</wml>