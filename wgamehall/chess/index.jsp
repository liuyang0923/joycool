<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.chess.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
ChessAction action = new ChessAction(request);
int point = action.getParameterInt("point");

action.index();
UserBean loginUser = action.getLoginUser();
ChessUserBean chessUser = null;
if(loginUser != null)
	chessUser = ChessAction.getChessUser(loginUser.getId());

if(point == 1) {
	if(chessUser == null || chessUser.getPoint() < 1800){
		point = 0;
		action.tip("tip", "积分不足1800无法进入高级房间");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="中国象棋" ontimer="<%=response.encodeURL("index.jsp?point="+point)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="help.jsp">观棋不语真君子,落子无悔大丈夫</a><br/>
<a href="/jcforum/forum.jsp?forumId=12109">+象棋爱好者论坛+</a><br/>
<%if(action.isResult("tip")){%><%=action.getTip() %><br/><%} %>
<%if(chessUser!=null){ 
int cur = ChessAction.getChessUserCurrent(chessUser);
if(cur!=0){
%><a href="v.jsp?b=<%=cur%>">我的当前棋局</a><br/>
<%}}%>
<%if(point==0) {%>
=普通.<a href="index.jsp?point=1">高级</a>=<br/>
<%}else { %>
=<a href="index.jsp">普通</a>.高级=<br/>
<%}
Object[] boards = (Object[])action.boards.values().toArray();
for(int i=0;i<boards.length;i++) {
ChessBean bean = (ChessBean)boards[i];
if(bean.getPoint()==0 && point == 0) {
%><a href="v.jsp?b=<%=bean.getId()%>"><%=i+1%>号桌</a>(<%=bean.getStatusString()%>)<br/>
<%if(bean.getUserId1()==0){%>(空位)<%}else{%><%=UserInfoUtil.getUser(bean.getUserId1()).getNickNameWml()%><%}%> VS <%if(bean.getUserId2()==0){%>(空位)<%}else{%><%=UserInfoUtil.getUser(bean.getUserId2()).getNickNameWml()%><%}%><br/>
<%} else if(point != 0 && bean.getPoint() > 0 ){%>
<a href="v.jsp?b=<%=bean.getId()%>"><%=i+1%>号桌</a>(<%=bean.getStatusString()%>)[积分<%=bean.getPoint() %>]<br/>
<%if(bean.getUserId1()==0){%>(空位)<%}else{%><%=UserInfoUtil.getUser(bean.getUserId1()).getNickNameWml()%><%}%> VS <%if(bean.getUserId2()==0){%>(空位)<%}else{%><%=UserInfoUtil.getUser(bean.getUserId2()).getNickNameWml()%><%}%><br/>
<%}}%>
<br/>
<a href="stat.jsp">查看积分排行</a><br/>
<a href="a.jsp">查看历史棋局</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>