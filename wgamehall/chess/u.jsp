<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.text.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.chess.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%!
static SimpleDateFormat sdf2 = new SimpleDateFormat("M-d/H:mm");
%><%
response.setHeader("Cache-Control","no-cache");
ChessAction action = new ChessAction(request, response);
UserBean loginUser = action.getLoginUser();
int userId = action.getParameterInt("id");
ChessUserBean chessUser = ChessAction.getChessUser(userId);
UserBean user = UserInfoUtil.getUser(userId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="棋手资料">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(user==null){%>
数据未找到<br/>
<%}else{%>
<a href="/chat/post.jsp?toUserId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>(<%=user.getId()%>)<br/>
积分<%=chessUser.getPoint()%><br/>
<%=chessUser.getWin()%>胜 <%=chessUser.getLose()%>负 <%=chessUser.getDraw()%>平 <%=chessUser.getFlee()%>逃跑<br/>
<%}%>
<a href="a.jsp?u=<%=userId%>">查看历史棋局</a><br/>
<br/>
<a href="index.jsp">返回象棋大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>