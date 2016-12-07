<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.chess.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");

ChessAction action = new ChessAction(request);
ChessAction.statPeople();
int cur = action.getParameterIntS("p");
int userCur = 0;
ChessUserBean chessUser = ChessAction.getChessUser(action.getLoginUser().getId());
if(cur == -1){
	if(chessUser!=null){
		userCur = ChessAction.service.getChessCurStat(chessUser.getUserId());
		//System.out.println(userCur);
		if(userCur > 0) {
			cur = (userCur % 10 == 0) ? (userCur / 10 - 1) : (userCur / 10);
			//System.out.println(cur);
		} else{
			cur = 0;
		}
	} else {
		cur = 0;
	}
}

int start = cur * 10;
int limit = 11;

//PagingBean paging = new PagingBean(action, total, 10, "p");
List list = ChessAction.service.getChessUserStat(start, limit);
int count = list.size() > 10 ? 10 : list.size();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="中国象棋">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(userCur > 0) {%>
您当前的排名为:第<%=userCur %>名<br/>
<%} %>==用户/积分==<br/>
<%for(int i=0;i<count;i++){
	Object[] obj = (Object[])list.get(i);
%>
<%=obj[0]%>.<a href="u.jsp?id=<%=obj[1]%>"><%=UserInfoUtil.getUser(((Integer)obj[1]).intValue()).getNickNameWml() %></a>|<%=obj[2] 
%><br/><%}
%>
<%if(list.size() > 10) {%><a href="stat.jsp?p=<%=cur+1%>">下一页</a><%}else{%>下一页<%}%>
<%if(cur > 0) {%><a href="stat.jsp?p=<%=cur-1%>">上一页</a><%}else{%>上一页<%}%><br/>
跳转到:<input name="p" maxlength="3"/>
<anchor>GO<go href="stat.jsp" method="post"><postfield name="p" value="$p"/></go></anchor><br/>
<br/>
<a href="a.jsp">查看历史棋局</a><br/>
<a href="index.jsp">返回象棋大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>