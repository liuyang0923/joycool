<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%!static int COUNT_PRE_PAGE = 5;%>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
int limit = 5;
int p = action.getParameterInt("p");
int start = p*limit;
int end = 0;
String tip = "";
String tmp = "";
MatchTrends trend = null;
//MatchVote vote = null;
//MatchFansCons cons = null;
PagingBean paging = null;
List consList = null;
List trendsList = null;
trendsList = MatchAction.service.getTrendsList(" left_uid=" + loginUser.getId() + " and flag=1 order by id desc limit " + start + ",6");
if(trendsList == null || trendsList.size() <= 0){
	tip ="您还没有过任何物品.马上去商城<a href=\"shop.jsp\">购买</a>.";
}
end = trendsList.size();
//consList = MatchAction.service.getFansConsList(" user_id=" + loginUser.getId() + " order by id desc limit " + start + ",6");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买记录"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if(trendsList.size() > limit){
	end = trendsList.size() - 1;
}
if(trendsList != null && trendsList.size() > 0){
	for (int i = 0 ; i < end ; i++){
		trend = (MatchTrends)trendsList.get(i);
		//cons = (MatchFansCons)consList.get(i);
		if (trend != null && trend.getContent() != null){
			tmp = trend.getContent().replace("%l","");
			%><%=DateUtil.sformatTime(trend.getCreateTime())%><%=tmp%><br/><%
		}
	}
}
if (trendsList.size() > 5){
%><a href="buyLog.jsp?p=<%=p+1%>">下一页</a><%
}
if (p > 0){
%>|<a href="buyLog.jsp?p=<%=p-1%>">上一页</a><%
}
%><br/><%
} else {
%><%=tip%><br/><%
}
%><a href="shop.jsp">奢侈品商城</a>|<a href="index.jsp">返回乐后首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>