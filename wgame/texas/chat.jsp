<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.Date"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="net.joycool.wap.util.UserInfoUtil,java.util.List"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.game.texas.*"%><%!
static int NUMBER_OF_PAGE = 10;%><%
response.setHeader("Cache-Control","no-cache");
TexasAction action = new TexasAction(request);
UserBean loginUser = action.getLoginUser();
TexasGame game = (TexasGame)session.getAttribute("texas");
if(game==null){
	response.sendRedirect("index.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=game.getBoardId()+1%>号桌">
<p align="left">
<%=BaseAction.getTop(request, response)%><%
if(loginUser != null){
SimpleChatLog sc = SimpleChatLog.getChatLog("tx" + game.getBoardId());
PagingBean paging = new PagingBean(action, sc.size(),NUMBER_OF_PAGE,"p");
String content = action.getParameterNoEnter("content");
if(content != null && content.length()!=0 && action.isCooldown("teamchat", 5000) && !ForbidUtil.isForbid("chat",loginUser.getId())) {		// 发言
	sc.add(loginUser.getNickNameWml() + ":" + StringUtil.toWml(content) + "(" + DateUtil.formatTime(new Date()) + ")");
}
%><input name="cchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="chat.jsp" method="post">
    <postfield name="content" value="$cchat"/>
</go></anchor>|
<a href="chat.jsp">刷新</a>|<a href="play.jsp">返回游戏</a><br/>
<%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("chat.jsp", false, "|", response)%>
<%}%>
<a href="play.jsp">返回游戏</a><br/>
<a href="index.jsp">返回德州扑克大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>