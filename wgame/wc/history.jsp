<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.game.worldcup.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%>
<%! static int COUNT_PRE_PAGE = 5; 
	static String betStr[] = {"胜","负","平"}; 
	static String resultStr[] = {"输了","赢了","尚无结果"};
%>
<% response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
UserBean loginUser = action.getLoginUser();
WcUser wcUser = null;
WcBet wcBet = null;
WcMatch wcMatch = null;
WcInfo wcInfo = WorldCupAction.getWcInfo();
String tip = "";
int count = 0;
List list = null;
PagingBean paging = null;
String call = "您";
int back = action.getParameterInt("b");
int uid = action.getParameterInt("uid");
if (uid <= 0){
	if (loginUser == null){
		tip = "要查看的用户不存在.";
	} else {
		wcUser = action.getUser(loginUser.getId());
	}
} else {
	wcUser = action.getUser(uid);
	if (loginUser == null || uid != loginUser.getId()){
		call = "TA";	
	}
}
if (wcUser != null){
	count = SqlUtil.getIntResult("select count(id) from wc_bet where user_id=" + wcUser.getUserId(),5);
	paging = new PagingBean(action,count,COUNT_PRE_PAGE,"p");
	list = WorldCupAction.service.getBetList(" user_id=" + wcUser.getUserId() + " order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
}
int odds = 0;
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="竞猜"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%><%=call%>此前的投注历史如下:<br/>
<%if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		wcBet = (WcBet)list.get(i);
		if (wcBet != null){
			wcMatch = WorldCupAction.service.getMatch(" id=" + wcBet.getMatchId());
				if (wcMatch != null){
				%><%=i+paging.getStartIndex()+1 %>.<%=wcMatch.getTeamWml1() + betStr[wcBet.getFlag()]%><%=wcMatch.getTeamWml2()%>,<%if (wcBet.getResult()==1){%>赔率<%=wcBet.getOddsf()%>,<%}%><%=wcBet.getBet()%>彩币,<%=resultStr[wcBet.getResult()]%>,连本带利得<%=wcBet.getPoint()%>彩币.<br/><%	
				}
			}
	}%><%=paging.shuzifenye("history.jsp?uid=" + wcUser.getUserId() + "&amp;b=" + back, true, "|", response)%><%
} else {
%>暂无记录.<br/><%
}
} else {
%><%=tip%><br/><%	
}
if (back == 1){
%>[<a href="rank.jsp">返回排行榜</a>]<br/><%
}
%>
[<a href="index.jsp">返回投注首页</a>]<br/>
<%if (wcInfo != null && wcInfo.getSubjectId() > 0){
%>[<a href="/Column.do?columnId=<%=wcInfo.getSubjectId()%>">返回竞猜首页</a>]<br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>