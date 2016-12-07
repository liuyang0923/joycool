<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.chess.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
ChessAction action = new ChessAction(request, response);
int id = action.getParameterInt("id");
ChessBean chess = action.getChessHistory(id);
if(chess==null){
	action.redirect("index.jsp");
	return;
}
PagingBean paging = new PagingBean(action, chess.getTurnCount(), 20, "p");
UserBean loginUser = action.getLoginUser();
int side = chess.getUserSide(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="棋局资料">
<p align="left"><%=BaseAction.getTop(request, response)%>
<%if(paging.getCurrentPageIndex()==0){
int ws=chess.getWinSide();
%>红方:<%if(chess.getUserId1()==0){%><a href="v.jsp?ps=1">加入</a><%}else {%><a href="u.jsp?id=<%=chess.getUserId1()%>"><%=UserInfoUtil.getUser(chess.getUserId1()).getNickNameWml() %></a>(<%if(ws==1){%>胜<%}else if(ws==2){%>负<%}else{%>平<%}%>)<%}%><br/>
黑方:<%if(chess.getUserId2()==0){%><a href="v.jsp?ps=2">加入</a><%}else {%><a href="u.jsp?id=<%=chess.getUserId2()%>"><%=UserInfoUtil.getUser(chess.getUserId2()).getNickNameWml() %></a>(<%if(ws==2){%>胜<%}else if(ws==1){%>负<%}else{%>平<%}%>)<%}%><br/>
开局:<%=DateUtil.sformatTime(chess.getStartTime())%><br/>
结束:<%=DateUtil.sformatTime(chess.getEndTime())%><br/>
共<%=chess.getTurnCount()%>回合,用时<%=DateUtil.formatTimeInterval(chess.getEndTime()-chess.getStartTime())%><br/>
<%}%>
<% int endIndex = paging.getEndIndex();
for(int i=paging.getStartIndex()+1;i<=endIndex;i++){
%><%=chess.getTurnString(i)%><br/>
<%}%>
<%=paging.shuzifenye("h2.jsp?id="+id,true,"|",response)%>
<a href="index.jsp">返回象棋大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>