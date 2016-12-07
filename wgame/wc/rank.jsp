<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="jc.game.worldcup.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
UserBean loginUser = action.getLoginUser();
String tip = "";
int p = action.getParameterInt("p");
int start = p*COUNT_PRE_PAGE;
int end = 0;
WcRank wcRank = null;
WcRank wcMyRank = null;
WcUser wcUser = null;
WcInfo wcInfo = WorldCupAction.getWcInfo();
if (loginUser != null){
	wcMyRank = WorldCupAction.service.getRank(" user_id=" + loginUser.getId());
	wcUser = action.getUser(loginUser.getId());
}
List list = WorldCupAction.service.getRankList(" 1 limit " + start + "," +  (COUNT_PRE_PAGE+1));
if (list.size() > COUNT_PRE_PAGE){
	end = list.size() - 1;
} else {
	end = list.size();
}
int point=0,point2=0;
if (wcUser != null){
	point = wcUser.getPoint();
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="竞猜"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%>您目前<%=wcMyRank!=null?"排名" + wcMyRank.getId() : "没进入名次"%>,再接再厉哦!<br/><%
for (int i = 0 ; i < end ; i++){
	wcRank = (WcRank)list.get(i);
	if (wcRank != null){
		%><%=wcRank.getId()%>.<a href="history.jsp?uid=<%=wcRank.getUserId()%>&amp;b=1"><%=UserInfoUtil.getUser(wcRank.getUserId()).getNickNameWml()%></a>(<%=wcRank.getPoint()%>)<br/><%
	}
}
if (list.size() > COUNT_PRE_PAGE){
	%><a href="rank.jsp?p=<%=p+1%>">下一页</a><%if(p > 0){%>|<%} else {%><br/><%}%><%
}
if (p > 0){
	%><a href="rank.jsp?p=<%=p-1%>">上一页</a><br/><%
}
} else {
%><%=tip%><br/><%	
}%>
[<a href="index.jsp">返回投注首页</a>]<br/>
<%if (wcInfo != null && wcInfo.getSubjectId() > 0){
%>[<a href="/Column.do?columnId=<%=wcInfo.getSubjectId()%>">返回竞猜首页</a>]<br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>