<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.chess.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
ChessAction action = new ChessAction(request, response);
ChessBean chess = action.getCurrentChess();
UserBean loginUser = action.getLoginUser();
if(chess==null){
	action.redirect("index.jsp");
	return;
}
int operate = action.getParameterInt("o");
if(operate == 2) {		//重新开始，结束棋局后需要等待30秒才能重新开
	if(System.currentTimeMillis() - chess.getEndTime() > 40000 && chess.isStatusEnd()){
		chess.reset();
		SqlUtil.executeUpdate("update mcoolgame.chess set win_side=0,status=0,user_id1=0,user_id2=0,history='' where id=" + chess.getId(), 4);
	}
	action.redirect("v.jsp");
	return;
}
if (operate == 5) {
	// 下棋双方都超时30分钟，则判断为和棋，并且第三方可以把他们从棋局中踢走
	if(System.currentTimeMillis() - chess.getLastMoveTime() > 30 * 60000){
		chess.end(3);
		chess.reset();
	}
	action.redirect("v.jsp");
	return;
}

int side = chess.getUserSide(loginUser.getId());
if(side == -1 || !chess.isStatusPlay()) {
	action.redirect("v.jsp");
	return;
}
if(operate == 1) {		// 认输
	chess.end(-side);
	action.redirect("v.jsp");
	return;
}
if(operate == 4) {		// 求和
	chess.setDrawSide(side);
	action.redirect("v.jsp");
	return;
}
if(operate == 3) {		//胜利
	if(chess.getTimeLeft() == 0) {
		chess.end(side);
	}
	action.redirect("v.jsp");
	return;
}
if(side != chess.getTurnSide()) {
	action.redirect("v.jsp");
	return;
}
action.go();
if(true) {
	action.redirect("v.jsp");
	return;
}
int pos = action.getParameterIntS("p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="象棋">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(pos==-1){%>
<%=chess.getBoardString(loginUser.getId(),response)%>
<%}else{%>
<%=chess.getBoardString2(loginUser.getId(),pos,response)%>
<%}%><br/>
<%//if(side == -1){%>
<%if(chess.getUserId1()==0){%><a href="v.jsp?ps=1">加入红方</a><%}else {%>加入红方<%}%>|
<%if(chess.getUserId2()==0){%><a href="v.jsp?ps=2">加入黑方</a><%}else {%>加入黑方<%}%>
<%//}%>

<br/>
<a href="index.jsp">返回象棋大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>