<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.text.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.chess.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%!
static SimpleDateFormat sdf2 = new SimpleDateFormat("M-d/H:mm");
%><%
response.setHeader("Cache-Control","no-cache");
ChessAction action = new ChessAction(request, response);
List list;
int totalCount = 0;
int userId = action.getParameterInt("u");
if(userId > 0) {
	totalCount = SqlUtil.getIntResult("select count(id) from mcoolgame.chess_his where user_id1="+userId,4);
} else {
	totalCount = SqlUtil.getIntResult("select count(id) from mcoolgame.chess_his",4);
}

PagingBean paging = new PagingBean(action,totalCount,10,"p");
if(userId > 0) {
	list = action.service.getUserHistories(userId, paging.getStartIndex(), paging.getCountPerPage());
} else {
	list = action.service.getAllHistories(paging.getStartIndex(), paging.getCountPerPage());
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="中国象棋">
<p align="left">
<%=BaseAction.getTop(request, response)%>
==历史记录==<br/>
<%
for(int i = 0;i < list.size(); i ++) {
	ChessBean chess = (ChessBean)list.get(i);		
%>
<%=i + 1%>.<a href="h2.jsp?id=<%=chess.getId()%>"><%=(UserInfoUtil.getUser(chess.getUserId1())).getNickNameWml()%> VS <%=(UserInfoUtil.getUser(chess.getUserId2())).getNickNameWml()%></a><br/>
<%=DateUtil.sformatTime(chess.getStartTime())%>(共<%=chess.getTurnCount()%>回合)<br/>
<%}%>
<%=paging.shuzifenye("a.jsp?u=" + userId,true,"|",response)%>
<br/>
<a href="index.jsp">返回象棋大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>