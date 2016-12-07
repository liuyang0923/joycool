<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%!static int COUNT_PRE_PAGE = 5;%>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
String tip = "";
String tmp = "";
String call = "";
String call2 = "";
int limit = 5;
int p = action.getParameterInt("p");
int start = p*limit;
int end = 0;
int back = action.getParameterInt("b"); //0:返回投票页 1:返回我的赛况
MatchTrends trend = null;
int uid = action.getParameterInt("uid");
List trendsList = MatchAction.service.getTrendsList(" flag=0 and right_uid=" + uid + " order by id desc limit " + start + ",6");
if (trendsList == null || trendsList.size() < 0){
	tip = "没有找到该用户的动态.";
}
if (trendsList.size() > limit){
	end = trendsList.size() - 1;
} else {
	end = trendsList.size();
}
if (end == 0){
	tip = "目前还没有粉丝为您投票.";
}
String other = "她";
if (loginUser != null && loginUser.getId() == uid ){
	other = "我";
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="全部粉丝"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
for (int i = 0 ; i < end ; i++){
	trend = (MatchTrends)trendsList.get(i);
	if (trend != null && trend.getContent() != null){
		tmp = trend.getContent().replace("%l","<a href=\"/user/ViewUserInfo.do?userId=" + trend.getLeftUid() + "\">" + UserInfoUtil.getUser(trend.getLeftUid()).getNickNameWml() + "</a>");
		tmp = tmp.replace("%r",other);
		%><%=DateUtil.sformatTime(trend.getCreateTime())%><%=tmp%><br/><%
	}
}
if (trendsList.size() > 5){
%><a href="allFans.jsp?uid=<%=uid%>&amp;p=<%=p+1%>&amp;b=<%=back%>">下一页</a><%
}
if (p > 0){
	%>|<a href="allFans.jsp?uid=<%=uid%>&amp;p=<%=p-1%>&amp;b=<%=back%>">上一页</a><br/><%
} else {
	%><br/><%
}
if (back == 0){
	%><a href="vote.jsp?uid=<%=uid%>">返回投票页</a><br/><%
} else {
	%><a href="my.jsp">返回我的赛况</a><br/><%
}
} else {
%><%=tip%><br/><br/><%
}%><a href="index.jsp">返回乐后首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>