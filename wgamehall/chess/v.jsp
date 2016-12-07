<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.chess.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
ChessAction action = new ChessAction(request, response);
ChessUserBean chessUser = action.getUser();
int boardId = action.getParameterInt("b");

ChessBean chesss = (ChessBean)ChessAction.boards.get(new Integer(boardId));
if(chesss == null) {
	chesss = action.getCurrentChess();
}
boolean flag = chessUser.getPoint() >= chesss.getPoint() || chesss.getPoint()== 0;
if(flag) {
action.view();
ChessBean chess = action.getCurrentChess();
if(chess==null){
	action.redirect("index.jsp");
	return;
}
int pos = action.getParameterIntS("p");
UserBean loginUser = action.getLoginUser();
int side = chess.getUserSide(loginUser.getId());
SimpleChatLog sc = SimpleChatLog.getChatLog("c"+chess.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(loginUser.getId()==chess.getTurnUser()){%><card id="c" title="中国象棋"><%}else{%><card id="c" title="中国象棋" ontimer="<%=response.encodeURL("v.jsp")%>"><timer value="200"/><%}%>
<p align="left"><%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
<%
int userSide = chess.getUserSide(loginUser.getId());
if(!chess.isStatusReady()){	%>
<%if(chess.isStatusEnd()) {

%>棋局结束,<%if(chess.getWinSide()==3){%>双方握手言和<%}else{%><%if(userSide == -1){%><%=UserInfoUtil.getUser(chess.getWinUser()).getNickNameWml()%>获胜<%}else if(userSide == chess.getWinSide()){%>恭喜获胜<%}else{%>很遗憾输了<%}%><%}%><br/><%

long interval = System.currentTimeMillis() - chess.getEndTime();

if(interval < ChessAction.END_WAIT){ %>
新开棋局(等待<%=DateUtil.formatTimeInterval(ChessAction.END_WAIT - interval)%>)
<%}else{%><a href="g.jsp?o=2">新开棋局</a>
<%}%><br/>
<%} else {
	long newGameInterval = System.currentTimeMillis() - chess.getLastMoveTime();
	if(newGameInterval >  30 * 60000) {
%><a href="g.jsp?o=5">新开棋局</a>(下棋双方超时)<br/>
<%}}%>
<%if(chess.getDrawSide()!=0&&userSide!=-1){
	if(userSide!=chess.getDrawSide()){
%>对手请求和棋-<a href="v.jsp?draw=1">同意</a>|<a href="v.jsp?draw=0">拒绝</a><br/><%
}else{
%>正在向对方请求和棋<br/><%
}}%>
<%if(chess.isBind) {%>!!!<%if(chess.getTurnSide()==1){%>红方<%}else{%>黑方<%}%>被将军!<br/><%}%>
<%if(chess.getMoveCount()>0){%><%=chess.getTurnString(chess.getTurnCount())%><br/><%}else{%><a href="h.jsp">查看本局资料</a><br/><%}%>
<%	if(pos==-1){
%><%=chess.getBoardString(loginUser.getId(),response)%><%
	}else{
%><%=chess.getBoardString2(loginUser.getId(),pos,response)%><%
	}%>
<%}else{%>
<%if(chess.getUserId1()==0){%>红方:<a href="v.jsp?ps=1">加入</a><%}else {%>红方:<a href="u.jsp?id=<%=chess.getUserId1()%>"><%=UserInfoUtil.getUser(chess.getUserId1()).getNickNameWml() %></a><%=chess.getWinString(1)%><%}%><br/>
<%if(chess.getUserId2()==0){%>黑方:<a href="v.jsp?ps=2">加入</a><%}else {%>黑方:<a href="u.jsp?id=<%=chess.getUserId2()%>"><%=UserInfoUtil.getUser(chess.getUserId2()).getNickNameWml() %></a><%=chess.getWinString(2)%><%}%><br/>
<%if(userSide!=-1){%>--<a href="v.jsp?up=1">离开座位</a><br/><%}%>
<%}%>
<%if(sc.size()>0){%><%=sc.getChatString(0, 1)%><%}%>
<%if(side == -1){
%><input name="cchat"  maxlength="100"/>
<anchor title="确定">发表
<go href="chat.jsp">
    <postfield name="content" value="$cchat"/>
</go></anchor><br/>
<%}else{%>
<a href="chat.jsp">更多评论</a><br/>
<%}%>
<a href="v.jsp">刷新</a>
<%if(chess.getTurnSide()==side&&chess.isStatusPlay()){%>
|<a href="g.jsp?o=1">认输</a>|<a href="g.jsp?o=4">求和</a>
<%}else if(chess.isStatusPlay()&&side!=-1){
int timeLeft = chess.getTimeLeft();
if(timeLeft==0){
%>|<a href="g.jsp?o=3">获胜(对方超时)</a><%}else if(timeLeft < 240){%>
<%=timeLeft%>秒<%}%>
<%}%>
|<a href="h.jsp">查看历史</a><br/>
<a href="index.jsp">返回象棋大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>
<%} else {%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card id="c" title="中国象棋" ontimer="<%=response.encodeURL("index.jsp")%>"><timer value="50"/>
<p align="left"><%=BaseAction.getTop(request, response)%>
分数不够，不能进入高级房<br/>
<a href="h.jsp">查看历史</a><br/>
<a href="index.jsp">返回象棋大厅</a><br/>
</p></card></wml>
<%}%>