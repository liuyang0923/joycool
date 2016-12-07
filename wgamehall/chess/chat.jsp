<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean,net.joycool.wap.spec.chess.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%!
static int NUMBER_OF_PAGE = 10;%><%
response.setHeader("Cache-Control","no-cache");
ChessAction action = new ChessAction(request);
UserBean loginUser = action.getLoginUser();
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="棋局评论">
<p align="left">
<%=BaseAction.getTop(request, response)%><%
if(loginUser != null){
SimpleChatLog sc = SimpleChatLog.getChatLog("c" + session.getAttribute("chessId"));
PagingBean paging = new PagingBean(action, sc.size(),NUMBER_OF_PAGE,"p");
String content = action.getParameterNoEnter("content");
if(content != null && content.length()!=0 && action.isCooldown("teamchat", 5000) && !ForbidUtil.isForbid("chat",loginUser.getId())) {		// 发言
	sc.add(loginUser.getNickNameWml() + ":" + StringUtil.toWml(content) + "(" + DateUtil.formatTime(new Date()) + ")");
}
%><input name="cchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="<%=url%>" method="post">
    <postfield name="content" value="$cchat"/>
</go></anchor>|
<a href="<%=url%>">刷新</a>|<a href="v.jsp">返回游戏</a><br/>
<%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("chat.jsp", false, "|", response)%>
<%}%>
<a href="v.jsp">返回游戏</a><br/>
<a href="index.jsp">返回象棋大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>